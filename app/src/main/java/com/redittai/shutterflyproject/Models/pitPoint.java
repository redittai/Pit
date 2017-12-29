package com.redittai.shutterflyproject.Models;

import android.content.Context;
import android.view.View;

/**
 * Created by redittai on 27/12/2017.
 */

public class pitPoint extends View{
    private float xPoint;
    private float yPoint;

    public pitPoint(Context context, float xPoint, float yPoint) {
        super(context);
        this.xPoint = xPoint;
        this.yPoint = yPoint;
    }


    public float getxPoint() {
        return xPoint;
    }

    public void setxPoint(float xPoint) {
        this.xPoint = xPoint;
    }

    public float getyPoint() {
        return yPoint;
    }

    public void setyPoint(float yPoint) {
        this.yPoint = yPoint;
    }
}
