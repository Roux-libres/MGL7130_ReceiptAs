package com.example.receiptas;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.receiptas.model.domain_model.Receipt;
import com.example.receiptas.model.repository.MainRepository;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Receipt>> receipts;
    private MainRepository mainRepository;
    private String receiptDirectory;

    @Inject
    public MainViewModel(MainRepository mainRepository, @ApplicationContext Context context) {
        this.receipts = new MutableLiveData<>();
        this.mainRepository = mainRepository;
        this.receiptDirectory = context.getFilesDir().toString();

        this.setReceipts();
    }

    private void setReceipts(){
        this.receipts.setValue(this.mainRepository.loadReceipts(this.receiptDirectory));
    }

    public LiveData<ArrayList<Receipt>> getReceipts() {
        return this.receipts;
    }

    public Receipt getReceipt(int index) {
        return this.receipts.getValue().get(index);
    }

    public void deleteReceipt(int index){
        this.mainRepository.removeReceipt(index, this.receiptDirectory);
    }

    public int createReceipt(Receipt newReceipt){
        this.mainRepository.addReceipt(newReceipt, this.receiptDirectory);
        return this.receipts.getValue().size() - 1;
    }
}