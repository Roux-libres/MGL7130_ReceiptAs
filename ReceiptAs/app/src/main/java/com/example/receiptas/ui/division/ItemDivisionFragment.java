package com.example.receiptas.ui.division;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.receiptas.R;
import com.example.receiptas.model.domain_model.Receipt;
import com.example.receiptas.ui.history.HistoryFragmentDirections;
import com.example.receiptas.ui.history.OnRecyclerViewItemClickListener;
import com.example.receiptas.ui.history.ReceiptAdapter;
import com.example.receiptas.ui.scan_receipt.ScanReceiptViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ItemDivisionFragment extends Fragment {

    private RecyclerView itemRecyclerView;
    private ItemDivisionAdapter itemDivisionAdapter;
    private ScanReceiptViewModel scanReceiptViewModel;
    private int participantIndex;

    public static ItemDivisionFragment newInstance() {
        return new ItemDivisionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        scanReceiptViewModel = new ViewModelProvider(getActivity()).get(ScanReceiptViewModel.class);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.validate, menu);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_item_divison, container, false);

        this.participantIndex = getArguments().getInt("participantIndex");

        TextView information_message = root.findViewById(R.id.information_message);
        String message = getContext().getString(R.string.information_message_division_participant);
        information_message.setText(String.format("%s%s", scanReceiptViewModel.getTheReceipt().getParticipants().get(participantIndex).getName(), message));

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.validate_button) {
            int nextParticipantIndex = participantIndex + 1;

            if(scanReceiptViewModel.getTheReceipt().getParticipants().size() > nextParticipantIndex) {
                ItemDivisionFragmentDirections.ActionItemDivisionSelf action =  ItemDivisionFragmentDirections.actionItemDivisionSelf();
                action.setParticipantIndex(nextParticipantIndex);
                Navigation.findNavController(getView()).navigate(action);
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);

        }
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
        itemDivisionAdapter = new ItemDivisionAdapter(scanReceiptViewModel.getTheReceipt(), getContext(), scanReceiptViewModel.getTheReceipt().getParticipants().get(this.participantIndex));
        itemRecyclerView.setAdapter(itemDivisionAdapter);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}