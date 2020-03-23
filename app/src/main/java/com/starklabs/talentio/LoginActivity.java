package com.starklabs.talentio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    String color1="#3949ab";
    String color2="#1976d2";
    String color3="#4fc3f7";
    LinearLayout mLinearLayout;
    TextInputEditText user,pass;
    MaterialButton bt_login;
    TextView tv_signup,tv_password;
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
        tv_password=findViewById(R.id.tv_password);

        GradientDrawable mGradientDrawable=new GradientDrawable(GradientDrawable.Orientation.TR_BL,
                new int[]{Color.parseColor(color1),Color.parseColor(color2),Color.parseColor(color3)});
        mLinearLayout.setBackgroundDrawable(mGradientDrawable);

        if(getIntent().getIntExtra("verified",-1)==0)
        {
            Toast.makeText(LoginActivity.this, "Please check your email for verification then Login again", Toast.LENGTH_SHORT).show();
        }

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

        tv_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgotPass.class);
                startActivity(intent);
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
                    final FirebaseUser curUser=mFirebaseAuth.getCurrentUser();
                    if(curUser==null)
                    {
                        Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(!curUser.isEmailVerified())
                    {
                         AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                         builder.setCancelable(false)
                                 .setMessage("E-mail is not verified. Please verify and Login again\nResend verification e-mail?")
                                 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                         curUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(LoginActivity.this, "Sent successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                    }
                                             }
                                         });
                                         mFirebaseAuth.signOut();
                                        dialog.dismiss();
                                     }
                                 })
                                    .setNeutralButton("DISMISS", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mFirebaseAuth.signOut();
                                            dialog.dismiss();
                                        }
                                    });
                         AlertDialog alertDialog = builder.create();
                         alertDialog.show();
                    }
                    else if(curUser!=null && curUser.isEmailVerified())
                        {
                        Toast.makeText(LoginActivity.this,"Successful",Toast.LENGTH_LONG).show();
                        Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
                else
                {
                    Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
