package com.starklabs.talentio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPass extends AppCompatActivity {

    TextInputEditText email;
    MaterialButton button;
    String color1="#3949ab";
    String color2="#1976d2";
    String color3="#4fc3f7";
    LinearLayout mainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        email=findViewById(R.id.forgot_pass);
        button=findViewById(R.id.forgot_pass_button);

        mainLayout=findViewById(R.id.forgot_main_layout);

        GradientDrawable mGradientDrawable=new GradientDrawable(GradientDrawable.Orientation.TR_BL,
                new int[]{Color.parseColor(color1),Color.parseColor(color2),Color.parseColor(color3)});
        mainLayout.setBackgroundDrawable(mGradientDrawable);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=email.getText().toString().trim();

                if(mail.isEmpty())
                {
                    email.setError("E-mail is required");
                    email.requestFocus();
                    return;
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches())
                {
                    email.setError("Please enter valid E-mail");
                    email.requestFocus();
                    return;
                }

                FirebaseAuth.getInstance().sendPasswordResetEmail(mail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(ForgotPass.this, "Reset link sent to e-mail!", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(ForgotPass.this, "Error occurred!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
