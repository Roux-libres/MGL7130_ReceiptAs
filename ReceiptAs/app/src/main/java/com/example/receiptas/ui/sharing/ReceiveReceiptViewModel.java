package com.example.receiptas.ui.sharing;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.example.receiptas.model.repository.MainRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class ReceiveReceiptViewModel extends ViewModel {
    private MainRepository mainRepository;
    private String receiptDirectory;

    @Inject
    public ReceiveReceiptViewModel(MainRepository mainRepository,  @ApplicationContext Context context) {
        this.mainRepository = mainRepository;
        this.receiptDirectory = context.getFilesDir().toString();

    }

    public String addReceiptFromJsonString(String json) {
        return this.mainRepository.addReceiptFromJsonString(json, this.receiptDirectory);
    }

}