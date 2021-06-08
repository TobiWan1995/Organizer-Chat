package com.example.projekt1.activities.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekt1.R;

public class AuthenticationActivity_second extends AppCompatActivity {
    // auth next Buttons
    Button next2;
    // auth2 data
    String gender, birth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authenticate_stage2);

        // init Stage2
        RadioGroup radioGender = findViewById(R.id.radioGroup);
        Button buttonBirth = findViewById(R.id.buttonBirth);

        // navigate through authentication
        next2 = findViewById(R.id.authNext2);

        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get Data from Stage2
                int checkedGenderId = radioGender.getCheckedRadioButtonId();
                RadioButton checkedGenderRadioButton = findViewById(checkedGenderId);
                gender = String.valueOf(checkedGenderRadioButton.getText());
                birth = buttonBirth.getText().toString();

                // next - authentication 2
                Intent intent = new Intent(getApplicationContext(), AuthenticationActivity_third.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // pass data from Stage1
                intent.putExtra("username", getIntent().getStringExtra("username"));
                intent.putExtra("fullname",  getIntent().getStringExtra("fullname"));
                intent.putExtra("email",  getIntent().getStringExtra("email"));
                intent.putExtra("password",  getIntent().getStringExtra("password"));

                // pass data from Stage2
                intent.putExtra("gender", gender);
                intent.putExtra("birth", birth);

                getApplicationContext().startActivity(intent);

            }
        });
    }
}