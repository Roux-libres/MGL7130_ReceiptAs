package com.example.receiptas.ui.sharing;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    private String NFC_ERROR_AVAILABILITY = "The phone does not have an NFC service. Please return to the receipt.";
    private NfcAdapter nfcAdapter;
    private Button nfcButton;
    private TextView nfcTextView;
    private boolean nfcIsAvailable;


    public static SendReceiptFragment newInstance() {
        return new SendReceiptFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this.getContext());
        if (this.nfcAdapter == null || (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)) {
            this.getCustomAlertDialog(this.NFC_ERROR_AVAILABILITY);
            nfcIsAvailable = false;
            return;
        } else {
            nfcAdapter.setNdefPushMessageCallback(this, this.getActivity());
            Toast.makeText(getContext(), "NFC Message ready", Toast.LENGTH_LONG).show();
            nfcIsAvailable = true;
        }
        //TODO Créer un observeur sur le changement d'état du NFC avec NfcAdapter.ACTION_ADAPTER_STATE_CHANGED et le principe de broadcastreceiver
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_send_receipt, container, false);

        this.nfcButton = (Button) root.findViewById(R.id.activate_nfc_button);
        this.nfcTextView = (TextView) root.findViewById(R.id.nfc_textview);


        if(this.nfcIsAvailable) {
            this.manageNFCState();
            this.nfcButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openNFCSettings();
                }
            });
        } else {
            this.nfcTextView.setText(R.string.nfc_error_availability);
            this.nfcButton.setEnabled(false);
        }

        return root;
    }


    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String receiptInformation = "Ceci est un recu";
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", receiptInformation.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }

    private void manageNFCState() {
        if (!this.nfcAdapter.isEnabled()) {
            this.getDialogForUserToActivateNFC().show();
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

    private Dialog getDialogForUserToActivateNFC() {
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage("The NFC is required to send a receipt. Would you like to activate it?");
        builder.setCancelable(false);
        builder.setTitle("NFC Activation");

        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        openNFCSettings();
                    }
                });

        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        dialog = builder.create();
        return dialog;
    }

    private Dialog getCustomAlertDialog(String message) {
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setTitle("Information");

        builder.setNeutralButton("Ok",  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        dialog = builder.create();
        return dialog;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SendReceiptViewModel.class);
        // TODO: Use the ViewModel
    }


}