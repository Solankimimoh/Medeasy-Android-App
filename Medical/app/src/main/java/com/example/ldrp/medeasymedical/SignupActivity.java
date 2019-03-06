package com.example.ldrp.medeasymedical;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEd;
    private EditText emailEd;
    private EditText passwordEd;
    private EditText mobileEd;
    private EditText addressEd;
    private EditText typesStockEd;
    private EditText licenseEd;
    private Button signupBtn;
    private TextView oldUserTv;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initView();

    }

    private void initView() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        nameEd = findViewById(R.id.activity_signup_name_ed);
        emailEd = findViewById(R.id.activity_signup_email_ed);
        passwordEd = findViewById(R.id.activity_signup_password_ed);
        mobileEd = findViewById(R.id.activity_signup_mobile_ed);
        addressEd = findViewById(R.id.activity_signup_address_ed);
        typesStockEd = findViewById(R.id.activity_signup_types_stock_ed);
        signupBtn = findViewById(R.id.activity_signup_signup_btn);
        licenseEd = findViewById(R.id.activity_signup_license_ed);
        oldUserTv = findViewById(R.id.activity_signup_old_user_tv);
        progressDialog = new ProgressDialog(SignupActivity.this);

        signupBtn.setOnClickListener(this);
        oldUserTv.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_signup_signup_btn:
                userRegister();
                break;
            case R.id.activity_signup_old_user_tv:
                finish();
                break;
        }

    }

    private void userRegister() {

        final String email = emailEd.getText().toString().trim();
        final String password = passwordEd.getText().toString().trim();
        final String name = nameEd.getText().toString().trim();
        final String mobile = mobileEd.getText().toString().trim();
        final String address = addressEd.getText().toString().trim();
        final String license = licenseEd.getText().toString().trim();
        final String types = typesStockEd.getText().toString().trim();

        progressDialog.setTitle("Signup user");
        progressDialog.setMessage("Creating user....");
        progressDialog.show();

        if (email.isEmpty() || password.isEmpty() || name.isEmpty()
                || mobile.isEmpty() || address.isEmpty() || types.isEmpty() || license.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(this, "Details not entered", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(SignupActivity.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(SignupActivity.this, "Error :" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressDialog.dismiss();

                        insertIntoDatabase(name, email, password, mobile, address, license, types);


                    }
                }
            });

        }


    }

    private void insertIntoDatabase(String name, String email, String password, String mobile, String address, String license, String types) {


        databaseReference.child(AppConfig.FIREBASE_DB_MEDICAL)
                .child(firebaseAuth.getCurrentUser().getUid())
                .setValue(new MedicalModel(name, email, password, mobile, address, types, license), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Toast.makeText(SignupActivity.this, "Error " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignupActivity.this, "Registerd SuccessFully ", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    }
                });

    }

}
