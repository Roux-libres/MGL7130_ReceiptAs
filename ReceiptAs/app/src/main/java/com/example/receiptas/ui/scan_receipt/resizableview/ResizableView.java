package com.example.receiptas.ui.scan_receipt.resizableview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.receiptas.R;

import java.util.ArrayList;

public class ResizableView extends View {

    private ArrayList<ImagePoint> imagePoints;
    private Paint paint;
    private Canvas canvas;
    private int pointId;
    private int horizontalDifference, verticalDifference;

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

    private void initializeView(Context context) {
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setFocusable(true);
        this.canvas = new Canvas();
        this.imagePoints = new ArrayList<ImagePoint>();
    }

    public void compareSize(Context context, ImageView imageView, Bitmap imageBitmap) {
        this.horizontalDifference = (imageView.getWidth() - imageBitmap.getWidth()) / 2;
        this.verticalDifference = (imageView.getHeight() - imageBitmap.getHeight()) / 2;

        int paddingX = 100;
        int paddingY = 100;

        int minX = imageView.getWidth() / 2 - paddingX;
        int maxX = imageView.getWidth() / 2 + paddingX;
        int minY = imageView.getHeight() / 2 - paddingY;
        int maxY = imageView.getHeight() / 2 + paddingY;

        //Upper left
        ImagePoint point0 = new ImagePoint(context, R.drawable.circle, new Point());
        point0.setX(minX);
        point0.setY(minY);
        this.imagePoints.add(point0);

        //Upper right
        ImagePoint point1 = new ImagePoint(context, R.drawable.circle, new Point());
        point1.setX(maxX);
        point1.setY(minY);
        this.imagePoints.add(point1);

        //Bottom left
        ImagePoint point2 = new ImagePoint(context, R.drawable.circle, new Point());
        point2.setX(minX);
        point2.setY(maxY);
        this.imagePoints.add(point2);

        //Bottom right
        ImagePoint point3 = new ImagePoint(context, R.drawable.circle, new Point());
        point3.setX(maxX);
        point3.setY(maxY);
        this.imagePoints.add(point3);

        ImagePoint.resetCount();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.paint.setAntiAlias(true);
        this.paint.setDither(true);
        this.paint.setColor(getResources().getColor(R.color.teal_200));
        this.paint.setXfermode(null);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(10);

        Path path = new Path();
        path.moveTo(this.imagePoints.get(0).getX(), this.imagePoints.get(0).getY());
        path.lineTo(this.imagePoints.get(1).getX(), this.imagePoints.get(1).getY());
        path.lineTo(this.imagePoints.get(3).getX(), this.imagePoints.get(3).getY());
        path.lineTo(this.imagePoints.get(2).getX(), this.imagePoints.get(2).getY());
        path.lineTo(this.imagePoints.get(0).getX(), this.imagePoints.get(0).getY());
        canvas.drawPath(path, this.paint);

        for (ImagePoint point : this.imagePoints) {
            point.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();

        int x = (int) event.getX();
        int y = (int) event.getY();

        if (x <= this.horizontalDifference) {
            x = this.horizontalDifference;
        } else if (x >= this.getWidth() - this.horizontalDifference) {
            x = this.getWidth() - this.horizontalDifference;
        }

        if (y <= this.verticalDifference) {
            y = this.verticalDifference;
        } else if (y >= this.getHeight() - this.verticalDifference) {
            y = this.getHeight() - this.verticalDifference;
        }

        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                this.pointId = -1;
                for (ImagePoint point : this.imagePoints) {
                    int centerX = point.getX();
                    int centerY = point.getY();
                    double radCircle = Math.sqrt((double) (((centerX - x) * (centerX - x)) + (centerY - y) * (centerY - y)));

                    if (radCircle < point.getWidth()) {
                        this.pointId = point.getId();
                        point.draw(this.canvas);
                        invalidate();
                        break;
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (this.pointId == -1) {
                    break;
                }

                imagePoints.get(this.pointId).setX(x);
                imagePoints.get(this.pointId).setY(y);

                switch (this.pointId) {
                    case 0:
                        imagePoints.get(2).setX(x);
                        imagePoints.get(1).setY(y);
                        break;
                    case 1:
                        imagePoints.get(3).setX(x);
                        imagePoints.get(0).setY(y);
                        break;
                    case 2:
                        imagePoints.get(0).setX(x);
                        imagePoints.get(3).setY(y);
                        break;
                    case 3:
                        imagePoints.get(1).setX(x);
                        imagePoints.get(2).setY(y);
                        break;
                }

                invalidate();
                break;
        }
        invalidate();
        return true;
    }

    public Point[] getPoints() {
        Point[] points = {
                this.imagePoints.get(0).getPoint(),
                this.imagePoints.get(1).getPoint(),
                this.imagePoints.get(2).getPoint(),
                this.imagePoints.get(3).getPoint()
        };

        for (Point point : points) {
            point.x = point.x - this.horizontalDifference;
            point.y = point.y - this.verticalDifference;
        }

        return points;
    }
}
