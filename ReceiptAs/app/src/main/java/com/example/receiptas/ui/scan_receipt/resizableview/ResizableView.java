package com.example.receiptas.ui.scan_receipt.resizableview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.receiptas.R;

import java.util.ArrayList;

public class ResizableView extends View {

    ArrayList<ImagePoint> imagePoints;
    Paint paint;
    Canvas canvas;
    int pointId;

    public ResizableView(Context context) {
        super(context);
        this.initializeView(context);
    }

    public ResizableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initializeView(context);
    }

    public ResizableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initializeView(context);
    }

    private void initializeView(Context context){
        this.paint = new Paint();
        setFocusable(true);
        this.canvas = new Canvas();
        this.imagePoints = new ArrayList<ImagePoint>();

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        int widthDivision = metrics.widthPixels / 6;
        int heightDivision = metrics.heightPixels / 6;

        //Upper left
        ImagePoint point0 = new ImagePoint(context, R.drawable.circle, new Point());
        point0.setX(widthDivision);
        point0.setY(heightDivision);
        this.imagePoints.add(point0);

        //Upper right
        ImagePoint point1 = new ImagePoint(context, R.drawable.circle, new Point());
        point1.setX(metrics.widthPixels - widthDivision);
        point1.setY(heightDivision);
        this.imagePoints.add(point1);

        //Bottom left
        ImagePoint point2 = new ImagePoint(context, R.drawable.circle, new Point());
        point2.setX(widthDivision);
        point2.setY(metrics.heightPixels - heightDivision);
        this.imagePoints.add(point2);

        //Bottom right
        ImagePoint point3 = new ImagePoint(context, R.drawable.circle, new Point());
        point3.setX(metrics.widthPixels - widthDivision);
        point3.setY(metrics.heightPixels - heightDivision);
        this.imagePoints.add(point3);
    }

    @Override
    protected void onDraw(Canvas canvas){
        this.paint.setAntiAlias(true);
        this.paint.setDither(true);
        this.paint.setColor(Color.parseColor("#55000000"));
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setStrokeJoin(Paint.Join.ROUND);
        this.paint.setStrokeWidth(5);

        canvas.drawPaint(this.paint);
        this.paint.setColor(Color.parseColor("#55FFFFFF"));

        Path path = new Path();
        path.moveTo(this.imagePoints.get(0).getX(), this.imagePoints.get(0).getY());
        path.lineTo(this.imagePoints.get(1).getX(), this.imagePoints.get(1).getY());
        path.lineTo(this.imagePoints.get(3).getX(), this.imagePoints.get(3).getY());
        path.lineTo(this.imagePoints.get(2).getX(), this.imagePoints.get(2).getY());
        canvas.drawPath(path, this.paint);

        for(ImagePoint point : this.imagePoints){
            point.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();

        int x = (int) event.getX();
        int y = (int) event.getY();

        System.out.println("Event X : " + x + "\tEvent Y : " + y);

        switch(eventAction){
            case MotionEvent.ACTION_DOWN:
                this.pointId = -1;
                for(ImagePoint point : this.imagePoints){
                    int centerX = point.getX();
                    int centerY = point.getY();
                    double radCircle = Math.sqrt((double)(((centerX - x) * (centerX - x)) + (centerY - y) * (centerY - y)));

                    if(radCircle < point.getWidth()){
                        this.pointId = point.getId();
                        point.draw(this.canvas);
                        invalidate();
                        break;
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(this.pointId > -1){
                    imagePoints.get(this.pointId).setX(x - imagePoints.get(this.pointId).getWidth() / 2);
                    imagePoints.get(this.pointId).setY(y - imagePoints.get(this.pointId).getHeight() / 2);
                    imagePoints.get(this.pointId).draw(this.canvas);
                    System.out.println("X : " + imagePoints.get(this.pointId).getX() + "\tY : " + imagePoints.get(this.pointId).getY());
                    invalidate();
                }
                break;
        }
        invalidate();
        return true;
    }

    public Point[] getPoints(){
        Point[] points = {
                this.imagePoints.get(0).getPoint(),
                this.imagePoints.get(1).getPoint(),
                this.imagePoints.get(2).getPoint(),
                this.imagePoints.get(3).getPoint()
        };

        return points;
    }
}
