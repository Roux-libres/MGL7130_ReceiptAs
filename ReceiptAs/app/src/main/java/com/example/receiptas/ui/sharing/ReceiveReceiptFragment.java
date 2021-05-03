package com.example.receiptas.ui.sharing;

import androidx.lifecycle.ViewModelProvider;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.receiptas.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ReceiveReceiptFragment extends Fragment {

    private UUID MY_UUID = UUID.fromString("10011001-0000-1000-8000-00805f8b34fb");
    private String NAME = getResources().getString(R.string.app_name);
    private static final int MESSAGE_READ = 0;
    private static final int REQUEST_ENABLE_BT = 10;


    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;

    private ReceiveReceiptViewModel mViewModel;
    private Button nfcButton;
    private TextView nfcTextView;



    public static ReceiveReceiptFragment newInstance() {
        return new ReceiveReceiptFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                case MESSAGE_READ:
                    //TODO
                    System.out.println("Message recu" + message.arg1);
                    break;
            }
            return true;
        }
    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_receive_receipt, container, false);

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


    @Override
    public void onResume() {
        super.onResume();

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mViewModel = new ViewModelProvider(this.getActivity()).get(ReceiveReceiptViewModel.class);
    }


    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                Log.e("error", "Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e("error", "Socket's accept() method failed", e);
                    break;
                }

                if (socket != null) {
                    // A connection was accepted. Perform work associated with
                    // the connection in a separate thread.
                    bluetoothSocket = socket;
                    this.cancel();
                    break;
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e("error", "Could not close the connect socket", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;

            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e("error", "Error occurred when creating input stream", e);
            }
            mmInStream = tmpIn;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes;

            while (true) {
                try {
                    numBytes = mmInStream.read(mmBuffer);
                    Message readMsg = handler.obtainMessage(
                            MESSAGE_READ, numBytes, -1,
                            mmBuffer);
                    readMsg.sendToTarget();
                } catch (IOException e) {
                    Log.d("error", "Input stream was disconnected", e);
                    break;
                }
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("error", "Could not close the connect socket", e);
            }
        }
    }
}

