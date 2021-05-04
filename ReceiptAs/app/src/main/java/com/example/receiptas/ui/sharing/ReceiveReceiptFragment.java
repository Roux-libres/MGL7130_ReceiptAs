package com.example.receiptas.ui.sharing;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.receiptas.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class ReceiveReceiptFragment extends Fragment {

    private static final int MESSAGE_READ = 0;
    private static final String UUID_KEY = "10011001-0000-1000-8000-00805f8b34fb";

    private UUID MY_UUID;
    private String NAME;

    private BluetoothAdapter bluetoothAdapter;
    private AcceptThread acceptThread;
    private ConnectedThread connectedThread;

    private ReceiveReceiptViewModel mViewModel;
    private Button bluetoothStateButton;
    private TextView bluetoothStateTextView;
    private TextView bluetoothInformationTextView;
    private ImageView bluetoothImage;

    private final Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case MESSAGE_READ:
                    String readMessage = new String((byte[]) message.obj, 0, message.arg1);
                    String receiptTitle = mViewModel.addReceiptFromJsonString(readMessage);
                    if (receiptTitle != null) {
                        bluetoothInformationTextView.setText(getString(R.string.bluetooth_information_reception) + " " + receiptTitle);
                    } else {
                        bluetoothInformationTextView.setText(R.string.bluetooth_information_reception_error);
                    }
                    break;
            }
            return true;
        }
    });
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.STATE_OFF);
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                if (state == BluetoothAdapter.STATE_OFF) {
                    changeViewBluetoothDisabled();
                    deleteBluetoothThread();
                    askForBluetoothActivation();
                } else if (state == BluetoothAdapter.STATE_ON) {
                    changeViewBluetoothActivated();
                    createBluetoothThread();
                }

            }
        }
    };

    public static ReceiveReceiptFragment newInstance() {
        return new ReceiveReceiptFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.getActivity().registerReceiver(mReceiver, filter);
        this.NAME = getResources().getString(R.string.app_name);
        this.MY_UUID = UUID.fromString(UUID_KEY);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_receive_receipt, container, false);
        this.bluetoothStateButton = root.findViewById(R.id.bt_state_button);
        this.bluetoothInformationTextView = root.findViewById(R.id.bt_information_device);
        this.bluetoothStateTextView = root.findViewById(R.id.bt_state_textview);
        this.bluetoothImage = root.findViewById(R.id.bt_image);

        this.bluetoothStateButton.setText(R.string.bluetooth_button_activate);
        this.bluetoothInformationTextView.setText(R.string.bluetooth_information_wait_reception);
        this.bluetoothStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForBluetoothActivation();
            }
        });

        this.initBluetooth();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.createBluetoothThread();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.deleteBluetoothThread();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.getActivity().unregisterReceiver(mReceiver);
    }

    private void createBluetoothThread() {
        this.deleteBluetoothThread();
        if (this.bluetoothAdapter.isEnabled()) {
            this.acceptThread = new AcceptThread();
            this.acceptThread.start();
        }
    }

    private void deleteBluetoothThread() {
        if (this.acceptThread != null) {
            this.acceptThread.cancel();
            this.acceptThread.interrupt();
        }
        if (this.connectedThread != null) {
            this.connectedThread.cancel();
            this.connectedThread.interrupt();
        }
    }

    private void askForBluetoothActivation() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        this.getActivity().startActivityForResult(enableBtIntent, 10);
    }

    private void initBluetooth() {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                changeViewBluetoothActivated();
            } else {
                changeViewBluetoothDisabled();
                askForBluetoothActivation();
            }
        } else {
            this.changeViewBluetoothUnavailable();
        }
    }

    private void changeViewBluetoothActivated() {
        this.bluetoothStateButton.setVisibility(View.INVISIBLE);
        this.bluetoothStateButton.setEnabled(false);
        this.bluetoothImage.setImageResource(R.drawable.ic_baseline_bluetooth_150);
        this.bluetoothStateTextView.setText(R.string.bluetooth_state_on);
    }

    private void changeViewBluetoothDisabled() {
        this.bluetoothStateButton.setVisibility(View.VISIBLE);
        this.bluetoothStateButton.setEnabled(true);
        this.bluetoothImage.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_150);
        this.bluetoothStateTextView.setText(R.string.bluetooth_state_off);
    }

    public void changeViewBluetoothUnavailable() {
        this.bluetoothStateButton.setVisibility(View.INVISIBLE);
        this.bluetoothStateButton.setEnabled(false);
        this.bluetoothImage.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_150);
        this.bluetoothStateTextView.setText(R.string.bluetooth_state_unavailable);
        this.bluetoothInformationTextView.setText("");
    }

    public void manageMyConnectedSocket(BluetoothSocket socket) {
        this.connectedThread = new ConnectedThread(socket);
        this.connectedThread.start();
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
                    manageMyConnectedSocket(socket);
                    this.cancel();
                    break;
                }
            }
        }

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

