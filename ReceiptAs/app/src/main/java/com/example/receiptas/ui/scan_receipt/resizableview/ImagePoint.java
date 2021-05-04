package com.example.receiptas.ui.scan_receipt.resizableview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

public class ImagePoint {

    static int count = 0;
    private Drawable drawable;
    private Point point;
    private int id;

    public ImagePoint(Context context, int resourceId, Point point) {
        this.id = count++;
        this.drawable = context.getDrawable(resourceId);
        this.point = point;
    }

    public static void resetCount() {
        count = 0;
    }

    public int getWidth() {
        return this.drawable.getIntrinsicWidth();
    }

    public int getHeight() {
        return this.drawable.getIntrinsicHeight();
    }

    public void draw(Canvas canvas) {
        this.drawable.setBounds(
                this.getX() - this.getWidth() / 2,
                this.getY() - this.getHeight() / 2,
                this.getX() + this.getWidth() / 2,
                this.getY() + this.getHeight() / 2
        );
        this.drawable.draw(canvas);
    }

    public int getX() {
        return this.point.x;
    }

    public void setX(int x) {
        this.point.x = x;
    }

    public int getY() {
        return this.point.y;
    }

    public void setY(int y) {
        this.point.y = y;
    }

    public int getId() {
        return this.id;
    }

    public Point getPoint() {
        return this.point;
    }
}