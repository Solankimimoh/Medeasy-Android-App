package com.example.ldrp.medeeasyapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ldrp.medeeasyapp.app.AppConfig;
import com.example.ldrp.medeeasyapp.model.AppoinmentModel;
import com.example.ldrp.medeeasyapp.model.LaboratoryAppoinmentModel;
import com.example.ldrp.medeeasyapp.model.PatientReviewModel;
import com.example.ldrp.medeeasyapp.model.PrescriptionModel;
import com.example.ldrp.medeeasyapp.patient.BookAppoinmentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class LaboratoryBookingActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText purposeEd;
    private TextView dateTv;
    private TextView timeTv;
    private EditText remarkEd;
    private Button bookBtn;
    private Intent intent;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laboratory_booking);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        intent = getIntent();

        initView();
    }

    private void initView() {

        purposeEd = findViewById(R.id.activity_laboratory_booking_purpose);
        dateTv = findViewById(R.id.activity_laboratory_booking_date);
        timeTv = findViewById(R.id.activity_laboratory_booking_time);
        remarkEd = findViewById(R.id.activity_laboratory_booking_remarks);
        bookBtn = findViewById(R.id.activity_laboratory_booking_btn);

        dateTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);
        bookBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_laboratory_booking_btn:
                bookAppoinment();
                break;

            case R.id.activity_laboratory_booking_date:
                openDatePicker();
                break;

            case R.id.activity_laboratory_booking_time:
                openTimePicker();
                break;

        }
    }


    private void bookAppoinment() {

        final String purpose = purposeEd.getText().toString().trim();
        final String date = dateTv.getText().toString().trim();
        final String time = timeTv.getText().toString().trim();
        final String remarks = remarkEd.getText().toString().trim();
        final String laboratoryUid = intent.getStringExtra(AppConfig.KEY_LABORATORY_UID);
        final String uid = firebaseAuth.getCurrentUser().getUid();


        if (purpose.isEmpty() || date.isEmpty()
                || time.isEmpty() || remarks.isEmpty()) {
            Toast.makeText(this, "Please Enter the Details", Toast.LENGTH_SHORT).show();
        } else {

            if (!uid.isEmpty()) {

                databaseReference.child(AppConfig.FIREBASE_DB_LABORATORY_APPOINMENT)
                        .child(laboratoryUid)
                        .child(uid)
                        .push()
                        .setValue(new LaboratoryAppoinmentModel(purpose, date, time, remarks,false), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Toast.makeText(LaboratoryBookingActivity.this, "Database Error :" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                } else {

                                    Toast.makeText(LaboratoryBookingActivity.this, "Appoinment Success ", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });

            } else {
                Toast.makeText(this, "Try Relogin", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openDatePicker() {

        final Calendar calendar = Calendar.getInstance();
        final int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        final int mMonth = calendar.get(Calendar.MONTH);
        final int mYear = calendar.get(Calendar.YEAR);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(LaboratoryBookingActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateTv.setText(dayOfMonth + "/" + (month+1) + "/" + year);

            }
        }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    private void openTimePicker() {

        final Calendar calendar = Calendar.getInstance();

        final int mHour = calendar.get(Calendar.HOUR);
        final int mMinute = calendar.get(Calendar.MINUTE);

        final TimePickerDialog timePickerDialog = new TimePickerDialog(LaboratoryBookingActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeTv.setText(hourOfDay + ":" + minute);
            }
        }, mHour, mMinute, false);

        timePickerDialog.show();
    }

}
