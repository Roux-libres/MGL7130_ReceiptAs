package com.example.receiptas.ui.sharing;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


public class SendReceiptFragment extends Fragment {

    private static final UUID MY_UUID = UUID.fromString("10011001-0000-1000-8000-00805f8b34fb");
    private static final int MESSAGE_WRITE = 1;
    private static final int MESSAGE_ERROR = 2;
    private static final int REQUEST_ENABLE_BT = 10;
    private final Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case MESSAGE_WRITE:
                    //TODO
                    System.out.println("Message envoye" + message.obj);
                    break;
                case MESSAGE_ERROR:
                    //TODO
                    System.out.println("Erreur de l'envoi du message");
                    break;
            }
            return true;
        }
    });
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice selectedDevice;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;
    private String NAME;
    private SendReceiptViewModel mViewModel;
    private Button nfcButton;
    private TextView nfcTextView;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.STATE_OFF);
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                if (state == BluetoothAdapter.STATE_OFF) {
                    changeViewBluetoothDisabled();
                    askForBluetoothActivation();
                } else if (state == BluetoothAdapter.STATE_ON)
                    changeViewBluetoothActivated();
            }
        }
    };
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
        this.NAME = getResources().getString(R.string.app_name);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_send_receipt, container, false);

        this.nfcButton = root.findViewById(R.id.send_message_button);
        this.nfcTextView = root.findViewById(R.id.bt_information_textview);

        this.initBluetooth();

        this.nfcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDevice();
            }
        });

        return root;
    }

    private void askForBluetoothActivation() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        this.getActivity().startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    private void initBluetooth() {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                this.changeViewBluetoothActivated();
            } else {
                this.changeViewBluetoothDisabled();
                this.askForBluetoothActivation();
            }
        } else {
            this.manageUnavailableBluetooth();
        }
    }

    private void changeViewBluetoothActivated() {
        this.nfcButton.setEnabled(true);
      //  this.nfcButton.setText(R.string.nfc_button_activated);
        //this.nfcTextView.setText(R.string.nfc_activated_description);
    }

    private void changeViewBluetoothDisabled() {
        this.nfcButton.setEnabled(false);
      //  this.nfcButton.setText(R.string.nfc_button_activated);
      //  this.nfcTextView.setText(R.string.nfc_disabled_description);
    }

    private void manageUnavailableBluetooth() {
        //TODO
    }

    private void selectDevice() {
        this.updateBoundedDevices();
        this.selectedDevice = (BluetoothDevice) this.bluetoothAdapter.getBondedDevices().toArray()[8];
        System.out.println(this.selectedDevice.getName());
        this.connectThread = new ConnectThread(this.selectedDevice);
        this.connectThread.start();
    }

    private void updateBoundedDevices() {
        Set<BluetoothDevice> bondedDevices = this.bluetoothAdapter.getBondedDevices();

        if (bondedDevices.size() > 0) {
            for (BluetoothDevice device : bondedDevices) {
                System.out.println(device.getName());
                //TODO Actualize list
            }

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (this.connectThread != null) {
            this.connectThread.cancel();
            this.connectThread.interrupt();
        }
        if (this.connectedThread != null) {
            this.connectedThread.cancel();
            this.connectedThread.interrupt();
        }
    }

    public void manageMyConnectedSocket(BluetoothSocket socket) {
        this.connectedThread = new ConnectedThread(socket);
        this.connectedThread.start();
        String receiptAsJson = this.mViewModel.getReceiptAsJsonString(this.receiptId);
        this.connectedThread.write(receiptAsJson.getBytes());
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
            //   bluetoothAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                this.cancel();
                Log.e("error", "Could not connect the client socket", connectException);
                return;
            }

            manageMyConnectedSocket(mmSocket);
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

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("error", "Could not close the connect socket", e);
            }
        }
    }
}

