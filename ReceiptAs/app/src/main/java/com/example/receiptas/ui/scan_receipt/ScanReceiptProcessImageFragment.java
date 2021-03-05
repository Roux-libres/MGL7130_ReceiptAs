package com.example.receiptas.ui.scan_receipt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.receiptas.R;
import com.example.receiptas.ui.scan_receipt.resizableview.ResizableView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.Arrays;

public class ScanReceiptProcessImageFragment extends Fragment {

    private FloatingActionButton validation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_scan_receipt_process_image, container, false);

        String image_path = ScanReceiptProcessImageFragmentArgs.fromBundle(getArguments()).getImagePath();
        File imageFile = new File(image_path);

        if(!imageFile.exists()){
            return root;
        }

        final Bitmap[] imageBitmap = {BitmapFactory.decodeFile(imageFile.getAbsolutePath())};
        ImageView image_view = root.findViewById(R.id.image_view);
        ResizableView resizableView = root.findViewById(R.id.resizable_view);

        image_view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                ImageView image_view = getView().findViewById(R.id.image_view);

                float ratioWidth = ((float) image_view.getWidth()) / ((float) imageBitmap[0].getWidth());
                float ratioHeight = ((float) image_view.getHeight()) / ((float) imageBitmap[0].getHeight());
                float ratio = Math.min(ratioWidth, ratioHeight);
                int newWidth = (int) ((int) imageBitmap[0].getWidth() * ratio);
                int newHeight = (int) ((int) imageBitmap[0].getHeight() * ratio);

                imageBitmap[0] = Bitmap.createScaledBitmap(imageBitmap[0], newWidth, newHeight, true);
                image_view.setImageBitmap(imageBitmap[0]);
                resizableView.compareSize(image_view, imageBitmap[0]);
            }
        });

        this.validation = root.findViewById(R.id.fab_validation);
        this.validation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shapeBitmap(imageBitmap[0], resizableView);
            }
        });

        return root;
    }

    private Bitmap shapeBitmap(Bitmap imageBitmap, ResizableView resizableView){
        Point[] points = resizableView.getPoints();
        points = this.swapPoints(points);

        Bitmap imageBitmapCropped = Bitmap.createBitmap(imageBitmap,
                points[0].x,
                points[0].y,
                points[3].x - points[0].x,
                points[3].y - points[0].y);

        return imageBitmapCropped;
    }

    private Point[] swapPoints(Point[] points){
        Point lowestPoint = points[0];

        for(Point point : points){
            if(point.x <= lowestPoint.x && point.y <= lowestPoint.y){
                lowestPoint = point;
            }
        }

        Point tempPoint;

        switch(Arrays.asList(points).indexOf(lowestPoint)){
            case 0:
                break;
            case 1:
                tempPoint = points[1];
                points[1] = points[0];
                points[0] = tempPoint;
                tempPoint = points[2];
                points[2] = points[3];
                points[3] = tempPoint;
                break;
            case 2:
                tempPoint = points[2];
                points[2] = points[0];
                points[0] = tempPoint;
                tempPoint = points[3];
                points[3] = points[1];
                points[1] = tempPoint;
                break;
            case 3:
                tempPoint = points[3];
                points[3] = points[0];
                points[0] = tempPoint;
                tempPoint = points[2];
                points[2] = points[1];
                points[1] = tempPoint;
                break;
        }

        return points;
    }
}