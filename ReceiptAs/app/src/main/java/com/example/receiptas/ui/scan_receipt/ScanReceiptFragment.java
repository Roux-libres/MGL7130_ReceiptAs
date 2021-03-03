package com.example.receiptas.ui.scan_receipt;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
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

import com.example.receiptas.MaterialDropdownMenuArrayAdapter;
import com.example.receiptas.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;

public class ScanReceiptFragment extends Fragment implements View.OnClickListener {

    private ScanReceiptViewModel scanReceiptViewModel;

    private TextInputEditText inputReceiptName;
    private EditText inputReceiptPrice;
    private AutoCompleteTextView receiptCurrency;

    private RecyclerView recyclerView;
    private ImageView shape;
    private FloatingActionButton validation;
    private GalleryAdapter galleryAdapter;

    private static final int MY_READ_PERMISSION_CODE = 101;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scanReceiptViewModel = new ViewModelProvider(this).get(ScanReceiptViewModel.class);
        View root = inflater.inflate(R.layout.fragment_scan_receipt, container, false);

        this.inputReceiptName = root.findViewById(R.id.input_scan_receipt_name);
        if(scanReceiptViewModel.hasReceiptName()){
            this.inputReceiptName.setText(scanReceiptViewModel.getReceiptName());
        }

        this.inputReceiptPrice = root.findViewById(R.id.input_scan_receipt_price);
        if(scanReceiptViewModel.hasReceiptPrice()){
            this.inputReceiptPrice.setText(String.valueOf(scanReceiptViewModel.getReceiptPrice()));
        }

        this.receiptCurrency = root.findViewById(R.id.currency_menu_text_view);
        ArrayList<String> currencyArray = new ArrayList<String>(
                Arrays.asList(getResources().getStringArray(R.array.currencies_array)));
        MaterialDropdownMenuArrayAdapter adapter = new MaterialDropdownMenuArrayAdapter(getContext(),
                R.layout.list_item, currencyArray);
        this.receiptCurrency.setAdapter(adapter);

        if(scanReceiptViewModel.hasReceiptCurrency()){
            this.receiptCurrency.setText((CharSequence) adapter.getItem(
                    adapter.getPosition(scanReceiptViewModel.getReceiptCurrency())), false);
        } else {
            this.receiptCurrency.setText((CharSequence) adapter.getItem(0), false);
        }

        this.receiptCurrency.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = getActivity().getCurrentFocus();
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

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

                String inputReceiptNameString = this.inputReceiptName.getText().toString();
                if(this.scanReceiptViewModel.isStringSet(inputReceiptNameString)){
                    this.scanReceiptViewModel.setReceiptName(inputReceiptNameString);
                }

                String inputReceiptPriceString = this.inputReceiptPrice.getText().toString();
                if(scanReceiptViewModel.isStringSet(inputReceiptPriceString)) {
                    this.scanReceiptViewModel.setReceiptPrice(Float.parseFloat(inputReceiptPriceString));
                }

                this.scanReceiptViewModel.setReceiptCurrency(this.receiptCurrency.getText().toString());

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
                    holder.image.setBackgroundResource(R.drawable.gallery_border_unselected);
                    scanReceiptViewModel.removeSelectedImage(path);
                    if(scanReceiptViewModel.getSelectedImage().size() == 0){
                        validation.setVisibility(View.INVISIBLE);
                    }
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