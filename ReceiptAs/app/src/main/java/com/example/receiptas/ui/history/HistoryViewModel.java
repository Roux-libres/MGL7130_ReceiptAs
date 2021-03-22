package com.example.receiptas.ui.history;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.receiptas.model.domain_model.Receipt;
import com.example.receiptas.model.repository.MainRepository;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HistoryViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Receipt>> receipts;
    private MainRepository mainRepository;

    @Inject
    public HistoryViewModel(MainRepository mainRepository) {
        receipts = new MutableLiveData<>();
        this.mainRepository = mainRepository;
    }

    public void setContext(Context context){
        this.receipts.setValue(this.mainRepository.getReceipts(context.getFilesDir().toString()));
    }

    public MutableLiveData<ArrayList<Receipt>> getReceipts() {
        return this.receipts;
    }
}