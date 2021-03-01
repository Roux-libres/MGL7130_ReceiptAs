package com.example.receiptas.ui.scan_receipt;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.receiptas.R;

public class ScanReceiptProcessImageFragment extends Fragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_receipt_process_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView tv = view.findViewById(R.id.text);
        String image_path = ScanReceiptProcessImageFragmentArgs.fromBundle(getArguments()).getImagePath();
        tv.setText(image_path);
    }
}