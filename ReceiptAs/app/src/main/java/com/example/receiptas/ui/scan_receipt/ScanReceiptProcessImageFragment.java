package com.example.receiptas.ui.scan_receipt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.receiptas.MainActivity;
import com.example.receiptas.R;
import com.example.receiptas.ui.scan_receipt.resizableview.ResizableView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.Arrays;

public class ScanReceiptProcessImageFragment extends Fragment {

    private ScanReceiptViewModel scanReceiptViewModel;
    private FloatingActionButton validation;
    private boolean isCameraCapture;
    private Bitmap imageBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).lockDrawer();
        View root = inflater.inflate(R.layout.fragment_scan_receipt_process_image, container, false);
        this.scanReceiptViewModel = new ViewModelProvider(getActivity()).get(ScanReceiptViewModel.class);

        int numberOfImages = getArguments().getInt("numberOfImages");
        TextView progressionTextView = root.findViewById(R.id.text_progression);
        if(numberOfImages > 1){
            String progression = String.valueOf(numberOfImages - this.scanReceiptViewModel.getNumberOfSelectedImages() + 1) + "/" + String.valueOf(numberOfImages);
            progressionTextView.setText(progression);
        } else {
            progressionTextView.setVisibility(View.INVISIBLE);
        }

        this.isCameraCapture = (this.scanReceiptViewModel.getCameraCaptureBitmap() != null);

        if(this.isCameraCapture) {
            this.imageBitmap = this.scanReceiptViewModel.getCameraCaptureBitmap();
        } else {
            String image_path = this.scanReceiptViewModel.getSelectedImages().get(0);
            File imageFile = new File(image_path);

            if(!imageFile.exists()){
                return root;
            }

            this.imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        }

        ImageView image_view = root.findViewById(R.id.image_view);
        ResizableView resizableView = root.findViewById(R.id.resizable_view);

        image_view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                ImageView image_view = getView().findViewById(R.id.image_view);

                float ratioWidth = ((float) image_view.getWidth()) / ((float) imageBitmap.getWidth());
                float ratioHeight = ((float) image_view.getHeight()) / ((float) imageBitmap.getHeight());
                float ratio = Math.min(ratioWidth, ratioHeight);
                int newWidth = (int) ((int) imageBitmap.getWidth() * ratio);
                int newHeight = (int) ((int) imageBitmap.getHeight() * ratio);

                imageBitmap = Bitmap.createScaledBitmap(imageBitmap, newWidth, newHeight, true);
                image_view.setImageBitmap(imageBitmap);
                resizableView.compareSize(image_view, imageBitmap);
            }
        });

        this.validation = root.findViewById(R.id.fab_validation_selection);
        this.validation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanReceiptViewModel.addProcessedImage(shapeBitmap(imageBitmap, resizableView));

                ((MainActivity) getActivity()).unlockDrawer();

                if(scanReceiptViewModel.getNumberOfProcessedImages() % 2 == 1){
                    ScanReceiptProcessImageFragmentDirections.ActionNavScanReceiptProcessImageSelf action =
                            ScanReceiptProcessImageFragmentDirections.actionNavScanReceiptProcessImageSelf(
                                    getString(R.string.scan_receipt_process_image_product_price));
                    action.setNumberOfImages(numberOfImages);
                    Navigation.findNavController(view).navigate(action);
                } else if(isCameraCapture){
                    ScanReceiptProcessImageFragmentDirections.ActionNavScanReceiptProcessImageToNavScanReceipt action =
                            ScanReceiptProcessImageFragmentDirections.actionNavScanReceiptProcessImageToNavScanReceipt();
                    scanReceiptViewModel.setCameraCaptureBitmap(null);
                    Navigation.findNavController(view).navigate(action);
                } else if(scanReceiptViewModel.getNumberOfSelectedImages() == 1){
                    ScanReceiptProcessImageFragmentDirections.ActionNavScanReceiptProcessImageToNavScanReceipt action =
                            ScanReceiptProcessImageFragmentDirections.actionNavScanReceiptProcessImageToNavScanReceipt();
                    scanReceiptViewModel.getSelectedImages().remove(0);
                    Navigation.findNavController(view).navigate(action);
                } else {
                    ScanReceiptProcessImageFragmentDirections.ActionNavScanReceiptProcessImageSelf action =
                            ScanReceiptProcessImageFragmentDirections.actionNavScanReceiptProcessImageSelf(
                                    getString(R.string.scan_receipt_process_image_product_name));
                    action.setNumberOfImages(numberOfImages);
                    scanReceiptViewModel.getSelectedImages().remove(0);
                    Navigation.findNavController(view).navigate(action);
                }
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