package com.starklabs.talentio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    String color1="#f44336";
    String color2="#ff5722";
    String color3="#ffcdd2";
    LinearLayout mLinearLayout;
    TextInputEditText user,pass;
    MaterialButton bt_login;
    TextView tv_signup;
    ProgressBar mProgressBar;

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth=FirebaseAuth.getInstance();

        mProgressBar=findViewById(R.id.login_progress_bar);
        mLinearLayout=findViewById(R.id.loginTopLayout);
        user=findViewById(R.id.username);
        pass=findViewById(R.id.password);
        bt_login=findViewById(R.id.button_login);
        tv_signup=findViewById(R.id.tv_signup);

        GradientDrawable mGradientDrawable=new GradientDrawable(GradientDrawable.Orientation.TR_BL,
                new int[]{Color.parseColor(color1),Color.parseColor(color2),Color.parseColor(color3)});
        mLinearLayout.setBackgroundDrawable(mGradientDrawable);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userlogin();
            }
        });

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void userlogin() {
        String email=user.getText().toString().trim();
        String password=pass.getText().toString().trim();
        if(email.isEmpty())
        {
            user.setError("E-mail is required");
            user.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            user.setError("Please enter valid E-mail");
            user.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            pass.setError("Password can't be blank");
            pass.requestFocus();
            return;
        }
        else if(password.length()<6)
        {
            pass.setError("Minimum password length is 6");
            pass.requestFocus();
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressBar.setVisibility(View.GONE);
                if(task.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this,"Successful",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
