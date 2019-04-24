package com.example.laboratory;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nameEd;
    private EditText emailEd;
    private EditText passwordEd;
    private EditText mobileEd;
    private EditText addressEd;
    private EditText licenseEd;
    private EditText typesStockEd;
    private Button signupBtn;
    private TextView oldUserTv;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();
    }

    private void initView() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        nameEd = findViewById(R.id.activity_profile_name_ed);
        emailEd = findViewById(R.id.activity_profile_email_ed);
        passwordEd = findViewById(R.id.activity_profile_password_ed);
        mobileEd = findViewById(R.id.activity_profile_mobile_ed);
        addressEd = findViewById(R.id.activity_profile_address_ed);
        licenseEd = findViewById(R.id.activity_profile_license_ed);
        typesStockEd = findViewById(R.id.activity_profile_types_stock_ed);
        signupBtn = findViewById(R.id.activity_profile_signup_btn);
        progressDialog = new ProgressDialog(ProfileActivity.this);
        signupBtn.setOnClickListener(this);


        databaseReference.child(AppConfig.FIREBASE_DB_LABORATORY)
                .child(firebaseAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        LaboratoryModel laboratoryModel = dataSnapshot.getValue(LaboratoryModel.class);

                        nameEd.setText(laboratoryModel.getName());
                        emailEd.setText(laboratoryModel.getEmail());
                        passwordEd.setText(laboratoryModel.getPassword());
                        mobileEd.setText(laboratoryModel.getMobile());
                        addressEd.setText(laboratoryModel.getAddress());
                        licenseEd.setText(laboratoryModel.getLicense());
                        typesStockEd.setText(laboratoryModel.getServices());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    @Override
    public void onClick(View v) {
        final String email = emailEd.getText().toString().trim();
        final String password = passwordEd.getText().toString().trim();
        final String name = nameEd.getText().toString().trim();
        final String mobile = mobileEd.getText().toString().trim();
        final String address = addressEd.getText().toString().trim();
        final String license = licenseEd.getText().toString().trim();
        final String types = typesStockEd.getText().toString().trim();

        progressDialog.setTitle("Profile Update");
        progressDialog.setMessage("profile updating....");
        progressDialog.show();

        if (email.isEmpty() || password.isEmpty() || name.isEmpty()
                || mobile.isEmpty() || address.isEmpty() || types.isEmpty() || license.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(this, "Details not entered", Toast.LENGTH_SHORT).show();
        } else {
            databaseReference.child(AppConfig.FIREBASE_DB_LABORATORY)
                    .child(firebaseAuth.getCurrentUser().getUid())
                    .setValue(new LaboratoryModel(name, email, password, mobile, address, license, types), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            if (databaseError != null) {
                                Toast.makeText(ProfileActivity.this, "Error :" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        }
                    });
        }
    }
}
