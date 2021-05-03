package com.example.receiptas.ui.sharing;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
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

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.receiptas.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class SendReceiptFragment extends Fragment {


    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice selectedDevice;
    private BluetoothSocket bluetoothSocket;

    //Todo create function init
    private static final UUID MY_UUID = UUID.fromString("10011001-0000-1000-8000-00805f8b34fb");
    private final String NAME = getResources().getString(R.string.app_name);
    private static final int MESSAGE_WRITE = 1;
    private static final int MESSAGE_ERROR = 2;
    private static final int REQUEST_ENABLE_BT = 10;


    private SendReceiptViewModel mViewModel;
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

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.getActivity().registerReceiver(mReceiver, filter);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.STATE_OFF);
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                if(state == BluetoothAdapter.STATE_OFF)
                    changeViewBluetoothActivated();
                else if(state == BluetoothAdapter.STATE_ON)
                    changeViewBluetoothDisabled();
            }
        }
    };

    private final Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case MESSAGE_WRITE:
                    //TODO
                    System.out.println("Message envoyé" + message.obj);
                    break;
                case MESSAGE_ERROR:
                    //TODO
                    System.out.println("Erreur de l'envoi du message");
                    break;
            }
            return true;
        }
    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_send_receipt, container, false);

        this.initBluetooth();

        this.nfcButton = (Button) root.findViewById(R.id.activate_nfc_button);
        this.nfcTextView = (TextView) root.findViewById(R.id.nfc_textview);

        this.nfcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return root;
    }


    private void initBluetooth() {
        this.bluetoothAdapter =  BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                changeViewBluetoothActivated();
            } else {
                changeViewBluetoothDisabled();
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                this.getActivity().startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        } else {
            this.manageUnavailableBluetooth();
        }
    }

    private void changeViewBluetoothActivated() {
        this.nfcButton.setEnabled(true);
        this.nfcButton.setText(R.string.nfc_button_activate);
        this.nfcTextView.setText(R.string.nfc_disabled_description);
    }

    private void changeViewBluetoothDisabled() {
        this.nfcButton.setEnabled(false);
        this.nfcButton.setText(R.string.nfc_button_activated);
        this.nfcTextView.setText(R.string.nfc_activated_description);
    }

    public void manageUnavailableBluetooth() {
        //TODO
    }

    public void updateBoundedDevices() {
        Set<BluetoothDevice> bondedDevices = this.bluetoothAdapter.getBondedDevices();

        if(bondedDevices.size() > 0) {
            for (BluetoothDevice device: bondedDevices) {
                System.out.println(device.getName());
                //TODO Actualize list
            }

        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this.getActivity()).get(SendReceiptViewModel.class);

    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e("error", "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            bluetoothAdapter.cancelDiscovery();

            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                this.cancel();
                Log.e("error", "Could not connect the client socket", connectException);
                return;
            }

            bluetoothSocket = mmSocket;
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("error", "Could not close the client socket", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.

            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e("error", "Error occurred when creating output stream", e);
            }

            mmOutStream = tmpOut;
        }


        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);

                Message writtenMsg = handler.obtainMessage(
                        MESSAGE_WRITE, -1, -1, bytes);
                writtenMsg.sendToTarget();
            } catch (IOException e) {
                Log.e("error", "Error occurred when sending data", e);

                // Send a failure message back to the activity.
                Message writeErrorMsg = handler.obtainMessage(MESSAGE_ERROR);
                handler.sendMessage(writeErrorMsg);
            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("error", "Could not close the connect socket", e);
            }
        }
    }
}

