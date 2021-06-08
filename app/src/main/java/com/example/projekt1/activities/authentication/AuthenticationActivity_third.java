package com.example.projekt1.activities.authentication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.MailTo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.projekt1.R;
import com.hbb20.CountryCodePicker;

public class AuthenticationActivity_third extends AppCompatActivity {
    // auth next Buttons
    Button next3;
    // auth3 Data
    String phoneNumber;
    String mPhoneNumber;

    static final int PERMISSON_READ_STATE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authenticate_stage3);

        // display device-phoneNumber
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSON_READ_STATE);

        // init Stage3
        CountryCodePicker countryCodePicker = findViewById(R.id.countryPicker);
        EditText editTextPhone = findViewById(R.id.editTextPhone);

        // navigate through authentication
        next3 = findViewById(R.id.authNext3);

        next3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get Data from Stage3
                phoneNumber = countryCodePicker.getSelectedCountryCode() + editTextPhone.getText().toString();

                // next - authentication verifyPhone
                Intent intent = new Intent(getApplicationContext(), AuthenticationActivity_verifyPhone.class);

                // pass data from Stage1
                intent.putExtra("username", getIntent().getStringExtra("username"));
                intent.putExtra("fullname", getIntent().getStringExtra("fullname"));
                intent.putExtra("email", getIntent().getStringExtra("email"));
                intent.putExtra("password", getIntent().getStringExtra("password"));

                // pass data from Stage2
                intent.putExtra("gender", getIntent().getStringExtra("gender"));
                intent.putExtra("birth", getIntent().getStringExtra("birth"));

                // pass data from Stage3
                intent.putExtra("phoneNumber", mPhoneNumber);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSON_READ_STATE: {
                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mPhoneNumber = tMgr.getLine1Number();
                    System.out.println(mPhoneNumber);
                } else {
                    Toast.makeText(getApplicationContext(), "You do not have the Permisson.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}