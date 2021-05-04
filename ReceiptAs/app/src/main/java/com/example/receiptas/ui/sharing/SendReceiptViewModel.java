package com.example.receiptas.ui.sharing;

import androidx.lifecycle.ViewModel;

import com.example.receiptas.model.repository.MainRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SendReceiptViewModel extends ViewModel {

    private MainRepository mainRepository;

    @Inject
    public SendReceiptViewModel(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    public String getReceiptAsJsonString(int receipt_id) {
        return this.mainRepository.getReceiptAsJsonString(receipt_id);
    }

}