package com.example.receiptas.ui.scan_receipt.division;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.receiptas.MainViewModel;
import com.example.receiptas.R;
import com.example.receiptas.model.domain_model.Participant;
import com.example.receiptas.model.domain_model.Receipt;
import com.example.receiptas.ui.history.receipt_detail.SummaryParticipantAdapter;
import com.example.receiptas.ui.scan_receipt.ScanReceiptViewModel;

import java.util.ArrayList;


public class FinalizationFragment extends Fragment {

    private ListView participantList;
    private TextView receipt_title, receipt_total, receipt_remaining;
    private ScanReceiptViewModel scanReceiptViewModel;
    private MainViewModel mainViewModel;
    private View.OnClickListener onValidateReceipt;

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
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        if(this.scanReceiptViewModel.getReceipt().getPayer() != null) {
            onValidateReceipt = onValidateReceiptWithPayer;
        } else {
            onValidateReceipt = onValidateReceiptWithoutPayer;
        }
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

        Receipt receipt = this.scanReceiptViewModel.getReceipt();

        receipt_total.setText(getString(R.string.receipt_total, receipt.getTotalAmount(), receipt.getCurrency()));
        receipt_title.setText(receipt.getName());
        receipt_remaining.setText(getString(R.string.receipt_remaining, receipt.getUnassignedAmount(), receipt.getCurrency()));

        this.participantList = view.findViewById(R.id.participant_list);

        SummaryParticipantAdapter adapter = new SummaryParticipantAdapter(this.getContext(), R.layout.receipt_summary_participant, receipt);

        this.participantList.setOnItemClickListener((parent, view1, position, id) -> {
            Participant participant = receipt.getParticipantsPayerFirst().get(position);
            Participant payer = receipt.getPayer();

            if(payer == participant){
                participant.setPayer(false);
                this.onValidateReceipt = onValidateReceiptWithoutPayer;
            } else if (payer == null){
                participant.setPayer(true);
                this.onValidateReceipt = onValidateReceiptWithPayer;
            }

            participantList.setAdapter(new SummaryParticipantAdapter(getContext(), R.layout.receipt_summary_participant, receipt));
        });

        this.participantList.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.validate_button) {
            this.onValidateReceipt.onClick(getView());
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private final View.OnClickListener onValidateReceiptWithPayer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ArrayList<Receipt> receipts = new ArrayList<>(mainViewModel.getReceipts().getValue());
            receipts.add(scanReceiptViewModel.getReceipt());
            mainViewModel.getReceipts().setValue(receipts);

            NavDirections action = FinalizationFragmentDirections.showNewReceiptDetails(
                receipts.size() - 1,
                receipts.get(receipts.size() - 1).getName()
            );
            Navigation.findNavController(getView()).navigate(action);
        }
    };

    private final View.OnClickListener onValidateReceiptWithoutPayer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder blockingDialog = new AlertDialog.Builder(getContext());
            blockingDialog.setTitle(R.string.scan_receipt_missing_payer_title);
            blockingDialog.setMessage(R.string.scan_receipt_missing_payer_description);
            blockingDialog.setPositiveButton(R.string.dialog_positive, null);
            blockingDialog.create().show();
        }
    };
}