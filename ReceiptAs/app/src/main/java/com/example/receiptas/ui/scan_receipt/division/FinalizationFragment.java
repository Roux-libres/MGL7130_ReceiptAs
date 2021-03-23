package com.example.receiptas.ui.scan_receipt.division;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.receiptas.R;
import com.example.receiptas.model.domain_model.Participant;
import com.example.receiptas.model.domain_model.Receipt;
import com.example.receiptas.ui.history.receipt_detail.SummaryParticipantAdapter;
import com.example.receiptas.ui.scan_receipt.ScanReceiptViewModel;


public class FinalizationFragment extends Fragment {

    private ListView participantList;
    private TextView receipt_title, receipt_total, receipt_remaining;
    private ScanReceiptViewModel scanReceiptViewModel;

    public static FinalizationFragment newInstance(String param1, String param2) {
        return new FinalizationFragment();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.validate, menu);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        scanReceiptViewModel = new ViewModelProvider(getActivity()).get(ScanReceiptViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_finalization, container, false);

        TextView information_message = root.findViewById(R.id.information_message);
        information_message.setText(getContext().getString(R.string.information_message_choose_payer));


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        receipt_total = view.findViewById(R.id.receipt_total);
        receipt_title = view.findViewById(R.id.receipt_title);
        receipt_remaining = view.findViewById(R.id.receipt_remaining);

        Receipt receipt = this.scanReceiptViewModel.getTheReceipt();

        receipt_total.setText(getString(R.string.receipt_total, receipt.getTotalAmount(), receipt.getCurrency()));
        receipt_title.setText(receipt.getName());
        receipt_remaining.setText(getString(R.string.receipt_remaining, receipt.getUnassignedAmount(), receipt.getCurrency()));

        this.participantList = view.findViewById(R.id.participant_list);

        SummaryParticipantAdapter adapter = new SummaryParticipantAdapter(this.getContext(), R.layout.receipt_summary_participant, receipt);

        this.participantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Participant participant = receipt.getParticipantsPayerFirst().get(position);
                Participant payer = receipt.getPayer();

                if(payer == participant){
                    participant.setPayer(false);
                } else if (payer == null){
                    participant.setPayer(true);
                }

                participantList.setAdapter(new SummaryParticipantAdapter(getContext(), R.layout.receipt_summary_participant, receipt));
            }
        });

        this.participantList.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.validate_button) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}