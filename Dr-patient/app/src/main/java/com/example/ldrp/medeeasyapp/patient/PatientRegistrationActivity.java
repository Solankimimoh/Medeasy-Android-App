package com.example.ldrp.medeeasyapp.patient;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ldrp.medeeasyapp.R;
import com.example.ldrp.medeeasyapp.app.AppConfig;
import com.example.ldrp.medeeasyapp.model.PatientModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PatientRegistrationActivity extends AppCompatActivity implements View.OnClickListener {


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
        setContentView(R.layout.activity_patient_registration);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        initView();

    }

    private void initView() {

        nameEd = findViewById(R.id.activity_patient_registration_name_ed);
        emailEd = findViewById(R.id.activity_patient_registration_email_ed);
        passwordEd = findViewById(R.id.activity_patient_registration_password_ed);
        mobileEd = findViewById(R.id.activity_patient_registration_mobile_ed);
        addressEd = findViewById(R.id.activity_patient_registration_address_ed);
        ageEd = findViewById(R.id.activity_patient_registration_age);

        progressDialog = new ProgressDialog(PatientRegistrationActivity.this);

        patientSingupBtn = findViewById(R.id.activity_patient_registration_signup_btn);
        patientSingupBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {


        progressDialog.setTitle("Account Create");
        progressDialog.setMessage("Account Creating...");
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
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(PatientRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                insertInToDatabase(name, email, password, mobile, address, age);
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(PatientRegistrationActivity.this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        }


    }

    private void insertInToDatabase(String name, String email, String password, String mobile, String address, String age) {

        progressDialog.setMessage("Data inserting...");

        databaseReference.child(AppConfig.FIREBASE_DB_PATIENT)
                .child(firebaseAuth.getCurrentUser().getUid())
                .setValue(new PatientModel(name, email, password, mobile,
                        address, age), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            progressDialog.dismiss();
                            Toast.makeText(PatientRegistrationActivity.this, "Error " + databaseError.getDetails(), Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(PatientRegistrationActivity.this, "Patient Profile Created", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }
}