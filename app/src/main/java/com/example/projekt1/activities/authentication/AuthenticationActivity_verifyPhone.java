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
import com.example.projekt1.activities.launcher.LauncherActivity;
import com.example.projekt1.activities.login.LoginActivity;
import com.example.projekt1.models.Session;
import com.example.projekt1.models.User;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
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

import java.util.concurrent.TimeUnit;

public class AuthenticationActivity_verifyPhone extends AppCompatActivity implements View.OnClickListener {
    // Setup Firebase-Database
    FirebaseDatabase root =  FirebaseDatabase.getInstance();
    // Get User-Table-Reference from FireDB
    DatabaseReference userref = root.getReference("User");
    // Firebase-Authentication
    FirebaseAuth firebaseAuth;

    // authVP - ViewElements
    Button nextVPhone;
    Pinview pinview;

    // auth Data
    String phoneNumber;
    String codeBySystem;

    // Session for current User
    Session session;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.authenticate_verifyphone);

            // init phoneVerification
            phoneNumber = getIntent().getStringExtra("phoneNumber");

            nextVPhone = findViewById(R.id.nextPV);
            nextVPhone.setOnClickListener(this::onClick);

            pinview = findViewById(R.id.pinview);


            // send phoneVerification TODO: uncomment on deployment
            // sendPhoneVerificationCode(phoneNumber);

            // for testing - TODO: delete line on deployment
            saveUserToFirebase();
        }

        void sendPhoneVerificationCode(String phoneNo){
            firebaseAuth = FirebaseAuth.getInstance();
            FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();

            System.out.println("PhoneNumber for Verification: " + phoneNo);

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
            Toast.makeText(getApplicationContext(), "Code is null.", Toast.LENGTH_SHORT).show();
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
            System.out.println("Code sent.");
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    private String TAG = "";

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();

                            saveUserToFirebase();

                            // Update UI
                            /* Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);
                            finish(); */

                            System.out.println("Verification success.");
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getApplicationContext(), "Verification-Code invalid.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    public void saveUserToFirebase() {
        // get UserData from Intent
        String fullname, username, eMail, password, gender, birth;

        fullname = getIntent().getStringExtra("fullname");
        username = getIntent().getStringExtra("username");
        eMail = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        gender = getIntent().getStringExtra("gender");
        birth = getIntent().getStringExtra("birth");

        // generate unique ID
        String key =  userref.push().getKey();

        // initial userList
        ArraySet<String> users = new ArraySet<>();
        users.add(key);

        User newUser = new User(key, fullname, username, eMail, password, gender, birth, users);
        userref.child(key).setValue(newUser);

        // TODO: remove on deployment
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
        finish();
    }
}
