package com.starklabs.talentio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPassActivity extends AppCompatActivity {

    TextInputEditText curPass,resetPass,reenterPass;
    MaterialButton button;
    ProgressBar mProgressBar;
    String pass1,pass2,pass3;
    FirebaseUser user;
    String color1="#3949ab";
    String color2="#1976d2";
    String color3="#4fc3f7";
    LinearLayout mainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        curPass=findViewById(R.id.reset_cur_pass);
        resetPass=findViewById(R.id.reset_new_pass);
        reenterPass=findViewById(R.id.reenter_new_pass);
        mProgressBar=findViewById(R.id.reset_progress_bar);
        mainLayout=findViewById(R.id.reset_main_layout);

        GradientDrawable mGradientDrawable=new GradientDrawable(GradientDrawable.Orientation.TR_BL,
                new int[]{Color.parseColor(color1),Color.parseColor(color2),Color.parseColor(color3)});
        mainLayout.setBackgroundDrawable(mGradientDrawable);

        button=findViewById(R.id.reset_pass_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass1=curPass.getText().toString().trim();
                pass2=resetPass.getText().toString().trim();
                pass3=reenterPass.getText().toString().trim();
                if(pass1.isEmpty())
                {
                    curPass.setError("Password can't be blank");
                    curPass.requestFocus();
                    return;
                }
                else if(pass1.length()<6)
                {
                    curPass.setError("Minimum password length is 6");
                    curPass.requestFocus();
                    return;
                }
                if(pass2.isEmpty())
                {
                    resetPass.setError("Password can't be blank");
                    resetPass.requestFocus();
                    return;
                }
                else if(pass2.length()<6)
                {
                    resetPass.setError("Minimum password length is 6");
                    resetPass.requestFocus();
                    return;
                }
                if(pass3.isEmpty())
                {
                    reenterPass.setError("Password can't be blank");
                    reenterPass.requestFocus();
                    return;
                }
                else if(pass3.length()<6)
                {
                    reenterPass.setError("Minimum password length is 6");
                    reenterPass.requestFocus();
                    return;
                }
                if(!pass2.equals(pass3)){
                    reenterPass.setError("New passwords do not match");
                    reenterPass.requestFocus();
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                user = FirebaseAuth.getInstance().getCurrentUser();
                final String email = user.getEmail();
                AuthCredential credential = EmailAuthProvider.getCredential(email,pass1);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            user.updatePassword(pass2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                         if(task.isSuccessful())
                                         {
                                             Toast.makeText(ResetPassActivity.this, "Successfully changed password\nLogin Again", Toast.LENGTH_LONG).show();
                                             FirebaseAuth.getInstance().signOut();
                                             Intent intent= new Intent(ResetPassActivity.this,LoginActivity.class);
                                             intent.putExtra("verified",99);
                                             intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                             startActivity(intent);
                                         }
                                         else
                                         {
                                             Toast.makeText(ResetPassActivity.this, "Couldn't change password at this moment", Toast.LENGTH_LONG).show();
                                         }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(ResetPassActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
