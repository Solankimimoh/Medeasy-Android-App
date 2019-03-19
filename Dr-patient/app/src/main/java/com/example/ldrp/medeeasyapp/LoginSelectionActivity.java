package com.example.ldrp.medeeasyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ldrp.medeeasyapp.doctor.DoctorHomeActivity;
import com.example.ldrp.medeeasyapp.doctor.DoctorLoginActivity;
import com.example.ldrp.medeeasyapp.patient.PatientLoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginSelectionActivity extends AppCompatActivity implements View.OnClickListener {


    private Button doctorNavigationBtn;
    private Button patientNavigationBtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_selection);
        firebaseAuth = FirebaseAuth.getInstance();
        initView();
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences("login.xml", Context.MODE_PRIVATE);

        String type = sharedpreferences.getString("TYPE", "");


        if (firebaseAuth.getCurrentUser() != null) {
            if (type != null) {
                if (type.equals("0")) {
                    final Intent gotoPatient = new Intent(LoginSelectionActivity.this, PatientHomeActivity.class);
                    startActivity(gotoPatient);
                    finish();
                } else if (type.equals("1")) {
                    final Intent gotoDoctor = new Intent(LoginSelectionActivity.this, DoctorHomeActivity.class);
                    startActivity(gotoDoctor);
                    finish();
                }
            }
        }
    }

    private void initView() {

        doctorNavigationBtn = findViewById(R.id.activity_login_selection_doctor_btn);
        patientNavigationBtn = findViewById(R.id.activity_login_selection_patient_btn);

        doctorNavigationBtn.setOnClickListener(this);
        patientNavigationBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_login_selection_doctor_btn:

                final Intent gotoDoctorLogin = new Intent(LoginSelectionActivity.this,
                        DoctorLoginActivity.class);
                startActivity(gotoDoctorLogin);
                finish();
                break;
            case R.id.activity_login_selection_patient_btn:

                final Intent gotoPatientLogin = new Intent(LoginSelectionActivity.this,
                        PatientLoginActivity.class);
                startActivity(gotoPatientLogin);
                finish();
                break;
        }

    }
}
