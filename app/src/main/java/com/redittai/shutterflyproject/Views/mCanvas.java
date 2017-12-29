package com.redittai.shutterflyproject.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.redittai.shutterflyproject.Models.pitPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Ittai Oren on 27/12/2017.
 */

public class mCanvas extends View {

   Canvas myCanvas;
   String tag = "mCanvas";
   float screenHeight;
   float screenWidth;
   public static ArrayList<pitPoint> pitPoints = new ArrayList<>();
   Paint black, red , blue;


    public mCanvas(Context context) {
        super(context);
        init(null);

    }


    public mCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public mCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);

    }


    // init all paints and rects
    private void init(@Nullable AttributeSet attrs){
        //black for grph grid

        black = new Paint();
        black.setColor(Color.GRAY);
        black.setStyle(Paint.Style.STROKE);
        black.setStrokeWidth(5);
        // red paint for pitPoints
        red = new Paint();
        red.setColor(Color.RED);
        red.setStyle(Paint.Style.FILL);

        //blue paint stroke to connect the lines
        blue = new Paint();
        blue.setColor(Color.BLUE);
        blue.setStyle(Paint.Style.STROKE);
        blue.setStrokeWidth(10);




    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.myCanvas = canvas;
       this.screenHeight = canvas.getHeight();
       this.screenWidth = canvas.getWidth();


        initGraph();
        drawPits();
        drawConnectingPath();

    }

    public void initGraph(){

     //draw
     //  myCanvas.drawLine(0,200 , screenWidth , 200 , black);
     //  myCanvas.drawLine(0,screenHeight , screenWidth , screenHeight  , black);
        myCanvas.drawColor(Color.LTGRAY );
        //Draw vertical line
        myCanvas.drawLine(screenWidth/2 , 0 ,screenWidth / 2 , screenHeight  ,black);
        //Draw horizontal line
        myCanvas.drawLine(0,screenHeight / 2 , screenWidth , screenHeight / 2 , black);
       // Rect addButton = new Rect(50,50,150,150);
     //   myCanvas.drawRect(addButton,black);

    }

//    public void createNewPitPoint(){
//
//        pitPoints.add(new pitPoint(getContext(),screenWidth/2, screenHeight/2));
//        myCanvas.drawCircle(screenWidth/2,screenHeight/2,20,red);
//        invalidate();
//    }
    private void drawConnectingPath( ){

        if (pitPoints.size() >= 1) {
            Path path = new Path();

            for (int i = 1; i < pitPoints.size(); i++) {

                    path.moveTo(pitPoints.get(i - 1).getxPoint(), pitPoints.get(i - 1).getyPoint());
                    path.lineTo(pitPoints.get(i).getxPoint(), pitPoints.get(i).getyPoint());


            }
            path.close();
          this.myCanvas.drawPath(path, blue);

        }
        Log.i(tag, pitPoints.size()+"");

    }

    private void drawPits() {
        sortArray(pitPoints);
        // Iterate threw the pitPoint list and draw the points
        for (int i = 0; i < pitPoints.size() ; i++){

            myCanvas.drawCircle(pitPoints.get(i).getxPoint(), pitPoints.get(i).getyPoint(),20,red);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:

                break;

            case MotionEvent.ACTION_MOVE:

                pitPoint newPitPoint = pickupPitPointFromGraph(event.getX(),event.getY());
                if (newPitPoint != null) {
                    for (int i = 0 ; i < pitPoints.size() ; i++){
                        if (newPitPoint == pitPoints.get(i)){
                            // make sure the drag is in the view bounds
                            if (event.getX() > 5 && event.getX() < screenWidth - 5 && event.getY() > 5 && event.getY() < screenHeight - 5){
                                pitPoints.get(i).setxPoint(event.getX());
                                pitPoints.get(i).setyPoint(event.getY());


                            }
                            Log.i(tag, "found it");

                            postInvalidate();
                        }
                    }
                }


                break;

            case MotionEvent.ACTION_UP:
                postInvalidate();

                break;
        }

        return true;
    }

    // check if the a pit point is "picked up" and return the specific point in the pitPoints array
    @Nullable
    private pitPoint pickupPitPointFromGraph(float x, float y) {
        for (int i = 0 ; i < pitPoints.size() ; i++){
            if (pitPoints.get(i).getxPoint() > x - 40 && pitPoints.get(i).getxPoint() < x + 40 && pitPoints.get(i).getyPoint()
                    > y-40 && pitPoints.get(i).getyPoint() < y+40){
                Log.i(tag,"found a pit point");
                return pitPoints.get(i);
            }
        }
        return null;
    }
    // sort pitPoint array by the value of the X position
    public void sortArray(ArrayList<pitPoint> points){
        Collections.sort(points, new Comparator<pitPoint>() {
            @Override
            public int compare(pitPoint o1, pitPoint o2) {
                int point1 = (int)o1.getxPoint();
                int point2 = (int)o2.getxPoint();

                return    Integer.valueOf(point1).compareTo(point2);         }
        });
    }

}




