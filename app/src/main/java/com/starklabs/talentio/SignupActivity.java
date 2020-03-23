package com.starklabs.talentio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    TextInputEditText signup_email,signup_pass;
    MaterialButton signup_submit;
    TextView signup_signin;
    private FirebaseAuth mAuth;
    ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        mProgressBar=findViewById(R.id.signup_progress_bar);
        signup_email=findViewById(R.id.signup_email);
        signup_pass=findViewById(R.id.signup_password);
        signup_submit=findViewById(R.id.signup_submit);
        signup_signin=findViewById(R.id.tv_signin);

        signup_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        signup_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void registerUser() {
        String email=signup_email.getText().toString().trim();
        String password=signup_pass.getText().toString().trim();
        if(email.isEmpty())
        {
            signup_email.setError("E-mail is required");
            signup_email.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            signup_email.setError("Please enter valid E-mail");
            signup_email.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            signup_pass.setError("Password can't be blank");
            signup_pass.requestFocus();
            return;
        }
        else if(password.length()<6)
        {
            signup_pass.setError("Minimum password length is 6");
            signup_pass.requestFocus();
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mProgressBar.setVisibility(View.GONE);
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SignupActivity.this,"Successfully Registered",Toast.LENGTH_LONG).show();
                            FirebaseUser curUser = mAuth.getCurrentUser();
                            curUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(SignupActivity.this, "Check your e-mail for verification", Toast.LENGTH_SHORT).show();
                                }
                            });
                            mAuth.signOut();
                            Intent intent= new Intent(SignupActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {

                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            {
                                signup_email.setError("Already registered");
                            }
                            else
                            {
                                Toast.makeText(SignupActivity.this,"Error while registering",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
}
