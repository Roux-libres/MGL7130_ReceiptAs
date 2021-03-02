package com.example.receiptas.ui.scan_receipt;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ScanReceiptViewModel extends ViewModel {

    private SavedStateHandle savedState;
    private ArrayList<String> images;
    private ArrayList<String> selectedImages;

    public ScanReceiptViewModel(SavedStateHandle savedStateHandle) {
        this.savedState = savedStateHandle;
        this.selectedImages = new ArrayList<>();
    }

    public void setImages(ArrayList<String> images){
        this.images = images;
    }

    public ArrayList<String> getImages(){
        return this.images;
    }

    public void addSelectedImage(String image){
        this.selectedImages.add(image);
    }

    public void removeSelectedImage(String image){
        this.selectedImages.remove(image);
    }

    public void clearSelectedImages(){
        this.selectedImages.clear();
    }

    public ArrayList<String> getSelectedImage(){
        return this.selectedImages;
    }

    public boolean hasReceiptName(){
        return this.savedState.contains("receipt_name");
    }

    public void setReceiptName(String receiptName){
        this.savedState.set("receipt_name", receiptName);
    }

    public String getReceiptName(){
        return this.savedState.get("receipt_name");
    }

    public boolean hasReceiptPrice(){
        return this.savedState.contains("receipt_price");
    }

    public void setReceiptPrice(float receiptPrice){
        this.savedState.set("receipt_price", receiptPrice);
    }

    public float getReceiptPrice(){
        return this.savedState.get("receipt_price");
    }

    public boolean hasReceiptCurrency(){
        return this.savedState.contains("receipt_currency");
    }

    public void setReceiptCurrency(String receiptCurrency){
        this.savedState.set("receipt_currency", receiptCurrency);
    }

    public String getReceiptCurrency(){
        return this.savedState.get("receipt_currency");
    }

    public boolean isStringSet(String string){
        if(string.matches("")){
            return false;
        } else {
            return true;
        }
    }
}