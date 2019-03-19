package com.example.ldrp.medeeasyapp.doctor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ldrp.medeeasyapp.ForgotPasswordActivity;
import com.example.ldrp.medeeasyapp.R;
import com.example.ldrp.medeeasyapp.app.AppConfig;
import com.example.ldrp.medeeasyapp.patient.PatientLoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEd;
    private EditText passwordEd;
    private Button loginBtn;
    private TextView newUserTv;
    private TextView forgotPasswordTv;


    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        initView();

    }

    private void initView() {

        emailEd = findViewById(R.id.actvitiy_doctor_login_email_ed);
        passwordEd = findViewById(R.id.actvitiy_doctor_login_password_ed);
        loginBtn = findViewById(R.id.actvitiy_doctor_login_btn);
        newUserTv = findViewById(R.id.actvitiy_doctor_login_new_doctor_tv);
        forgotPasswordTv = findViewById(R.id.actvitiy_doctor_login_forgot_pwd_tv);

        progressDialog = new ProgressDialog(DoctorLoginActivity.this);

        loginBtn.setOnClickListener(this);
        newUserTv.setOnClickListener(this);
        forgotPasswordTv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.actvitiy_doctor_login_new_doctor_tv:
                final Intent gotoNewUser = new Intent(DoctorLoginActivity.this, DoctorRegistrationActivity.class);
                startActivity(gotoNewUser);
                break;
            case R.id.actvitiy_doctor_login_btn:
                doctorLogin();
                break;
            case R.id.actvitiy_doctor_login_forgot_pwd_tv:
                final Intent gotoForgotPwd = new Intent(DoctorLoginActivity.this, ForgotPasswordActivity.class);
                startActivity(gotoForgotPwd);
                break;
        }

    }

    private void doctorLogin() {
        progressDialog.setTitle("Doctor Login");
        progressDialog.setMessage("Credential Verifying...");
        progressDialog.show();

        final String email = emailEd.getText().toString().trim();
        final String password = passwordEd.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please Enter the details", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(DoctorLoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                dataVerify(firebaseAuth.getCurrentUser().getUid());
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(DoctorLoginActivity.this, "Error : " + task.getException(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

    private void dataVerify(String uid) {
        progressDialog.setMessage("verifying Details");
        databaseReference.child(AppConfig.FIREBASE_DB_DOCTOR)
                .child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressDialog.dismiss();
                    SharedPreferences sharedpreferences;
                    sharedpreferences = getSharedPreferences("login.xml", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("TYPE", "1");
                    editor.apply();
                    Toast.makeText(DoctorLoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                    final Intent gotoDoctorHome = new Intent(DoctorLoginActivity.this, DoctorHomeActivity.class);
                    startActivity(gotoDoctorHome);
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(DoctorLoginActivity.this, "Not Right User to Login", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
