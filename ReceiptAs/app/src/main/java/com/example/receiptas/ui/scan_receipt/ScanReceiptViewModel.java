package com.example.receiptas.ui.scan_receipt;

import android.graphics.Bitmap;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ScanReceiptViewModel extends ViewModel {

    private MutableLiveData<ArrayList<String>> images = new MutableLiveData<ArrayList<String>>();
    private MutableLiveData<ArrayList<String>> selectedImages = new MutableLiveData<ArrayList<String>>();
    private MutableLiveData<String> receiptName = new MutableLiveData<String>();
    private MutableLiveData<Float> receiptPrice = new MutableLiveData<Float>();
    private MutableLiveData<String> receiptCurrency = new MutableLiveData<String>();
    private MutableLiveData<ArrayList<Bitmap>> processedImages = new MutableLiveData<ArrayList<Bitmap>>();

    public ScanReceiptViewModel() {
        this.selectedImages.setValue(new ArrayList<String>());
        this.processedImages.setValue(new ArrayList<Bitmap>());
    }

    public void setImages(ArrayList<String> images){
        this.images.setValue(images);
    }

    public ArrayList<String> getImages(){
        return this.images.getValue();
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

    public ArrayList<String> getSelectedImages(){
        return this.selectedImages.getValue();
    }

    public int getNumberOfSelectedImages(){
        return this.selectedImages.getValue().size();
    }

    public boolean hasReceiptName(){
        if(TextUtils.isEmpty(this.receiptName.getValue())){
            return false;
        } else {
            return true;
        }
    }

    public void setReceiptName(String receiptName){
        this.receiptName.setValue(receiptName);
    }

    public String getReceiptName(){
        return this.receiptName.getValue();
    }

    public boolean hasReceiptPrice(){
        if(this.receiptPrice.getValue() == null){
            return false;
        } else {
            return true;
        }
    }

    public void setReceiptPrice(float receiptPrice){
        this.receiptPrice.setValue(receiptPrice);
    }

    public float getReceiptPrice(){
        return this.receiptPrice.getValue();
    }

    public boolean hasReceiptCurrency(){
        if(TextUtils.isEmpty(this.receiptCurrency.getValue())){
            return false;
        } else {
            return true;
        }
    }

    public void setReceiptCurrency(String receiptCurrency){
        this.receiptCurrency.setValue(receiptCurrency);
    }

    public String getReceiptCurrency(){
        return this.receiptCurrency.getValue();
    }

    public boolean isStringSet(String string){
        if(TextUtils.isEmpty(string)){
            return false;
        } else {
            return true;
        }
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

    public ArrayList<Bitmap> getProcessedImages(){
        return this.processedImages.getValue();
    }

    public int getNumberOfProcessedImages(){
        return this.processedImages.getValue().size();
    }
}