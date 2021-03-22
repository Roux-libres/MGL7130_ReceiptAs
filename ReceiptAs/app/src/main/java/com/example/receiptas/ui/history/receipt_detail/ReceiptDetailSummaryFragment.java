package com.example.receiptas.ui.history.receipt_detail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.receiptas.MainViewModel;
import com.example.receiptas.R;
import com.example.receiptas.model.domain_model.Participant;
import com.example.receiptas.model.domain_model.Receipt;

import java.util.ArrayList;

public class ReceiptDetailSummaryFragment extends Fragment {

    private MainViewModel mainViewModel;
    private ListView listView;
    private TextView receipt_total, unassigned_total;
    private int receiptId;

    public ReceiptDetailSummaryFragment() {

    }

    public ReceiptDetailSummaryFragment(int receiptId) {
        this.receiptId = receiptId;
    }

    public static ReceiptDetailSummaryFragment newInstance(int receiptId) {
        ReceiptDetailSummaryFragment fragment = new ReceiptDetailSummaryFragment(receiptId);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        if(savedInstanceState != null){
            this.receiptId = savedInstanceState.getInt("receipt_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipt_detail_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.list_view);
        receipt_total = view.findViewById(R.id.receipt_total);
        unassigned_total = view.findViewById(R.id.unassigned_total);

        Receipt receipt = this.mainViewModel.getReceipts().getValue().get(this.receiptId);

        receipt_total.setText(getString(R.string.receipt_total, receipt.getTotalAmount(), receipt.getCurrency()));
        unassigned_total.setText(getString(R.string.unassigned_total, receipt.getUnassignedAmount(), receipt.getCurrency()));

        listView.setAdapter(new ParticipantAdapter(this.getContext(), R.layout.receipt_summary_participant, receipt));
    }
}