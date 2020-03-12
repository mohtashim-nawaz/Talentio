package com.starklabs.talentio;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    String color1="#f44336";
    String color2="#ff5722";
    String color3="#ffcdd2";
    LinearLayout mLinearLayout;
    TextInputEditText user,pass;
    MaterialButton login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLinearLayout=findViewById(R.id.loginTopLayout);
        user=findViewById(R.id.username);
        pass=findViewById(R.id.password);
        login=findViewById(R.id.button_login);


        GradientDrawable mGradientDrawable=new GradientDrawable(GradientDrawable.Orientation.TR_BL,
                new int[]{Color.parseColor(color1),Color.parseColor(color2),Color.parseColor(color3)});
        mLinearLayout.setBackgroundDrawable(mGradientDrawable);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
