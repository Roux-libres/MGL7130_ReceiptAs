package com.example.receiptas.ui.sharing;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.NfcManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.receiptas.R;


public class SendReceiptFragment extends Fragment implements NfcAdapter.CreateNdefMessageCallback {


    private SendReceiptViewModel mViewModel;
    private NfcAdapter nfcAdapter;
    private Button nfcButton;
    private TextView nfcTextView;
    private int receiptId;


    public static SendReceiptFragment newInstance() {
        return new SendReceiptFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.receiptId = getArguments().getInt("receipt_id");

        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this.getContext());

        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED);
        this.getActivity().registerReceiver(mReceiver, filter);
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            final int state = intent.getIntExtra(NfcAdapter.EXTRA_ADAPTER_STATE,
                    NfcAdapter.STATE_OFF);
            if (action.equals(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED) && (state == NfcAdapter.STATE_OFF || state == NfcAdapter.STATE_ON)) {
                manageNFCState();
            }
        }
    };


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_send_receipt, container, false);

        this.nfcButton = (Button) root.findViewById(R.id.activate_nfc_button);
        this.nfcTextView = (TextView) root.findViewById(R.id.nfc_textview);

        if (this.nfcAdapter == null || (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)) {
            this.nfcTextView.setText(R.string.nfc_error_availability);
            this.nfcButton.setEnabled(false);
        } else {
            this.manageNFCState();
            nfcAdapter.setNdefPushMessageCallback(this, this.getActivity()); //TODO Change
            this.nfcButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openNFCSettings();
                }
            });
        }

        return root;
    }


    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String receiptInformation = this.mViewModel.getReceiptAsJsonString(this.receiptId);
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", receiptInformation.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }


    private void manageNFCState() {
        if (!this.nfcAdapter.isEnabled()) {
            this.nfcButton.setEnabled(true);
            this.nfcButton.setText(R.string.nfc_button_activate);
            this.nfcTextView.setText(R.string.nfc_disabled_description);
        } else {
            this.nfcButton.setEnabled(false);
            this.nfcButton.setText(R.string.nfc_button_activated);
            this.nfcTextView.setText(R.string.nfc_activated_description);

        }
    }


    private void openNFCSettings() {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            intent = new Intent(Settings.ACTION_NFC_SETTINGS);
        } else {
            intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        }
        startActivity(intent);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this.getActivity()).get(SendReceiptViewModel.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.getActivity().unregisterReceiver(mReceiver);
    }

}