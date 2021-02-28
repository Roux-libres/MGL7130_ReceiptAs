package com.example.receiptas.ui.scan_receipt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.receiptas.R;
import com.google.android.material.textfield.TextInputLayout;

public class ScanReceiptFragment extends Fragment {

    private ScanReceiptViewModel scanReceiptViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scanReceiptViewModel = new ViewModelProvider(this).get(ScanReceiptViewModel.class);
        View root = inflater.inflate(R.layout.fragment_scan_receipt, container, false);

        final TextView textView = root.findViewById(R.id.text_scan_receipt);
        scanReceiptViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.currencies_array, R.layout.list_item);
        AutoCompleteTextView autoCompleteTextView = root.findViewById(R.id.currency_menu_text_view);
        autoCompleteTextView.setText(adapter.getItem(0), false);
        autoCompleteTextView.setAdapter(adapter);

        return root;
    }
}