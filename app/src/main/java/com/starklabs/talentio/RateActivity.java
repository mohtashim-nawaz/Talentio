package com.starklabs.talentio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;

import com.google.android.material.button.MaterialButton;

public class RateActivity extends AppCompatActivity
{


    RatingBar bar1,bar2,bar3,bar4,bar5;
    MaterialButton submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        submit = findViewById(R.id.rating_submit_button);
        bar1 = findViewById(R.id.rating_bar_1);
        bar2 = findViewById(R.id.rating_bar_2);
        bar3 = findViewById(R.id.rating_bar_3);
        bar4 = findViewById(R.id.rating_bar_4);
        bar5 = findViewById(R.id.rating_bar_5);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


}
