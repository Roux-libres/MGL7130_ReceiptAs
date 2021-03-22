package com.example.receiptas.ui.scan_receipt;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Base64;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.receiptas.model.domain_model.Receipt;
import com.example.receiptas.model.repository.MainRepository;
import com.example.receiptas.model.util.DataState;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ScanReceiptViewModel extends ViewModel {

    private MainRepository mainRepository;
    private MutableLiveData<ArrayList<String>> images = new MutableLiveData<ArrayList<String>>();
    private MutableLiveData<ArrayList<String>> selectedImages = new MutableLiveData<ArrayList<String>>();
    private MutableLiveData<Bitmap> cameraCaptureBitmap = new MutableLiveData<Bitmap>();
    private MutableLiveData<Float> receiptSpecifiedPrice = new MutableLiveData<Float>();
    private MutableLiveData<String> receiptCurrency = new MutableLiveData<String>();
    private MutableLiveData<ArrayList<Bitmap>> processedImages = new MutableLiveData<ArrayList<Bitmap>>();
    private MutableLiveData<DataState<ArrayList<String>>> items = new MutableLiveData<>();
    private MutableLiveData<DataState<ArrayList<String>>> prices = new MutableLiveData<>();
    private Receipt theReceipt;

    @Inject
    public ScanReceiptViewModel(MainRepository mainRepository) {
        this.selectedImages.setValue(new ArrayList<String>());
        this.processedImages.setValue(new ArrayList<Bitmap>());
        this.mainRepository = mainRepository;
        this.theReceipt = new Receipt();
    }

    public Receipt getTheReceipt() {
        return theReceipt;
    }

    public MutableLiveData<ArrayList<String>> getImages(){
        return this.images;
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

    public MutableLiveData<ArrayList<String>> getSelectedImages(){
        return this.selectedImages;
    }

    public int getNumberOfSelectedImages(){
        return this.selectedImages.getValue().size();
    }

    public MutableLiveData<Bitmap> getCameraCaptureBitmap(){
        return this.cameraCaptureBitmap;
    }

    public boolean hasReceiptPrice(){
        if(this.receiptSpecifiedPrice.getValue() == null){
            return false;
        } else {
            return true;
        }
    }

    public MutableLiveData<Float> getReceiptSpecifiedPrice(){
        return this.receiptSpecifiedPrice;
    }

    public boolean hasReceiptCurrency(){
        if(TextUtils.isEmpty(this.receiptCurrency.getValue())){
            return false;
        } else {
            return true;
        }
    }

    public MutableLiveData<String> getReceiptCurrency(){
        return this.receiptCurrency;
    }

    public void addProcessedImage(Bitmap imageBitmap){
        this.processedImages.getValue().add(imageBitmap);
    }

    public void removeProcessedImage(int index){
        this.processedImages.getValue().remove(index);
    }

    public void clearProcessedImages(){
        this.processedImages.getValue().clear();
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
}