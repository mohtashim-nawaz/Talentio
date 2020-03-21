package com.starklabs.talentio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RateActivity extends AppCompatActivity
{


    RatingBar bar1,bar2,bar3,bar4,bar5;
    MaterialButton submit;
    String cityId;
    String instrId;
    String instrName;
    double v1,v2,v3,v4,v5;
    double num;
    ProgressDialog localProgress;
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

        localProgress=new ProgressDialog(RateActivity.this);
        localProgress.setCancelable(false);
        localProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        localProgress.setMessage("Submitting...");

        cityId=getIntent().getStringExtra("cityId");
        instrId=getIntent().getStringExtra("instrId");
        instrName=getIntent().getStringExtra("instrName");

        Log.d("instr:",cityId+":"+instrId);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double b1,b2,b3,b4,b5;
                b1=bar1.getRating();
                b2=bar2.getRating();
                b3=bar3.getRating();
                b4=bar4.getRating();
                b5=bar5.getRating();

                localProgress.show();
                saveRating(b1,b2,b3,b4,b5);
            }
        });

    }

    private void saveRating(final double b1, final double b2, final double b3, final double b4, final double b5) {
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        db.collection("cities").document(cityId).collection("rating")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult()!=null)
                        {
                            for(QueryDocumentSnapshot document: task.getResult())
                            {
                                if(document.getId().equals(instrId))
                                {
                                    try{

                                        v1= ((Number)document.get("p1")).doubleValue();

                                        v2= ((Number) document.get("p2")).doubleValue();

                                        v3= ((Number) document.get("p3")).doubleValue();

                                        v4= ((Number) document.get("p4")).doubleValue();

                                        v5= ((Number) document.get("p5")).doubleValue();

                                        num= ((Number) document.get("total")).doubleValue();


                                        double temp1=((v1*num)+(b1))/(num+1);
                                        double temp2=((v2*num)+(b2))/(num+1);
                                        double temp3=((v3*num)+(b3))/(num+1);
                                        double temp4=((v4*num)+(b4))/(num+1);
                                        double temp5=((v5*num)+(b5))/(num+1);
                                        double numTemp= num+1.0;
                                        //Log.d("mydata:",temp1+":"+temp2+":"+temp3+":"+temp4+":"+temp5+":"+numTemp);

                                        saveData(numTemp, temp1, temp2, temp3, temp4, temp5);
                                    }catch(Exception e)
                                    {
                                        Toast.makeText(RateActivity.this, "Something went wrong\nContact Admin", Toast.LENGTH_LONG).show();
                                        Log.d("mydata:",e.getMessage());
                                        localProgress.cancel();
                                    }
                                }
                            }
                        }
                    }
                });
    }

    private void saveData(double numTemp, double temp1, double temp2, double temp3, double temp4, double temp5) {

        FirebaseFirestore db= FirebaseFirestore.getInstance();
        db.collection("cities").document(cityId).collection("rating")
                .document(instrId)
                .update("name",instrName,"total",numTemp,"p1",temp1,"p2",temp2,"p3",temp3,"p4",temp4,"p5",temp5)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(RateActivity.this, "Rated Successfully", Toast.LENGTH_SHORT).show();
                            localProgress.cancel();
                            finish();
                        }
                        else
                        {
                            Toast.makeText(RateActivity.this, "Couldn't rate at this moment", Toast.LENGTH_SHORT).show();
                            localProgress.cancel();
                        }
                    }
                });

    }


}
