package com.example.receiptas.ui.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.receiptas.model.repository.MainRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HistoryViewModel extends ViewModel {

    private MutableLiveData<List<String>> receipts;
    private MainRepository mainRepository;

    @Inject
    public HistoryViewModel(MainRepository mainRepository) {
        receipts = new MutableLiveData<>();
        this.mainRepository = mainRepository;
    }

    public MutableLiveData<List<String>> getReceipts() {
        return receipts;
    }
}