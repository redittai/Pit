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
import java.util.Random;

/**
 * Created by Ittai Oren on 27/12/2017.
 */

public class mCanvas extends View {

    Canvas myCanvas;
    String tag = "mCanvas";
    float screenHeight;
    float screenWidth;
    public ArrayList<pitPoint> pitPoints = new ArrayList<>();
    Paint black, red , blue , yellow , purple;


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




    /**
     * init all Paint objects.
     * red for pitPoints.
     * blue for path.
     * black for grid.
     */
    private void init(@Nullable AttributeSet attrs){
        //black for graph grid
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

        // for debug purpose.
        yellow = new Paint();
        yellow.setColor(Color.YELLOW);
        yellow.setStyle(Paint.Style.FILL);

        // for debug purpose.
        purple = new Paint();
        purple.setColor(Color.MAGENTA);
        purple.setStyle(Paint.Style.FILL);

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

    /**
     *  calculates and draws the graph on the Canvas.
     */
    public void initGraph(){

        myCanvas.drawColor(Color.LTGRAY );
        //Draw vertical line
        myCanvas.drawLine(screenWidth/2 , 0 ,screenWidth / 2 , screenHeight  ,black);
        //Draw horizontal line
        myCanvas.drawLine(0,screenHeight / 2 , screenWidth , screenHeight / 2 , black);

    }

    /**
     * createNewPitPoint() is called on the button click in MainActivity.class.
     * Adds a new point in the origin axis (0, 0) coordinate.
     */
    public void createNewPitPoint(){
        pitPoints.add(new pitPoint(getContext(),screenWidth/2, screenHeight/2));
        invalidate();
    }

    /**
     * Iterate threw array and create a path to connect the pitPoints with the blue line.
     */
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

    pitPoint draggingPoint = null;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:

                break;
            /*
              check if found a pitPoint.
              if draggingPoint != null set points x & y values according to current event position value.
             */
            case MotionEvent.ACTION_MOVE:
                if (draggingPoint == null) {
                    draggingPoint = pickupPitPointFromGraph(event.getX(), event.getY());
                }

                if (draggingPoint != null){

                    if (event.getX() > 5 && event.getX() < screenWidth - 5 && event.getY() > 5 && event.getY() < screenHeight - 5) {
                        draggingPoint.setxPoint(event.getX());
                        draggingPoint.setyPoint(event.getY());


                        invalidate();
                    }
                }
                break;
            /*
              release dragPoint object and call onDraw()
             */
            case MotionEvent.ACTION_UP:
                draggingPoint = null;
                invalidate();

                break;
        }

        return true;
    }

    int step = 40; //

    /**
     *     check if the a pit point is "picked up" and return the specific point in the pitPoints array

     * @param x = MotionEvent.getX point
     * @param y  = MotionEvent.getY point
     * @return returns the pitPoint object from the pitPoints Array so on MotionEvent.ACTION_MOVE it's values would
     * be set to current position.
     */

    @Nullable
    private pitPoint pickupPitPointFromGraph(float x, float y) {

        for (int i = 0 ; i < pitPoints.size() ; i++){
            if (pitPoints.get(i).getxPoint() > x - step && pitPoints.get(i).getxPoint() < x + step && pitPoints.get(i).getyPoint()
                    > y-step && pitPoints.get(i).getyPoint() < y+step){

                return pitPoints.get(i);
            }
        }
        return null;
    }

    /**
     * Java Class - sorts the array by the value of X.
     * @param points pitPoint array.
     */
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
    int pointInitStep = 600;



    /**
     * init first 5 pitPoints. Try to calculate various screen sizes  calculateScreenRatioForRandomXPoints and
     calculateScreenRatioForRandomYPoints functions. If called before initial of pit object returns the random number that is generated
     in the function ( pointInitStep )
     */
    public void initFirstPitPoints() {


        for (int i = 0; i < 5; i++) {
            Random r = new Random();
            int x = r.nextInt(pointInitStep);
            int y = r.nextInt( pointInitStep);

            pitPoint p1 = new pitPoint(getContext(),calculateScreenRatioForRandomXPoints(x), calculateScreenRatioForRandomYPoints(y)) ;


            this.pitPoints.add(p1);

        }

    }

    private int calculateScreenRatioForRandomXPoints(int x){
        int pointX;
        if (this.myCanvas != null) {
            pointX = (x/pointInitStep) *  this.myCanvas.getWidth() ;
            return pointX;
        }

        return x;
    }
    private int calculateScreenRatioForRandomYPoints(int y){
        int pointY;

        if (this.myCanvas != null) {
            pointY = (y/pointInitStep) *  this.myCanvas.getHeight() ;
            return pointY;
        }

        return y;
    }

}




