package com.example.receiptas.ui.scan_receipt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.receiptas.R;

import java.io.File;

public class ScanReceiptProcessImageFragment extends Fragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_receipt_process_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        String image_path = ScanReceiptProcessImageFragmentArgs.fromBundle(getArguments()).getImagePath();
        File imageFile = new File(image_path);

        if(imageFile.exists()){
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            ImageView image = view.findViewById(R.id.image);
            image.setImageBitmap(imageBitmap);
        }

    }
}