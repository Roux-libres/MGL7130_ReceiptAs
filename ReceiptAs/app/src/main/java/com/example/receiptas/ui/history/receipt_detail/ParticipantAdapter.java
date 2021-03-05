package com.example.receiptas.ui.history.receipt_detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.receiptas.R;

import java.util.List;

public class ParticipantAdapter extends ArrayAdapter<String> {

    public ParticipantAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
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


        if(getItem(position).equals("Aurelien")) {
            LinearLayout layout = convertView.findViewById(R.id.participant_layout);
            layout.setBackground(getContext().getResources().getDrawable(R.drawable.receipt_summary_payer_background));
            participant.setText(getContext().getString(R.string.payer_name,getItem(position)));
        } else {
            participant.setText(getItem(position));
        }

        total.setText(getContext().getString(R.string.participant_total, 14.20, "CAD"));
        return convertView;
    }
}
