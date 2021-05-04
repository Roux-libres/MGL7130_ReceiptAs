package com.example.receiptas.ui.scan_receipt.correction;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.receiptas.R;
import com.google.android.material.textfield.TextInputEditText;

public class CorrectionDialogFragment extends DialogFragment {
    private ReceiptCorrectionViewModel receiptCorrectionViewModel;
    private int itemIndex;
    private TextInputEditText inputItemLabel;
    private EditText inputItemPrice;
    private Button validateButton;

    public CorrectionDialogFragment(int itemIndex, ReceiptCorrectionViewModel receiptCorrectionViewModel) {
        this.receiptCorrectionViewModel = receiptCorrectionViewModel;
        this.itemIndex = itemIndex;
    }

    public static CorrectionDialogFragment newInstance(int itemIndex, ReceiptCorrectionViewModel receiptCorrectionViewModel) {
        return new CorrectionDialogFragment(itemIndex, receiptCorrectionViewModel);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_correction_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle(R.string.correction_modal_title);

        this.inputItemLabel = view.findViewById(R.id.input_item_label);
        this.inputItemPrice = view.findViewById(R.id.input_item_price);
        this.validateButton = view.findViewById(R.id.validate_correction);

        this.validateButton.setOnClickListener(v -> {
            receiptCorrectionViewModel.changeLabel(this.itemIndex, this.inputItemLabel.getText().toString());
            receiptCorrectionViewModel.changePrice(this.itemIndex, this.inputItemPrice.getText().toString());
            this.dismiss();
        });

        this.inputItemLabel.setText(receiptCorrectionViewModel.getCorrectedItems().getValue().get(this.itemIndex));
        this.inputItemPrice.setText(receiptCorrectionViewModel.getPrices().getValue().get(this.itemIndex));
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        int displayHeight = displayMetrics.heightPixels;

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getDialog().getWindow().getAttributes());
        layoutParams.width = displayWidth * 80 / 100;
        layoutParams.height = displayHeight * 80 / 100;
        getDialog().getWindow().setAttributes(layoutParams);
    }
}