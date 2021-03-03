package com.example.receiptas.ui.scan_receipt.drawview;

import android.graphics.Bitmap;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class ImagePoint {

    Bitmap bitmap;
    Context mContext;
    Point point;
    int id;
    static int count = 0;

    public ImagePoint(Context context, int resourceId, Point point) {
        this.id = count++;
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        this.mContext = context;
        this.point = point;
    }

    public int getWidth() {
        return bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return point.x;
    }

    public int getY() {
        return point.y;
    }

    public int getID() {
        return id;
    }

    public void setX(int x) {
        point.x = x;
    }

    public void setY(int y) {
        point.y = y;
    }
}
