package com.redittai.pit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.redittai.pit.Views.mCanvas;

public class MainActivity extends AppCompatActivity {
    Button addPit;
    View pit;

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

}
