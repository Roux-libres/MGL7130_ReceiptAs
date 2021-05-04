package com.example.receiptas.ui.sharing;

import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.receiptas.R;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class SendReceiptFragment extends Fragment {

    private static final String UUID_KEY = "10011001-0000-1000-8000-00805f8b34fb";
    private static final int MESSAGE_WRITE = 1;
    private static final int MESSAGE_ERROR = 2;
    private static final int DEVICE_ERROR = 3;

    private String NAME;
    private UUID MY_UUID;



    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice selectedDevice;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;

    private SendReceiptViewModel mViewModel;
    private int receiptId;
    private Button bluetoothStateButton;
    private Button changeDeviceButton;
    private Button sendMessageButton;
    private TextView bluetoothStateTextView;
    private TextView bluetoothInformationSendingTextView;
    private TextView bluetoothInformationDeviceTextView;
    private ImageView bluetoothImage;

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

    private final Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case MESSAGE_WRITE:
                    bluetoothInformationSendingTextView.setText(R.string.bluetooth_information_sending_success);
                    sendMessageButton.setEnabled(false);
                    break;
                case MESSAGE_ERROR:
                    bluetoothInformationSendingTextView.setText(R.string.bluetooth_information_sending_error);
                    break;
                case DEVICE_ERROR:
                    bluetoothInformationSendingTextView.setText(R.string.bluetooth_information_device_error);
                    break;
            }
            return true;
        }
    });

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
        this.MY_UUID = UUID.fromString(UUID_KEY);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_send_receipt, container, false);
        this.bluetoothStateButton = root.findViewById(R.id.bt_state_button);
        this.changeDeviceButton = root.findViewById(R.id.change_bt_device_button);
        this.sendMessageButton = root.findViewById(R.id.send_message_button);
        this.bluetoothInformationDeviceTextView = root.findViewById(R.id.bt_information_device);
        this.bluetoothStateTextView = root.findViewById(R.id.bt_state_textview);
        this.bluetoothInformationSendingTextView = root.findViewById(R.id.bt_information_sending_textview);
        this.bluetoothImage = root.findViewById(R.id.bt_image);

        this.bluetoothInformationSendingTextView.setText(R.string.bluetooth_information_sending_init);
        this.bluetoothInformationDeviceTextView.setText(R.string.bluetooth_information_device_init);
        this.bluetoothStateButton.setText(R.string.bluetooth_button_activate);
        this.changeDeviceButton.setText(R.string.bluetooth_button_select);
        this.sendMessageButton.setText(R.string.bluetooth_button_send);
        this.sendMessageButton.setEnabled(false);
        this.bluetoothStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForBluetoothActivation();
            }
        });
        this.changeDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDevice();
            }
        });
        this.sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothInformationSendingTextView.setText(R.string.bluetooth_information_sending);
                createBluetoothThread();
            }
        });

        this.initBluetooth();

        return root;
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
        if(this.selectedDevice != null)
            this.sendMessageButton.setEnabled(true);
        this.changeDeviceButton.setEnabled(true);
        this.bluetoothImage.setImageResource(R.drawable.ic_baseline_bluetooth_150);
        this.bluetoothStateTextView.setText(R.string.bluetooth_state_on);
    }

    private void changeViewBluetoothDisabled() {
        this.bluetoothStateButton.setVisibility(View.VISIBLE);
        this.bluetoothStateButton.setEnabled(true);
        this.sendMessageButton.setEnabled(false);
        this.changeDeviceButton.setEnabled(false);
        this.bluetoothImage.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_150);
        this.bluetoothStateTextView.setText(R.string.bluetooth_state_off);
    }

    private void changeViewBluetoothUnavailable() {
        this.bluetoothStateButton.setVisibility(View.INVISIBLE);
        this.bluetoothStateButton.setEnabled(false);
        this.sendMessageButton.setEnabled(false);
        this.changeDeviceButton.setEnabled(false);
        this.bluetoothImage.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_150);
        this.bluetoothStateTextView.setText(R.string.bluetooth_state_unavailable);
        this.bluetoothInformationDeviceTextView.setText("");
        this.bluetoothInformationSendingTextView.setText("");
    }

    private void selectDevice() {
        Set<BluetoothDevice> bondedDevices = this.bluetoothAdapter.getBondedDevices();
        ArrayList<BluetoothDevice> devices = new ArrayList<>();
        String[] devicesName = new String[bondedDevices.size()];

        int index = 0;
        if (bondedDevices.size() > 0) {
            for (BluetoothDevice device : bondedDevices) {
                devices.add(device);
                devicesName[index] = device.getName();
                index++;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(R.string.bluetooth_dialog_title);

        builder.setItems(devicesName, (dialog, which) -> {
            selectedDevice = devices.get(which);
            bluetoothInformationDeviceTextView.setText(selectedDevice.getName());
            sendMessageButton.setEnabled(true);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createBluetoothThread() {
        this.deleteBluetoothThread();
        this.connectThread = new ConnectThread(this.selectedDevice);
        this.connectThread.start();
    }

    private void deleteBluetoothThread() {
        if (this.connectThread != null) {
            this.connectThread.cancel();
            this.connectThread.interrupt();
        }
        if (this.connectedThread != null) {
            this.connectedThread.cancel();
            this.connectedThread.interrupt();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.getActivity().unregisterReceiver(mReceiver);
        this.deleteBluetoothThread();
    }

    public void manageMyConnectedSocket(BluetoothSocket socket) {
        this.connectedThread = new ConnectedThread(socket);
        this.connectedThread.start();
        String receiptAsJson = this.mViewModel.getReceiptAsJsonString(this.receiptId);
        int size = receiptAsJson.getBytes().length;
        int numberOfChunks = 1 + size / 1024;
        receiptAsJson = String.valueOf(numberOfChunks) + receiptAsJson;
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
            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                this.cancel();
                Log.e("error", "Could not connect the client socket", connectException);
                Message deviceErrorMsg = handler.obtainMessage(DEVICE_ERROR);
                handler.sendMessage(deviceErrorMsg);
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
                mmOutStream.flush();
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

