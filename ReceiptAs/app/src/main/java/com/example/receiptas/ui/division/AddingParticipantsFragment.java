package com.example.receiptas.ui.division;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;

import com.example.receiptas.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AddingParticipantsFragment extends Fragment {

    private AddingParticipantsViewModel mViewModel;
    private GridView gridNames;
    private NamesAdapter namesAdapter;

    public static AddingParticipantsFragment newInstance() {
        return new AddingParticipantsFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_division_adding_participants, container, false);

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

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddingParticipantsViewModel.class);
        // TODO: Use the ViewModel
    }


}