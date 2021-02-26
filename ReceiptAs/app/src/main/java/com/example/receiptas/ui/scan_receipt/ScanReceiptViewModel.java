package com.example.receiptas.ui.scan_receipt;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScanReceiptViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ScanReceiptViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Scan Receipt fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}