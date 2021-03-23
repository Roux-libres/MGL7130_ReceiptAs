package com.example.receiptas.ui.division;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.receiptas.R;
import com.example.receiptas.ui.scan_receipt.ScanReceiptViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ItemDivisionFragment extends Fragment {

    private ItemDivisionViewModel itemDivisionViewModel;
    private RecyclerView itemRecyclerView;
    private ItemDivisionAdapter itemDivisionAdapter;
    private ScanReceiptViewModel scanReceiptViewModel;

    public static ItemDivisionFragment newInstance() {
        return new ItemDivisionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scanReceiptViewModel = new ViewModelProvider(getActivity()).get(ScanReceiptViewModel.class);
        itemDivisionViewModel = new ViewModelProvider(this).get(ItemDivisionViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_item_divison, container, false);

        int participantIndex = getArguments().getInt("participantIndex");

        TextView information_message = root.findViewById(R.id.information_message);
        String message = getContext().getString(R.string.information_message_division_participant) + " ";
        information_message.setText(scanReceiptViewModel.getReceipt().getParticipants().get(participantIndex).getName() + message);

        FloatingActionButton validation = root.findViewById(R.id.validate_button);
        validation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nextParticipantIndex = participantIndex + 1;

                if(scanReceiptViewModel.getReceipt().getParticipants().size() > nextParticipantIndex) {
                    ItemDivisionFragmentDirections.ActionItemDivisionSelf action =  ItemDivisionFragmentDirections.actionItemDivisionSelf();
                    action.setParticipantIndex(nextParticipantIndex);
                    Navigation.findNavController(view).navigate(action);
                } else {
                    System.out.println("C'est la fin frr");
                }
            } });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.itemRecyclerView = view.findViewById(R.id.item_recycler_view);
        this.configureRecyclerView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    private void configureRecyclerView() {
        itemDivisionAdapter = new ItemDivisionAdapter(scanReceiptViewModel.getReceipt(), getContext());
        itemRecyclerView.setAdapter(itemDivisionAdapter);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


}