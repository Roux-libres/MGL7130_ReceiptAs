package com.example.receiptas.ui.division;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.receiptas.R;
import com.example.receiptas.ui.scan_receipt.ScanReceiptViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AddingParticipantsFragment extends Fragment {

    private ScanReceiptViewModel scanReceiptViewModel;
    private GridView gridNames;
    private NamesAdapter namesAdapter;

    public static AddingParticipantsFragment newInstance() {
        return new AddingParticipantsFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scanReceiptViewModel = new ViewModelProvider(getActivity()).get(ScanReceiptViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_division_adding_participants, container, false);

       TextView information_message = root.findViewById(R.id.information_message);
        information_message.setText(R.string.information_message_adding_participants);

        this.gridNames = root.findViewById(R.id.names_grid);
        this.namesAdapter = new NamesAdapter(this.getContext(), null);

        gridNames.setAdapter(namesAdapter);
        gridNames.setVerticalScrollBarEnabled(false);
        gridNames.setEnabled(false);

        final TextInputLayout textInputLayout = root.findViewById(R.id.name_input_textfield);
        textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText editName = (TextInputEditText) root.findViewById(R.id.name_input_text);
                String inputText = editName.getText().toString();
                if (inputText.trim().length() > 0 && namesAdapter.addName(inputText)) {
                    editName.getText().clear();
                    namesAdapter.notifyDataSetChanged();
                }
            }
        });

        FloatingActionButton validation = root.findViewById(R.id.validate_button);
        validation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(saveParticipants()) {
                    AddingParticipantsFragmentDirections.ActionAddingParticipantsFragmentToItemDivision action =
                            AddingParticipantsFragmentDirections.actionAddingParticipantsFragmentToItemDivision();
                    Navigation.findNavController(view).navigate(action);
                }
            } });

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private boolean saveParticipants() {
        ArrayList<String> names = this.namesAdapter.getNames();
        if(names.size() > 0) {
            for(String name: names) {
                scanReceiptViewModel.getTheReceipt().addParticipantByName(name);
            }
            return true;
        }
        return false;
    }
}