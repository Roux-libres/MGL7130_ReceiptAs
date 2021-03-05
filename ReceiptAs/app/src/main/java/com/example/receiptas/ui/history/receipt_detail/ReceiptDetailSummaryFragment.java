package com.example.receiptas.ui.history.receipt_detail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.receiptas.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReceiptDetailSummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReceiptDetailSummaryFragment extends Fragment {

    private ListView listView;
    private TextView receipt_total, unassigned_total;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReceiptDetailSummaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReceiptDetailSummaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReceiptDetailSummaryFragment newInstance(String param1, String param2) {
        ReceiptDetailSummaryFragment fragment = new ReceiptDetailSummaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receipt_detail_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.list_view);
        receipt_total = view.findViewById(R.id.receipt_total);
        unassigned_total = view.findViewById(R.id.unassigned_total);

        receipt_total.setText(getString(R.string.receipt_total, 15.2, "CAD"));
        unassigned_total.setText(getString(R.string.unassigned_total, 4.00, "CAD"));

        ArrayList<String> strings = new ArrayList<>();
        strings.add("Aurelien");
        strings.add("Romain");
        strings.add("Nelson");

        listView.setAdapter(new ParticipantAdapter(this.getContext(), R.layout.receipt_summary_participant, strings));
    }
}