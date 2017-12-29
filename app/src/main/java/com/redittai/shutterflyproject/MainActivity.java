package com.redittai.shutterflyproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
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

        pit = findViewById(R.id.CustomComponent);
        addPit = findViewById(R.id.addPitBTN);
        pit.getWidth();
        pit.getHeight();
        if (IS_FIRST_ENTER){
            initFirstPitPoints();
            IS_FIRST_ENTER = false;
        }
        addPit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addaNewPitPoint();
            }
        });

    }
    // used to determine the position of the initialized pitPoint
    private float getScreenWidth(){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int widthInPixel = Math.round(dm.widthPixels);
        return widthInPixel;
    }
    // add a new pit point to the view group
    private void addaNewPitPoint(){
        pitPoint p = new pitPoint(getApplicationContext(),pit.getWidth()/2 , pit.getHeight() / 2);
        com.redittai.shutterflyproject.Views.mCanvas.pitPoints.add(p);
        pit.invalidate();
    }

    private void initFirstPitPoints() {
        int step = 100;
        for (int i = 0; i < 5; i++) {
            Random r = new Random();
            int x = r.nextInt(1000);
            int y = r.nextInt(1080);

            pitPoint p1 = new pitPoint(getApplicationContext(),x, y);


            mCanvas.pitPoints.add(p1);

        }

    }
}
