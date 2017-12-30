package com.redittai.shutterflyproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.redittai.shutterflyproject.Models.pitPoint;
import com.redittai.shutterflyproject.Views.mCanvas;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button addPit;
    View pit;
    public static boolean IS_FIRST_ENTER = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.pit = findViewById(R.id.CustomComponent);
        addPit = findViewById(R.id.addPitBTN);

        addPit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((mCanvas)pit).createNewPitPoint();

            }
        });

    }

    /**
     * Check if on resume is called for the first time.
     * If it is create 5 pitPoints.
     * Called from mCanvas.class
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (IS_FIRST_ENTER){
            ((mCanvas)pit). initFirstPitPoints();
            IS_FIRST_ENTER = false;
        }

    }



}
