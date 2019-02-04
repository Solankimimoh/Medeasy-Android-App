package com.example.laboratory;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEd;
    private Button forgotPwdBtn;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initView();
    }

    private void initView() {

        firebaseAuth = FirebaseAuth.getInstance();
        emailEd = findViewById(R.id.activity_forgot_password_email_ed);
        forgotPwdBtn = findViewById(R.id.activity_forgot_password_btn);

        forgotPwdBtn.setOnClickListener(this);
        progressDialog = new ProgressDialog(ForgotPasswordActivity.this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_forgot_password_btn:
                resetPassword();
                break;
        }
    }

    private void resetPassword() {
        final String email = emailEd.getText().toString().trim();

        progressDialog.setTitle("Forgot password");
        progressDialog.setMessage("sending email....");
        progressDialog.show();

        if (email.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(this, "Email is not entered", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(ForgotPasswordActivity.this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(ForgotPasswordActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(ForgotPasswordActivity.this, "Password reset link send", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}