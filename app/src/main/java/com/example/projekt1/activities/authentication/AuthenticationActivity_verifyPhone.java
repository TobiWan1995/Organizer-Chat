package com.example.projekt1.activities.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projekt1.R;
import com.example.projekt1.activities.login.LoginActivity;
import com.example.projekt1.models.User;
import com.goodiebag.pinview.Pinview;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AuthenticationActivity_verifyPhone extends AppCompatActivity implements View.OnClickListener {
    // Setup Firebase-Database
    FirebaseDatabase root =  FirebaseDatabase.getInstance();
    // Get User-Table-Reference from FireDB
    DatabaseReference userRef = root.getReference("User");
    // Firebase-Authentication
    FirebaseAuth firebaseAuth;

    // authVP - ViewElements
    Button nextVPhone;
    Pinview pinview;

    // auth Data
    String phoneNumber;
    String codeBySystem;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.authenticate_verifyphone);

            // init phoneVerification
            phoneNumber = getIntent().getStringExtra("phoneNumber");

            nextVPhone = findViewById(R.id.nextPV);
            nextVPhone.setOnClickListener(this);

            pinview = findViewById(R.id.pinview);

            // send phoneVerification TODO: uncomment on deployment
            sendPhoneVerificationCode(phoneNumber);

            // for testing - TODO: delete line on deployment
            // saveUserToFirebase();
        }

        void sendPhoneVerificationCode(String phoneNo){
            firebaseAuth = FirebaseAuth.getInstance();
            FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();

            // Configure faking the auto-retrieval with the whitelisted numbers.
            // firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNo, "1234");

            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                            .setPhoneNumber(phoneNo) // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this) // Activity (for callback binding)
                            .setCallbacks(mCallbacks) // OnVerificationStateChangedCallbacks
                            .build();


            PhoneAuthProvider.verifyPhoneNumber(options);
        }

    @Override
    public void onClick(View v) {
        String code = pinview.getValue();
        if(!code.isEmpty()){
            // check code
            verifyCode(code);
        }
        else {
            Toast.makeText(getApplicationContext(), "Code is null", Toast.LENGTH_SHORT).show();
        }
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =  new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
            String code  = phoneAuthCredential.getSmsCode();
            if(code!=null){
                pinview.setValue(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken){
            super.onCodeSent(s, forceResendingToken);
            codeBySystem = s;
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    String TAG = "";
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");

                        FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();

                        saveUserToFirebase();

                        // Update UI
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);
                        finish();
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast.makeText(getApplicationContext(), "Verification-Code invalid", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void saveUserToFirebase() {
        // get UserData from Intent
        String fullName, username, eMail, password, gender, birth;

        fullName = getIntent().getStringExtra("fullname");
        username = getIntent().getStringExtra("username");
        eMail = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        gender = getIntent().getStringExtra("gender");
        birth = getIntent().getStringExtra("birth");

        // generate unique ID
        String key =  userRef.push().getKey();
        // throw assertion-error if null
        assert key != null;
        // initial userList
        // ArraySet<String> users = new ArraySet<>();

        User newUser = new User(key, fullName, username, eMail, password, gender, birth, phoneNumber);

        userRef.child(key).setValue(newUser);

        // TODO: remove on deployment
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
        finish();
    }
}
