package com.example.ldrp.medeeasyapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginSelectionActivity extends AppCompatActivity implements View.OnClickListener {


    private Button doctorNavigationBtn;
    private Button patientNavigationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_selection);
        initView();
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
                break;
            case R.id.activity_login_selection_patient_btn:

//                final Intent gotoPatientLogin = new Intent(LoginSelectionActivity.this,
//                        );
//                startActivity(gotoPatientLogin);

                break;
        }

    }
}
