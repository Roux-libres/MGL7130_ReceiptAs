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

    private MutableLiveData<ArrayList<String>> receipts;
    private MainRepository mainRepository;

    @Inject
    public HistoryViewModel(MainRepository mainRepository) {
        receipts = new MutableLiveData<>();
        this.mainRepository = mainRepository;
    }

    public void setContext(Context context){
        ArrayList<String> receiptsNames = new ArrayList<String>();
        ArrayList<Receipt> receipts = this.mainRepository.getReceipts(context.getFilesDir().toString());

        for(Receipt receipt : receipts){
            receiptsNames.add(receipt.getName());
        }

        this.receipts.setValue(receiptsNames);
    }

    public MutableLiveData<ArrayList<String>> getReceipts() {
        return this.receipts;
    }
}