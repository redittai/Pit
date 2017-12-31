package com.redittai.pit.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.redittai.pit.Models.pitPoint;

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
   Paint black, red , blue , yellow ;
    int currentPitInArray = -1;



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
        sortArray(pitPoints);
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

    }

    private void drawPits() {
      
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

                       invalidate(getAndInvalidateOnlyChangedData());
                   }
               }
                break;
            /*
              release dragPoint object and call onDraw()
             */
            case MotionEvent.ACTION_UP:
                draggingPoint = null;
                currentPitInArray = -1;
                invalidate();

                break;
        }

        return true;
    }

    int step = 40;
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
                currentPitInArray = i;
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
        Random r = new Random();

        for (int i = 0; i < 5; i++) {
            int x = r.nextInt(pointInitStep);
            int y = r.nextInt( pointInitStep);

            pitPoint p1 = new pitPoint(getContext(),x, y);
            this.pitPoints.add(p1);
        }
        sortArray(pitPoints);
    }
    boolean IS_FIRST_INIT = true;

    /**
     * check screen size and initial 5 Random pitPoints on all screen.
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("a1", "width: "+MeasureSpec.getSize(widthMeasureSpec) + " Height: "+MeasureSpec.getSize(heightMeasureSpec));

            this.screenWidth = MeasureSpec.getSize(widthMeasureSpec);
            this.screenHeight = MeasureSpec.getSize(heightMeasureSpec);
            this.pointInitStep = MeasureSpec.getSize(widthMeasureSpec);
            if (IS_FIRST_INIT) {
                initFirstPitPoints();
                IS_FIRST_INIT = false;
            }
    }


    /**
     * For memory optimizations, Avoid sorting Whole array each change. Switch only two objects when needed.
     * @param smaller smallest x value of pitPoint
     * @param larger Largest x value of pitPoint
     */
    private void switchItemsInArray(int smaller , int larger ){
        pitPoint temp;
        temp = pitPoints.get(smaller);
        pitPoints.set(smaller,pitPoints.get(larger));
        pitPoints.set(larger,temp);

    }

    /**
     *  Iterate threw nearest pitPoints in pitPoints Array,, compare them and switch if needed.
     *  return X values to determine only the changed rect.
     * @return Rect of changed data. Avoid drawing whole canvas in every Motion event.
     */
    private Rect getAndInvalidateOnlyChangedData() {


        float currentXValue = pitPoints.get(currentPitInArray).getxPoint();

        float previousXvalue = 0;

        if (currentPitInArray > 0) {

            previousXvalue = pitPoints.get(currentPitInArray-1).getxPoint();
        }

        float nextXvalue = 0;

        if (currentPitInArray < pitPoints.size()-1) {
            nextXvalue = pitPoints.get(currentPitInArray+1).getxPoint();
        }

        if (currentPitInArray == 0 ){

            if (currentXValue > nextXvalue) {
                switchItemsInArray(currentPitInArray + 1, currentPitInArray);
                currentPitInArray = currentPitInArray + 1;
                return new Rect((int)currentXValue , 0 , (int)nextXvalue , (int)screenHeight);

            }

        }
        else if (currentPitInArray == pitPoints.size()-1){

            if (currentXValue < previousXvalue){

                switchItemsInArray(currentPitInArray,currentPitInArray-1);
                currentPitInArray = currentPitInArray - 1;

            }
                return new Rect((int)previousXvalue , 0 , (int)currentXValue , (int)screenHeight);
        }

        else {
            if (currentXValue < previousXvalue){
               switchItemsInArray(currentPitInArray,currentPitInArray-1);
               currentPitInArray = currentPitInArray - 1;
            }

            if (nextXvalue > 0) {
                if (currentXValue > nextXvalue) {

                    switchItemsInArray(currentPitInArray + 1, currentPitInArray);
                     currentPitInArray = currentPitInArray + 1;
                }
            }
        }
        // Memory optimizations, draw only changed part in canvas.
        return new Rect((int)previousXvalue , 0 , (int)nextXvalue , (int)screenHeight);
    }
}




