package com.example.ldrp.medeasymedical;

import android.content.Intent;
import android.support.annotation.NonNull;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEd;
    private EditText passwordEd;
    private Button loginbtn;
    private TextView forgotpwdTv;
    private TextView newuserTv;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

    }

    private void initView() {
        firebaseAuth = FirebaseAuth.getInstance();
        emailEd = findViewById(R.id.activity_login_email_ed);
        passwordEd = findViewById(R.id.activity_login_password_ed);
        loginbtn = findViewById(R.id.activity_login_btn);
        forgotpwdTv = findViewById(R.id.activity_login_forgot_pwd_tv);
        newuserTv = findViewById(R.id.activity_login_new_user_tv);


        loginbtn.setOnClickListener(this);
        forgotpwdTv.setOnClickListener(this);
        newuserTv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_login_btn:
                userLogin();
                break;
            case R.id.activity_login_forgot_pwd_tv:
                forgotPassword();
                break;
            case R.id.activity_login_new_user_tv:
                newUser();
                break;
        }

    }

    private void userLogin() {
        final String email = emailEd.getText().toString().trim();
        final String password = passwordEd.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please Enter details", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Invalid Details! try Again", Toast.LENGTH_SHORT).show();
                            } else {

                            }
                        }
                    });
        }
    }


    private void forgotPassword() {
        final Intent gotoForgotPassword = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(gotoForgotPassword);
    }

    private void newUser() {
        final Intent gotoNewUser = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(gotoNewUser);
    }
}