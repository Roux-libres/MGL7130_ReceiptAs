package com.example.receiptas.ui.scan_receipt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

import androidx.exifinterface.media.ExifInterface;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.receiptas.model.domain_model.Receipt;
import com.example.receiptas.model.repository.MainRepository;
import com.example.receiptas.model.util.DataState;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ScanReceiptViewModel extends ViewModel {

    private MainRepository mainRepository;
    private MutableLiveData<ArrayList<String>> images = new MutableLiveData<ArrayList<String>>();
    private MutableLiveData<ArrayList<String>> selectedImages = new MutableLiveData<ArrayList<String>>();
    private MutableLiveData<Float> receiptSpecifiedPrice = new MutableLiveData<Float>();
    private MutableLiveData<ArrayList<Bitmap>> processedImages = new MutableLiveData<ArrayList<Bitmap>>();
    private MutableLiveData<DataState<ArrayList<String>>> items = new MutableLiveData<>();
    private MutableLiveData<DataState<ArrayList<String>>> prices = new MutableLiveData<>();
    private Receipt receipt;

    @Inject
    public ScanReceiptViewModel(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
        this.selectedImages.setValue(new ArrayList<String>());
        this.processedImages.setValue(new ArrayList<Bitmap>());
        this.receipt = new Receipt();
    }

    public void parseTextFromImages() {
        this.items.setValue(
            this.mainRepository.getTextFromImages(
                fromBitmapToBase64Images(getSublist(processedImages.getValue(), 0, 2))
            )
        );

        this.prices.setValue(
            this.mainRepository.getTextFromImages(
                fromBitmapToBase64Images(getSublist(processedImages.getValue(), 1, 2))
            )
        );
    }

    private <T> ArrayList<T> getSublist(ArrayList<T> list, int startIndex, int step) {
        ArrayList<T> sublist = new ArrayList<>();
        for(int i = startIndex; i < list.size(); i += step) {
            sublist.add(list.get(i));
        }
        return sublist;
    }

    private ArrayList<String> fromBitmapToBase64Images(ArrayList<Bitmap> imageSublist) {
        ArrayList<String> base64Images = new ArrayList<>();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        for(Bitmap bitmap : imageSublist) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            base64Images.add(
                new StringBuilder("data:image/png;base64,")
                    .append(Base64.encodeToString(byteArray, Base64.DEFAULT))
                    .toString());
            byteArrayOutputStream.reset();
        }

        return base64Images;
    }

    public void addSelectedImage(String image){
        this.selectedImages.getValue().add(image);
    }

    public void removeSelectedImage(String image){
        this.selectedImages.getValue().remove(image);
    }

    public void clearSelectedImages(){
        this.selectedImages.getValue().clear();
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public MutableLiveData<ArrayList<String>> getImages(){
        return this.images;
    }

    public MutableLiveData<ArrayList<String>> getSelectedImages(){
        return this.selectedImages;
    }

    public int getNumberOfSelectedImages(){
        return this.selectedImages.getValue().size();
    }

    public MutableLiveData<Float> getReceiptSpecifiedPrice(){
        return this.receiptSpecifiedPrice;
    }

    public void removeProcessedImage(int index){
        this.processedImages.getValue().remove(index);
    }

    public MutableLiveData<ArrayList<Bitmap>> getProcessedImages(){
        return this.processedImages;
    }

    public int getNumberOfProcessedImages(){
        return this.processedImages.getValue().size();
    }

    public MutableLiveData<DataState<ArrayList<String>>> getItems() {
        return items;
    }

    public MutableLiveData<DataState<ArrayList<String>>> getPrices() {
        return prices;
    }

    public Bitmap rotateBitmap(String imagePath){
        ExifInterface ei = null;
        Bitmap imageBitmap = null;

        try {
            ei = new ExifInterface(imagePath);
            imageBitmap = BitmapFactory.decodeFile(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        float angle = 0;

        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                angle = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                angle = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                angle = 270;
                break;
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(),
                matrix, true);
    }

    public void removeSavedData(){
        this.receiptSpecifiedPrice.setValue(null);
        this.processedImages.setValue(new ArrayList<Bitmap>());
        this.items.setValue(new DataState<ArrayList<String>>());
        this.prices.setValue(new DataState<ArrayList<String>>());
        this.receipt = new Receipt();
    }
}