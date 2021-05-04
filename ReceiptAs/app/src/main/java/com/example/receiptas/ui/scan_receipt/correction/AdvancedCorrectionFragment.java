package com.example.receiptas.ui.scan_receipt.correction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receiptas.R;
import com.example.receiptas.ui.history.OnRecyclerViewItemClickListener;

import java.util.ArrayList;

public class AdvancedCorrectionFragment extends Fragment {

    private final ItemCorrectionViewModel itemCorrectionViewModel;
    private RecyclerView correctionRecyclerView;

    public AdvancedCorrectionFragment(ItemCorrectionViewModel itemCorrectionViewModel) {
        this.itemCorrectionViewModel = itemCorrectionViewModel;
    }

    public static AdvancedCorrectionFragment newInstance(ItemCorrectionViewModel itemCorrectionViewModel) {
        return new AdvancedCorrectionFragment(itemCorrectionViewModel);
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

        this.configureRecyclerView();
        this.itemCorrectionViewModel.getCorrectableItems().observe(this.getViewLifecycleOwner(), correctableItemObserver);
        this.itemCorrectionViewModel.getPrices().observe(this.getViewLifecycleOwner(), this.pricesObserver);
    }

    private void configureRecyclerView() {
        this.correctionRecyclerView.setAdapter(
            new CorrectionAdapter(
                itemCorrectionViewModel.getCorrectableItems().getValue(),
                itemCorrectionViewModel.getPrices().getValue(),
                onItemClick,
                getContext()
            )
        );
        this.correctionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private final OnRecyclerViewItemClickListener<ItemCorrectionViewModel.CorrectableItem> onItemClick  = (itemId, item) -> {
        //TODO ouvrir modal modif
    };

    private final Observer<ArrayList<ItemCorrectionViewModel.CorrectableItem>> correctableItemObserver =
        new Observer<ArrayList<ItemCorrectionViewModel.CorrectableItem>>() {
            @Override
            public void onChanged(ArrayList<ItemCorrectionViewModel.CorrectableItem> correctableItems) {
                correctionRecyclerView.setAdapter(new CorrectionAdapter(
                    correctableItems,
                    itemCorrectionViewModel.getPrices().getValue(),
                    onItemClick,
                    getContext()
                ));
            }
        };

    private final Observer<ArrayList<String>> pricesObserver =
        new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> prices) {
                correctionRecyclerView.setAdapter(new CorrectionAdapter(
                    itemCorrectionViewModel.getCorrectableItems().getValue(),
                    prices,
                    onItemClick,
                    getContext()
                ));
            }
        };
}