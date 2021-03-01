package com.example.receiptas.ui.scan_receipt;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ScanReceiptViewModel extends ViewModel {

    private ArrayList<String> images;
    private ArrayList<String> selectedImages;

    public ScanReceiptViewModel() {
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

    public ArrayList<String> getSelectedImage(){ return this.selectedImages; }
}