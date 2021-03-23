package com.example.receiptas.ui.scan_receipt.division;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.receiptas.R;
import com.example.receiptas.model.domain_model.Participant;
import com.example.receiptas.ui.scan_receipt.ScanReceiptViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class AddingParticipantsFragment extends Fragment {

    private ScanReceiptViewModel scanReceiptViewModel;
    private GridView gridNames;
    private ParticipantAdapter participantAdapter;

    public static AddingParticipantsFragment newInstance() {
        return new AddingParticipantsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        scanReceiptViewModel = new ViewModelProvider(getActivity()).get(ScanReceiptViewModel.class);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.validate, menu);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_division_adding_participants, container, false);

       TextView information_message = root.findViewById(R.id.information_message);
        information_message.setText(R.string.information_message_adding_participants);

        this.gridNames = root.findViewById(R.id.names_grid);
        ArrayList<String> names = new ArrayList<>();
        for(Participant participant: this.scanReceiptViewModel.getTheReceipt().getParticipants())
            names.add(participant.getName());
        this.participantAdapter = new ParticipantAdapter(this.getContext(), this.scanReceiptViewModel.getTheReceipt());

        gridNames.setAdapter(participantAdapter);
        gridNames.setVerticalScrollBarEnabled(false);
        gridNames.setEnabled(false);

        final TextInputLayout textInputLayout = root.findViewById(R.id.name_input_textfield);
        textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText editName = (TextInputEditText) root.findViewById(R.id.name_input_text);
                String inputText = editName.getText().toString();
                if (inputText.trim().length() > 0 && scanReceiptViewModel.getTheReceipt().getParticipants().size() < 8) {
                    scanReceiptViewModel.getTheReceipt().addParticipantByName(inputText);
                    editName.getText().clear();
                    participantAdapter.notifyDataSetChanged();
                }
            }
        });

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.validate_button) {
            AddingParticipantsFragmentDirections.ActionAddingParticipantsFragmentToItemDivision action =
                    AddingParticipantsFragmentDirections.actionAddingParticipantsFragmentToItemDivision();
            Navigation.findNavController(getView()).navigate(action);
            return true;
        } else {
            return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}