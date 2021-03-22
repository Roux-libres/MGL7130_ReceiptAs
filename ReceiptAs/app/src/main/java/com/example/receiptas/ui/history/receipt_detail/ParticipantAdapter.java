package com.example.receiptas.ui.history.receipt_detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.receiptas.R;
import com.example.receiptas.model.domain_model.Participant;
import com.example.receiptas.model.domain_model.Receipt;

import java.util.List;

public class ParticipantAdapter extends ArrayAdapter<Participant> {

    private Receipt receipt;

    public ParticipantAdapter(@NonNull Context context, int resource, @NonNull Receipt receipt) {
        super(context, resource, receipt.getParticipantsPayerFirst());
        this.receipt = receipt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.receipt_summary_participant, parent, false);
        } else {
            //do nothing
        }

        TextView participant = convertView.findViewById(R.id.leftTextView);
        TextView total = convertView.findViewById(R.id.rightTextView);

        LinearLayout layout = convertView.findViewById(R.id.participant_layout);
        if(getItem(position).isPayer()) {
            layout.setBackground(getContext().getResources().getDrawable(R.drawable.receipt_summary_payer_background));
            participant.setText(getContext().getString(R.string.payer_name, getItem(position).getName()));
        } else {
            layout.setBackground(getContext().getResources().getDrawable(R.drawable.name_added_item_background));
            participant.setText(getItem(position).getName());
        }

        total.setText(getContext().getString(R.string.participant_total,
                receipt.getParticipantTotal(getItem(position)),
                this.receipt.getCurrency()));
        return convertView;
    }
}
