package com.example.projekt1.activities.authentication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekt1.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class AuthenticationActivity_second extends AppCompatActivity {
    // auth next Buttons
    Button next2;
    // auth2 data
    String gender, birth;
    // Listener for picked Date
    DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authenticate_stage2);

        // init Stage2
        Spinner spinnerGender = findViewById(R.id.select_gender);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item , new String[]{"male", "female", "diverse"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        TextView buttonBirth = findViewById(R.id.buttonBirth);

        // set Calender for Datepicker-Dialog
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        // set Datepicker-Listener
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth+"/"+month+"/"+year;
                buttonBirth.setText(date);
            }
        };

        // set Datepicker-Dialog and Button
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        buttonBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        // navigate through authentication
        next2 = findViewById(R.id.authNext2);

        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get Data from Stage2
                gender = String.valueOf(spinnerGender.getSelectedItem().toString());
                birth = buttonBirth.getText().toString();

                if(birth.isEmpty() || !isValidDate(birth)) {
                    Toast.makeText(getApplicationContext(), "Invalid Date", Toast.LENGTH_LONG).show();
                    return;
                }

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
                finish();
            }
        });
    }

    // datepicker validation
    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
}