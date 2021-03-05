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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.receiptas.R;
import com.example.receiptas.ui.scan_receipt.resizableview.ResizableView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

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

        Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        ImageView image = root.findViewById(R.id.image);
        image.setImageBitmap(imageBitmap);

        this.validation = root.findViewById(R.id.fab_validation);
        this.validation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResizableView resizableView = root.findViewById(R.id.resizable_view);
                image.setImageBitmap(shapeBitmap(imageBitmap, resizableView));
            }
        });

        return root;
    }

    private Bitmap shapeBitmap(Bitmap imageBitmap, ResizableView resizableView){
        Point[] points = resizableView.getPoints();

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        System.out.println("Screen width : " + metrics.widthPixels + "\tScreen height : " + metrics.heightPixels);

        int widthDifference = resizableView.getWidth() - imageBitmap.getWidth();
        int heightDifference = resizableView.getHeight() - imageBitmap.getHeight();

        System.out.println("View width : " + resizableView.getWidth() + "\tView height : " + resizableView.getHeight());
        System.out.println("Image width : " + imageBitmap.getWidth() + "\tImage height : " + imageBitmap.getHeight());
        System.out.println("Width difference : " + widthDifference + "\tHeight difference: " + heightDifference);

        for(Point point : points){
            point.set(point.x - (widthDifference / 2), point.y - (heightDifference / 2));
        }

        this.swapPoints(points);

        System.out.println("Point0 X : " + points[0].x + "\tPoint0 Y : " + points[0].y);
        System.out.println("Point3 X : " + points[3].x + "\tPoint3 Y : " + points[3].y);

        //TODO: check greater distance between p0-p3 and p1-p2
        Bitmap imageBitmapCropped = Bitmap.createBitmap(imageBitmap,
                points[0].x,
                points[0].y,
                points[3].x - points[0].x,
                points[3].y - points[0].y);

        return imageBitmapCropped;
    }

    private Point[] swapPoints(Point[] points){
        //TODO: Swap points in order to find the greatest area

        return points;
    }
}