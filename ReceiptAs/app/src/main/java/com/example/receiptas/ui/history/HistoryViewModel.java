package com.example.receiptas.ui.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class HistoryViewModel extends ViewModel {

    private MutableLiveData<List<String>> receipts;

    public HistoryViewModel() {
        receipts = new MutableLiveData<>();
    }

    public LiveData<List<String>> getReceipts() {
        return receipts;
    }
}