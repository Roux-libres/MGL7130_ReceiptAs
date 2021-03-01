package com.example.receiptas.ui.scan_receipt;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receiptas.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ScanReceiptFragment extends Fragment implements View.OnClickListener {

    private ScanReceiptViewModel scanReceiptViewModel;
    RecyclerView recyclerView;
    ImageView shape;
    FloatingActionButton validation;
    GalleryAdapter galleryAdapter;
    List<String> images;

    private static final int MY_READ_PERMISSION_CODE = 101;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scanReceiptViewModel = new ViewModelProvider(this).get(ScanReceiptViewModel.class);
        View root = inflater.inflate(R.layout.fragment_scan_receipt, container, false);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.currencies_array, R.layout.list_item);
        AutoCompleteTextView autoCompleteTextView = root.findViewById(R.id.currency_menu_text_view);
        autoCompleteTextView.setText(adapter.getItem(0), false);
        autoCompleteTextView.setAdapter(adapter);

        ImageButton buttonLoadImage = root.findViewById(R.id.button_add_image);
        buttonLoadImage.setOnClickListener(this);

        recyclerView = root.findViewById(R.id.gallery_recycler_view);
        shape = root.findViewById(R.id.gallery_recycler_view_mask);
        shape.setOnClickListener(this);
        validation = root.findViewById(R.id.fab_validation);

        validation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_READ_PERMISSION_CODE);
        }

        loadImages();

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_image:
                showGalleryOverlay();
                break;
            case R.id.gallery_recycler_view_mask:
                hideGalleryOverlay();
                galleryAdapter.resetImageViewBackground();
                break;
        }
    }

    private void hideGalleryOverlay(){
        recyclerView.setVisibility(View.INVISIBLE);
        shape.setVisibility(View.INVISIBLE);
        validation.setVisibility(View.INVISIBLE);
    }

    private void showGalleryOverlay(){
        recyclerView.setVisibility(View.VISIBLE);
        shape.setVisibility(View.VISIBLE);
        validation.setVisibility(View.VISIBLE);
    }

    private void loadImages(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        images = ImagesGallery.listOfImages(getContext());
        galleryAdapter = new GalleryAdapter(getContext(), images, new GalleryAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(GalleryAdapter.ViewHolder holder, String path) {
                if(holder.image.getBackground().getConstantState() ==
                        getResources().getDrawable(R.drawable.gallery_border_unselected).getConstantState()){
                    holder.image.setBackgroundResource(R.drawable.gallery_border_selected);
                } else {
                    holder.image.setBackgroundResource(R.drawable.gallery_border_unselected);
                }

                //Toast.makeText(getContext(), ""+path, Toast.LENGTH_SHORT).show();

            }
        });

        recyclerView.setAdapter(galleryAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_READ_PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getContext(), "Read external storage permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Read external storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}