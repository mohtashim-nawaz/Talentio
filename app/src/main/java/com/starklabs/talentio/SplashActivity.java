package com.starklabs.talentio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    String color1="#3949ab";
    String color2="#1976d2";
    String color3="#4fc3f7";
    LinearLayout mLinearLayout;
    Handler mHandler;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mFirebaseAuth=FirebaseAuth.getInstance();
        mLinearLayout=findViewById(R.id.splashTopLayout);
        mHandler=new Handler();

        GradientDrawable mGradientDrawable=new GradientDrawable(GradientDrawable.Orientation.TR_BL,
                new int[]{Color.parseColor(color1),Color.parseColor(color2),Color.parseColor(color3)});
        mLinearLayout.setBackgroundDrawable(mGradientDrawable);


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFirebaseUser=mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser!=null && mFirebaseUser.isEmailVerified())
                {
                    Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                    intent.putExtra("verified",1);
                    startActivity(intent);
                    finish();
                }
                else if(mFirebaseUser!=null && !mFirebaseUser.isEmailVerified())
                {
                    Intent intent= new Intent(SplashActivity.this,LoginActivity.class);
                    intent.putExtra("verified",0);
                    startActivity(intent);
                    finish();
                }
                else if(mFirebaseUser==null)
                {
                    Intent intent= new Intent(SplashActivity.this,LoginActivity.class);
                    intent.putExtra("verified",99);
                    startActivity(intent);
                    finish();
                }

            }
        },1500);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
