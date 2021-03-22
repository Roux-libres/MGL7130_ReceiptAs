package com.example.receiptas.ui.scan_receipt;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receiptas.MainActivity;
import com.example.receiptas.MaterialDropdownMenuArrayAdapter;
import com.example.receiptas.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ScanReceiptFragment extends Fragment implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 1;

    private ScanReceiptViewModel scanReceiptViewModel;

    private TextInputEditText inputReceiptName;
    private EditText inputReceiptPrice;
    private AutoCompleteTextView receiptCurrency;
    private RecyclerView processedImagesRecyclerView;
    private ProcessedImageAdapter processedImageAdapter;

    private RecyclerView galleryRecyclerView;
    private ImageView shape;
    private FloatingActionButton validationSelection;
    private GalleryAdapter galleryAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.validate, menu);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scanReceiptViewModel = new ViewModelProvider(getActivity()).get(ScanReceiptViewModel.class);
        View root = inflater.inflate(R.layout.fragment_scan_receipt, container, false);

        getActivity().invalidateOptionsMenu();

        this.inputReceiptName = root.findViewById(R.id.input_scan_receipt_name);
        if(!TextUtils.isEmpty(scanReceiptViewModel.getTheReceipt().getName())){
            this.inputReceiptName.setText(scanReceiptViewModel.getTheReceipt().getName());
        }

        this.inputReceiptPrice = root.findViewById(R.id.input_scan_receipt_price);
        if(scanReceiptViewModel.hasReceiptPrice()){
            this.inputReceiptPrice.setText(String.valueOf(scanReceiptViewModel.getReceiptSpecifiedPrice().getValue()));
        }

        this.receiptCurrency = root.findViewById(R.id.currency_menu_text_view);
        ArrayList<String> currencyArray = new ArrayList<String>(
                Arrays.asList(getResources().getStringArray(R.array.currencies_array)));
        MaterialDropdownMenuArrayAdapter adapter = new MaterialDropdownMenuArrayAdapter(getContext(),
                R.layout.list_item, currencyArray);
        this.receiptCurrency.setAdapter(adapter);

        if(scanReceiptViewModel.hasReceiptCurrency()){
            this.receiptCurrency.setText((CharSequence) adapter.getItem(
                    adapter.getPosition(scanReceiptViewModel.getReceiptCurrency().getValue())), false);
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

        if(this.scanReceiptViewModel.getNumberOfProcessedImages() > 0){
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.validate_button & this.saveDetails()) {
            NavDirections goToItemCorrection =
                ScanReceiptFragmentDirections.actionNavScanReceiptToItemCorrectionFragment();
            Navigation.findNavController(getView()).navigate(goToItemCorrection);
            return true;
        } else {
            this.openBlockingDialog();
            return super.onOptionsItemSelected(item);
        }
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
                this.saveDetails();

                ScanReceiptFragmentDirections.ActionNavScanReceiptToNavScanReceiptProcessImage action =
                        ScanReceiptFragmentDirections.actionNavScanReceiptToNavScanReceiptProcessImage(
                                getString(R.string.scan_receipt_process_image_product_name));
                action.setNumberOfImages(this.scanReceiptViewModel.getNumberOfSelectedImages());
                Navigation.findNavController(view).navigate(action);
                break;
        }
    }

    //TODO REFACTO view model getter/setter mutabledata + currency
    private boolean saveDetails(){
        String inputReceiptNameString = this.inputReceiptName.getText().toString();
        String inputReceiptPriceString = this.inputReceiptPrice.getText().toString();

        boolean allInfoSpecified = !TextUtils.isEmpty(inputReceiptNameString)
                & !TextUtils.isEmpty(inputReceiptPriceString)
                & !this.scanReceiptViewModel.getProcessedImages().getValue().isEmpty();

        if(allInfoSpecified) {
            this.scanReceiptViewModel.getTheReceipt().setName(inputReceiptNameString);
            this.scanReceiptViewModel.getReceiptSpecifiedPrice().setValue(Float.parseFloat(inputReceiptPriceString));
            this.scanReceiptViewModel.getReceiptCurrency().setValue(this.receiptCurrency.getText().toString());
        }

        return allInfoSpecified;
    }

    private void hideGalleryOverlay(){
        this.galleryRecyclerView.setVisibility(View.INVISIBLE);
        this.shape.setVisibility(View.INVISIBLE);
        this.validationSelection.setVisibility(View.INVISIBLE);
    }

    private void showGalleryOverlay(){
        this.galleryRecyclerView.setVisibility(View.VISIBLE);
        this.shape.setVisibility(View.VISIBLE);
    }

    private void loadGalleryImages(){
        this.galleryRecyclerView.setHasFixedSize(true);

        if(((MainActivity) getActivity()).isTablet()){
            this.galleryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        } else {
            this.galleryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }

        this.scanReceiptViewModel.getImages().setValue(ImagesGallery.listOfImages(getContext()));
        this.galleryAdapter = new GalleryAdapter(getContext(), scanReceiptViewModel.getImages().getValue(), new GalleryAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(GalleryAdapter.ViewHolder holder, String path) {
                if(path == ""){
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    try{
                        saveDetails();
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    } catch(ActivityNotFoundException e){
                        System.out.println(e);
                    }
                } else {
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
                        if(scanReceiptViewModel.getSelectedImages().getValue().size() == 0){
                            validationSelection.setVisibility(View.INVISIBLE);
                        }
                    }

                    holder.image.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
                }
            }
        });

        this.galleryRecyclerView.setAdapter(this.galleryAdapter);
    }

    private void loadProcessedImages(){
        this.processedImagesRecyclerView.setHasFixedSize(true);
        this.processedImagesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        this.processedImageAdapter = new ProcessedImageAdapter(getContext(), this.scanReceiptViewModel.getProcessedImages().getValue(), new ProcessedImageAdapter.PhotoListener() {
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
                                int index = scanReceiptViewModel.getProcessedImages().getValue().indexOf(imageBitmap);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            this.scanReceiptViewModel.clearSelectedImages();

            this.scanReceiptViewModel.getCameraCaptureBitmap().setValue(imageBitmap);
            ScanReceiptFragmentDirections.ActionNavScanReceiptToNavScanReceiptProcessImage action =
                    ScanReceiptFragmentDirections.actionNavScanReceiptToNavScanReceiptProcessImage(
                            getString(R.string.scan_receipt_process_image_product_name));
            Navigation.findNavController(getView()).navigate(action);
        }
    }

    private void openBlockingDialog() {
        AlertDialog.Builder blockingDialog = new AlertDialog.Builder(getContext());
        blockingDialog.setTitle(R.string.scan_receipt_missing_info_title);
        blockingDialog.setMessage(R.string.scan_receipt_missing_info_description);
        blockingDialog.setPositiveButton(R.string.dialog_positive, null);
        blockingDialog.create().show();
    }


}