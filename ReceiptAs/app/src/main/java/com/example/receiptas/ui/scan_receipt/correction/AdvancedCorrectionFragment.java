package com.example.receiptas.ui.scan_receipt.correction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receiptas.R;
import com.example.receiptas.ui.history.OnRecyclerViewItemClickListener;

import java.util.ArrayList;

public class AdvancedCorrectionFragment extends Fragment {

    private ReceiptCorrectionViewModel receiptCorrectionViewModel;
    private RecyclerView correctionRecyclerView;

    public AdvancedCorrectionFragment(ReceiptCorrectionViewModel receiptCorrectionViewModel) {
        this.receiptCorrectionViewModel = receiptCorrectionViewModel;
    }

    public static AdvancedCorrectionFragment newInstance(ReceiptCorrectionViewModel receiptCorrectionViewModel) {
        return new AdvancedCorrectionFragment(receiptCorrectionViewModel);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_advanced_correction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.correctionRecyclerView = view.findViewById(R.id.correction_recycler_view);

        TextView informationMessage = view.findViewById(R.id.information_message);
        informationMessage.setText(getResources().getString(R.string.information_message_advanced_correction));

        this.configureRecyclerView();
        this.receiptCorrectionViewModel.getCorrectedItems().observe(this.getViewLifecycleOwner(), correctedItemObserver);
        this.receiptCorrectionViewModel.getPrices().observe(this.getViewLifecycleOwner(), this.pricesObserver);
    }

    private void configureRecyclerView() {
        this.correctionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private final OnRecyclerViewItemClickListener<String> onItemClick  = (itemId, item) -> {
        //TODO ouvrir modal modif
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        CorrectionDialogFragment newFragment = CorrectionDialogFragment.newInstance(itemId, this.receiptCorrectionViewModel);
        newFragment.show(fragmentManager, "dialog");
    };

    private final Observer<ArrayList<String>> correctedItemObserver =
        new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> correctedItems) {
                correctionRecyclerView.setAdapter(new CorrectionAdapter(
                    correctedItems,
                    receiptCorrectionViewModel.getPrices().getValue(),
                    onItemClick,
                    getContext(),
                    receiptCorrectionViewModel.getFormatter()
                ));
            }
        };

    private final Observer<ArrayList<String>> pricesObserver =
        new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> prices) {
                correctionRecyclerView.setAdapter(new CorrectionAdapter(
                    receiptCorrectionViewModel.getCorrectedItems().getValue(),
                    prices,
                    onItemClick,
                    getContext(),
                    receiptCorrectionViewModel.getFormatter()
                ));
            }
        };
}