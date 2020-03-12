package com.starklabs.talentio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

public class SplashActivity extends AppCompatActivity {

    String color1="#f44336";
    String color2="#ff5722";
    String color3="#ffcdd2";
    LinearLayout mLinearLayout;
    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        mLinearLayout=findViewById(R.id.splashTopLayout);
        mHandler=new Handler();

        GradientDrawable mGradientDrawable=new GradientDrawable(GradientDrawable.Orientation.TR_BL,
                new int[]{Color.parseColor(color1),Color.parseColor(color2),Color.parseColor(color3)});
        mLinearLayout.setBackgroundDrawable(mGradientDrawable);


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },1500);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
