package com.example.ldrp.medeeasyapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ldrp.medeeasyapp.app.AppConfig;
import com.example.ldrp.medeeasyapp.model.PatientModel;
import com.example.ldrp.medeeasyapp.patient.PatientRegistrationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nameEd;
    private EditText emailEd;
    private EditText passwordEd;
    private EditText mobileEd;
    private EditText addressEd;
    private EditText ageEd;
    private Button patientSingupBtn;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        initView();
    }

    private void initView() {

        nameEd = findViewById(R.id.activity_patient_profile_name_ed);
        emailEd = findViewById(R.id.activity_patient_profile_email_ed);
        passwordEd = findViewById(R.id.activity_patient_profile_password_ed);
        mobileEd = findViewById(R.id.activity_patient_profile_mobile_ed);
        addressEd = findViewById(R.id.activity_patient_profile_address_ed);
        ageEd = findViewById(R.id.activity_patient_profile_age);

        progressDialog = new ProgressDialog(PatientProfileActivity.this);

        patientSingupBtn = findViewById(R.id.activity_patient_profile_signup_btn);
        patientSingupBtn.setOnClickListener(this);


        databaseReference.child(AppConfig.FIREBASE_DB_PATIENT)
                .child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PatientModel patientModel = dataSnapshot.getValue(PatientModel.class);

                nameEd.setText(patientModel.getName());
                emailEd.setText(patientModel.getEmail());
                mobileEd.setText(patientModel.getMobile());
                passwordEd.setText(patientModel.getPassword());
                addressEd.setText(patientModel.getAddress());
                ageEd.setText(patientModel.getAge());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
        final String age = ageEd.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()
                || mobile.isEmpty() || address.isEmpty() || age.isEmpty()) {
            Toast.makeText(this, "Please fill the details", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else {

            progressDialog.setMessage("Data Updating.....");

            databaseReference.child(AppConfig.FIREBASE_DB_PATIENT)
                    .child(firebaseAuth.getCurrentUser().getUid())
                    .setValue(new PatientModel(name, email, password, mobile,
                            address, age), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                progressDialog.dismiss();
                                Toast.makeText(PatientProfileActivity.this, "Error " + databaseError.getDetails(), Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(PatientProfileActivity.this, "Patient Profile Updated", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
        }

    }
}
