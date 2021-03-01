package com.example.receiptas.ui.scan_receipt;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receiptas.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ScanReceiptFragment extends Fragment implements View.OnClickListener {

    private ScanReceiptViewModel scanReceiptViewModel;
    private RecyclerView recyclerView;
    private ImageView shape;
    private FloatingActionButton validation;
    private GalleryAdapter galleryAdapter;

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

        this.recyclerView = root.findViewById(R.id.gallery_recycler_view);
        this.shape = root.findViewById(R.id.gallery_recycler_view_mask);
        this.shape.setOnClickListener(this);
        this.validation = root.findViewById(R.id.fab_validation);
        this.validation.setOnClickListener(this);

        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_READ_PERMISSION_CODE);
        } else {
            this.loadImages();
        }

        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_image:
                this.showGalleryOverlay();
                break;
            case R.id.gallery_recycler_view_mask:
                this.hideGalleryOverlay();
                this.galleryAdapter.resetImageViewBackground();
                this.scanReceiptViewModel.clearSelectedImages();
                break;
            case R.id.fab_validation:
                /*
                for(String image_path : scanReceiptViewModel.getSelectedImage()){
                    ScanReceiptFragmentDirections.ActionNavScanReceiptToNavScanReceiptProcessImage action =
                            ScanReceiptFragmentDirections.actionNavScanReceiptToNavScanReceiptProcessImage();
                    action.setImagePath(image_path);
                    Navigation.findNavController(view).navigate(action);
                }
                */

                String image_path = scanReceiptViewModel.getSelectedImage().get(0);
                ScanReceiptFragmentDirections.ActionNavScanReceiptToNavScanReceiptProcessImage action =
                        ScanReceiptFragmentDirections.actionNavScanReceiptToNavScanReceiptProcessImage();
                action.setImagePath(image_path);
                Navigation.findNavController(view).navigate(action);

                break;
        }
    }

    private void hideGalleryOverlay(){
        this.recyclerView.setVisibility(View.INVISIBLE);
        this.shape.setVisibility(View.INVISIBLE);
        this.validation.setVisibility(View.INVISIBLE);
    }

    private void showGalleryOverlay(){
        this.recyclerView.setVisibility(View.VISIBLE);
        this.shape.setVisibility(View.VISIBLE);
    }

    private void loadImages(){
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        this.scanReceiptViewModel.setImages(ImagesGallery.listOfImages(getContext()));
        this.galleryAdapter = new GalleryAdapter(getContext(), scanReceiptViewModel.getImages(), new GalleryAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(GalleryAdapter.ViewHolder holder, String path) {
                if(holder.image.getBackground().getConstantState() ==
                        getResources().getDrawable(R.drawable.gallery_border_unselected).getConstantState()){
                    validation.setVisibility(View.VISIBLE);
                    holder.image.setBackgroundResource(R.drawable.gallery_border_selected);
                    scanReceiptViewModel.addSelectedImage(path);
                } else {
                    //HIDE FAB
                    holder.image.setBackgroundResource(R.drawable.gallery_border_unselected);
                    scanReceiptViewModel.removeSelectedImage(path);
                }

            }
        });

        this.recyclerView.setAdapter(galleryAdapter);
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