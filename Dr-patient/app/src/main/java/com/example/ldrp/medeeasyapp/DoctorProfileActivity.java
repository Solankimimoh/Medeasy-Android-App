package com.example.ldrp.medeeasyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ldrp.medeeasyapp.app.AppConfig;
import com.example.ldrp.medeeasyapp.doctor.DoctorRegistrationActivity;
import com.example.ldrp.medeeasyapp.model.DoctorModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nameEd;
    private EditText emailEd;
    private EditText passwordEd;
    private EditText mobileEd;
    private EditText addressEd;
    private EditText licenseEd;
    private EditText educationEd;
    private EditText typesEd;
    private Button doctorSingupBtn;
    private Button chooseLicensebtn;


    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        initView();

        databaseReference.child(AppConfig.FIREBASE_DB_DOCTOR)
                .child(firebaseAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DoctorModel doctorModel = dataSnapshot.getValue(DoctorModel.class);
                        nameEd.setText(doctorModel.getName());
                        emailEd.setText(doctorModel.getEmail());
                        passwordEd.setText(doctorModel.getPassword());
                        mobileEd.setText(doctorModel.getMobile());
                        addressEd.setText(doctorModel.getAddress());
                        licenseEd.setText(doctorModel.getLicense());
                        educationEd.setText(doctorModel.getEducation());
                        typesEd.setText(doctorModel.getTypes());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void initView() {

        nameEd = findViewById(R.id.activity_doctor_profile_name_ed);
        emailEd = findViewById(R.id.activity_doctor_profile_email_ed);
        passwordEd = findViewById(R.id.activity_doctor_profile_password_ed);
        mobileEd = findViewById(R.id.activity_doctor_profile_mobile_ed);
        addressEd = findViewById(R.id.activity_doctor_profile_address_ed);
        licenseEd = findViewById(R.id.activity_doctor_profile_license_ed);
        educationEd = findViewById(R.id.activity_doctor_profile_educatuion_ed);
        typesEd = findViewById(R.id.activity_doctor_profile_types);

        progressDialog = new ProgressDialog(DoctorProfileActivity.this);

        doctorSingupBtn = findViewById(R.id.activity_doctor_profile_btn);
        doctorSingupBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        progressDialog.setTitle("Account Update");
        progressDialog.setMessage("Account updating...");
        progressDialog.show();
        final String name = nameEd.getText().toString().trim();
        final String email = emailEd.getText().toString().trim();
        final String password = passwordEd.getText().toString().trim();
        final String mobile = mobileEd.getText().toString().trim();
        final String address = addressEd.getText().toString().trim();
        final String license = licenseEd.getText().toString().trim();
        final String education = educationEd.getText().toString().trim();
        final String types = typesEd.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()
                || mobile.isEmpty() || address.isEmpty() || license.isEmpty() ||
                education.isEmpty() || types.isEmpty()) {
            Toast.makeText(this, "Please fill the details", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else {

            databaseReference.child(AppConfig.FIREBASE_DB_DOCTOR)
                    .child(firebaseAuth.getCurrentUser().getUid())
                    .setValue(new DoctorModel(name, email, password, mobile,
                            address, license, education, types), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                progressDialog.dismiss();
                                Toast.makeText(DoctorProfileActivity.this, "Error " + databaseError.getDetails(), Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(DoctorProfileActivity.this, "Doctor Profile Update", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
        }
    }
}
