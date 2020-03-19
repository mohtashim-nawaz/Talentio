package com.starklabs.talentio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    FirebaseFirestore db;
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

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(id!=0)
                {
                    try {
                        Log.d("selected:",(String)parent.getItemAtPosition(position));
                        getInstructor((String)parent.getItemAtPosition(position));
                    }catch(Exception e)
                    {
                        Toast.makeText(MainActivity.this,"Error loading instructors",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @Override
    protected void onResume() {
        try {
            getCities();
        }
        catch (Exception e){
            //Log.d("GETINFO",e.getMessage());
            Toast.makeText(MainActivity.this,"Error loading cities",Toast.LENGTH_SHORT).show();
        }

        super.onResume();
    }

    private boolean getInstructor(String city)
    {
        ProgressDialog mProgressDialog;
        mProgressDialog= new ProgressDialog(MainActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Loading Cities...");
        mProgressDialog.show();

        instrList=new ArrayList<>();
        instrList.add("Instructor");
        db=FirebaseFirestore.getInstance();
        db.collection("cities").document(city).collection("rating")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult()!=null) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("GETINFO", document.getId() + " => " + document.getData());
                                instrList.add((String)document.get("name"));
                            }
                        } else {
                            Log.w("GETINFO", "Error getting documents.", task.getException());
                            Toast.makeText(MainActivity.this, "No cities found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        instrAdapter= new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_spinner_item,instrList);
        instrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        instrSpinner.setAdapter(instrAdapter);

        mProgressDialog.cancel();
        return true;
    }

    private boolean getCities()
    {
        ProgressDialog mProgressDialog;
        mProgressDialog= new ProgressDialog(MainActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Loading Cities...");
        mProgressDialog.show();

        cityList= new ArrayList<>();
        cityList.add("City");

        db=FirebaseFirestore.getInstance();
        db.collection("cities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult()!=null) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("GETINFO", document.getId() + " => " + document.getData());
                                try {
                                    Log.d("GETINFO","I am here");
                                    cityList.add((String)document.get("name"));
                                }
                                catch (Exception e)
                                {
                                }
                            }
                        } else {
                            Log.w("GETINFO", "Error getting documents.", task.getException());
                            Toast.makeText(MainActivity.this, "No cities found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        cityAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item,cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        mProgressDialog.cancel();
        return true;
    }

}
