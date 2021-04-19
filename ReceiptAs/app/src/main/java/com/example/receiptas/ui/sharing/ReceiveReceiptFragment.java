package com.example.receiptas.ui.sharing;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.receiptas.R;

public class ReceiveReceiptFragment extends Fragment {

    private ReceiveReceiptViewModel mViewModel;

    public static ReceiveReceiptFragment newInstance() {
        return new ReceiveReceiptFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receive_receipt, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReceiveReceiptViewModel.class);
        // TODO: Use the ViewModel
    }

}