package com.example.receiptas.ui.scan_receipt;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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

import com.example.receiptas.MainActivity;
import com.example.receiptas.MaterialDropdownMenuArrayAdapter;
import com.example.receiptas.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class ScanReceiptFragment extends Fragment implements View.OnClickListener {

    private ScanReceiptViewModel scanReceiptViewModel;

    private TextInputEditText inputReceiptName;
    private EditText inputReceiptPrice;
    private AutoCompleteTextView receiptCurrency;
    private RecyclerView processedImagesRecyclerView;
    private ProcessedImageAdapter processedImageAdapter;
    private FloatingActionButton validationReceipt;

    private RecyclerView galleryRecyclerView;
    private ImageView shape;
    private FloatingActionButton validationSelection;
    private GalleryAdapter galleryAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scanReceiptViewModel = new ViewModelProvider(getActivity()).get(ScanReceiptViewModel.class);
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

        this.processedImagesRecyclerView = root.findViewById(R.id.processed_images_recycler_view);

        this.validationReceipt = root.findViewById(R.id.fab_validation_receipt);

        if(this.scanReceiptViewModel.getNumberOfProcessedImages() > 0){
            this.validationReceipt.setOnClickListener(this);
            this.validationReceipt.setVisibility(View.VISIBLE);
            this.loadProcessedImages();
        }

        ImageButton buttonLoadImage = root.findViewById(R.id.button_add_image);
        buttonLoadImage.setOnClickListener(this);

        this.galleryRecyclerView = root.findViewById(R.id.gallery_recycler_view);
        this.loadGalleryImages();
        this.shape = root.findViewById(R.id.gallery_recycler_view_mask);
        this.shape.setOnClickListener(this);
        this.validationSelection = root.findViewById(R.id.fab_validation_selection);
        this.validationSelection.setOnClickListener(this);

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
            case R.id.fab_validation_selection:
                String inputReceiptNameString = this.inputReceiptName.getText().toString();
                if(this.scanReceiptViewModel.isStringSet(inputReceiptNameString)){
                    this.scanReceiptViewModel.setReceiptName(inputReceiptNameString);
                }

                String inputReceiptPriceString = this.inputReceiptPrice.getText().toString();
                if(scanReceiptViewModel.isStringSet(inputReceiptPriceString)) {
                    this.scanReceiptViewModel.setReceiptPrice(Float.parseFloat(inputReceiptPriceString));
                }

                this.scanReceiptViewModel.setReceiptCurrency(this.receiptCurrency.getText().toString());

                ScanReceiptFragmentDirections.ActionNavScanReceiptToNavScanReceiptProcessImage action =
                        ScanReceiptFragmentDirections.actionNavScanReceiptToNavScanReceiptProcessImage(
                                getString(R.string.scan_receipt_process_image_product_name));
                Navigation.findNavController(view).navigate(action);

                break;
            case R.id.fab_validation_receipt:
                //TODO: Navigate to next fragment OR treat the data first
                break;
        }
    }

    private void hideGalleryOverlay(){
        this.galleryRecyclerView.setVisibility(View.INVISIBLE);
        this.shape.setVisibility(View.INVISIBLE);
        this.validationSelection.setVisibility(View.INVISIBLE);
        if(this.scanReceiptViewModel.getNumberOfProcessedImages() > 0){
            this.validationReceipt.setVisibility(View.VISIBLE);
        }
    }

    private void showGalleryOverlay(){
        this.validationReceipt.setVisibility(View.INVISIBLE);
        this.galleryRecyclerView.setVisibility(View.VISIBLE);
        this.shape.setVisibility(View.VISIBLE);
    }

    private void loadGalleryImages(){
        this.galleryRecyclerView.setHasFixedSize(true);
        this.galleryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        this.scanReceiptViewModel.setImages(ImagesGallery.listOfImages(getContext()));
        this.galleryAdapter = new GalleryAdapter(getContext(), scanReceiptViewModel.getImages(), new GalleryAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(GalleryAdapter.ViewHolder holder, String path) {
                float scale = getResources().getDisplayMetrics().density;
                int paddingSize;

                if(holder.image.getBackground().getConstantState() ==
                        getResources().getDrawable(R.drawable.gallery_border_unselected).getConstantState()){
                    validationSelection.setVisibility(View.VISIBLE);
                    holder.image.setBackgroundResource(R.drawable.gallery_border_selected);
                    paddingSize = (int) (3 * scale + 0.5f);
                    scanReceiptViewModel.addSelectedImage(path);
                } else {
                    holder.image.setBackgroundResource(R.drawable.gallery_border_unselected);
                    paddingSize = (int) (1 * scale + 0.5f);
                    holder.image.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
                    scanReceiptViewModel.removeSelectedImage(path);
                    if(scanReceiptViewModel.getSelectedImages().size() == 0){
                        validationSelection.setVisibility(View.INVISIBLE);
                    }
                }

                holder.image.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
            }
        });

        this.galleryRecyclerView.setAdapter(this.galleryAdapter);
    }

    private void loadProcessedImages(){
        this.processedImagesRecyclerView.setHasFixedSize(true);
        this.processedImagesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        this.processedImageAdapter = new ProcessedImageAdapter(getContext(), this.scanReceiptViewModel.getProcessedImages(), new ProcessedImageAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(GalleryAdapter.ViewHolder holder, Bitmap imageBitmap) {
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle(getString(R.string.scan_receipt_remove_image_title))
                        .setMessage(getString(R.string.scan_receipt_remove_image_message))
                        .setNeutralButton(R.string.scan_receipt_remove_image_cancel, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton(R.string.scan_receipt_remove_image_accept, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int index = scanReceiptViewModel.getProcessedImages().indexOf(imageBitmap);

                                if(index % 2 == 0){
                                    scanReceiptViewModel.removeProcessedImage(index + 1);
                                    scanReceiptViewModel.removeProcessedImage(index);
                                } else {
                                    scanReceiptViewModel.removeProcessedImage(index);
                                    scanReceiptViewModel.removeProcessedImage(index - 1);
                                }

                                loadProcessedImages();
                            }
                        })
                        .show();
            }
        });
        this.processedImagesRecyclerView.setAdapter(this.processedImageAdapter);
    }
}