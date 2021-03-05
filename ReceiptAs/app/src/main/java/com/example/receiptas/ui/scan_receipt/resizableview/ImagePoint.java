package com.example.receiptas.ui.scan_receipt.resizableview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

public class ImagePoint {

    private Drawable drawable;
    private Point point;
    private int id;
    static int count = 0;

    public ImagePoint(Context context, int resourceId, Point point){
        this.id = count ++;
        this.drawable = context.getDrawable(resourceId);
        this.point = point;
    }

    public int getWidth() {
        return this.drawable.getIntrinsicWidth();
    }

    public int getHeight() {
        return this.drawable.getIntrinsicHeight();
    }

    public void draw(Canvas canvas) {
        this.drawable.setBounds(
                this.getX(),
                this.getY(),
                this.getX() + this.getWidth(),
                this.getY() + this.getHeight()
        );
        this.drawable.draw(canvas);
    }

    public int getX() {
        return this.point.x;
    }

    public int getY() {
        return this.point.y;
    }

    public int getId() {
        return this.id;
    }

    public void setX(int x) {
        this.point.x = x;
    }

    public void setY(int y) {
        this.point.y = y;
    }

    public Point getPoint(){
        return this.point;
    }
}