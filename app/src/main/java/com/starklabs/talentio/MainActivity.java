package com.starklabs.talentio;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    Toolbar mToolbar;
    AppCompatActivity myActivity;
    LinearLayout mainLayout;
    CardView displayBar;
    MaterialButton rateBtn;
    Spinner citySpinner, instrSpinner;
    ArrayList<String> cityList;
    ArrayList<String> instrList;
    ArrayAdapter<String> cityAdapter;
    ArrayAdapter<String> instrAdapter;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    TextView id_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayBar=findViewById(R.id.display_bar);
        myActivity=MainActivity.this;
        mDrawerLayout=findViewById(R.id.drawer_layout);
        mToolbar=findViewById(R.id.toolbar);
        mainLayout=findViewById(R.id.main_layout);
        rateBtn = findViewById(R.id.button_rate);
        citySpinner=findViewById(R.id.spinner_city);
        instrSpinner=findViewById(R.id.spinner_instr);
        id_tv=findViewById(R.id.main_id_tv);


        cityList= new ArrayList<>();
        instrList=new ArrayList<>();

        cityList.add("City");
        instrList.add("Instructor");

        cityAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,cityList);
        instrAdapter= new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,instrList);

        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        instrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        citySpinner.setAdapter(cityAdapter);
        instrSpinner.setAdapter(instrAdapter);

        mToggle= new ActionBarDrawerToggle(myActivity,mDrawerLayout,mToolbar,R.string.open_drawer,R.string.close_drawer);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(displayBar.isShown())
                {
                    Animation fadeOut = new AlphaAnimation(1, 0);
                    fadeOut.setInterpolator(new AccelerateInterpolator());
                    fadeOut.setDuration(400);
                    fadeOut.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            displayBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    displayBar.startAnimation(fadeOut);
                }
                else
                {
                    Animation fadeIn = new AlphaAnimation(0, 1);
                    fadeIn.setInterpolator(new DecelerateInterpolator());
                    fadeIn.setDuration(400);
                    fadeIn.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            displayBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    displayBar.startAnimation(fadeIn);
                }
            }
        });

        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseUser=mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser!=null)
        {
            id_tv.append("   ");
            id_tv.append(mFirebaseUser.getEmail());
        }
    }
}
