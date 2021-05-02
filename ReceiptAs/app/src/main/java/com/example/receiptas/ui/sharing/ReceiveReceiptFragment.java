package com.example.receiptas.ui.sharing;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.receiptas.R;

public class ReceiveReceiptFragment extends Fragment {

    private ReceiveReceiptViewModel mViewModel;
    private Button nfcButton;
    private TextView nfcTextView;

    public static ReceiveReceiptFragment newInstance() {
        return new ReceiveReceiptFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_receive_receipt, container, false);

        this.nfcButton = (Button) root.findViewById(R.id.activate_nfc_button);
        this.nfcTextView = (TextView) root.findViewById(R.id.nfc_textview);

        this.nfcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNFCSettings();
            }
        });

        return root;
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
    public void onResume() {
        super.onResume();
        Intent intent = this.getActivity().getIntent();

        Toast.makeText(getContext(), "NFC Message reception ready", Toast.LENGTH_LONG).show();

        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Toast.makeText(getContext(), "Receiving NFC message", Toast.LENGTH_LONG).show();

            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            this.nfcTextView.setText(new String(message.getRecords()[0].getPayload()));

        } else
            this.nfcTextView.setText("Waiting for NDEF Message");

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReceiveReceiptViewModel.class);
        // TODO: Use the ViewModel
    }

}