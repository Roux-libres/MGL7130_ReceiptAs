package com.example.receiptas.ui.scan_receipt.division;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.receiptas.R;
import com.example.receiptas.model.domain_model.Participant;
import com.example.receiptas.model.domain_model.Receipt;

import java.util.ArrayList;

public class ParticipantAdapter extends BaseAdapter {

    private Context mContext;
    private Receipt receipt;
    private ArrayList<Participant> participants;
    private ArrayList<Integer> colorsParticipants;


    public ParticipantAdapter(Context mContext, Receipt receipt) {
        this.mContext = mContext;
        this.receipt = receipt;
        ArrayList<Participant> receiptParticipants = this.receipt.getParticipants();
        this.participants = (receiptParticipants == null) ? new ArrayList<Participant>(): receiptParticipants;
        this.colorsParticipants = new ArrayList<Integer>();

        Resources res = mContext.getResources();
        TypedArray colors = res.obtainTypedArray(R.array.colors_participants);
        for (int i = 0; i < colors.length(); ++i) {
            int id = colors.getResourceId(i, 0);
            if (id > 0) {
                colorsParticipants.add(res.getColor(id));
            }
        }
    }

    @Override
    public int getCount() {
        return this.participants.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.division_name_item, null);
        }

        TextView nameItemText = (TextView) convertView.findViewById(R.id.name_item_text);
        nameItemText.setText(this.participants.get(position).getName());

        FrameLayout nameItemColor = (FrameLayout) convertView.findViewById(R.id.name_item_color);
        nameItemColor.setBackgroundTintList(ColorStateList.valueOf(colorsParticipants.get(position)));

        FrameLayout removeNameIcon = (FrameLayout) convertView.findViewById(R.id.remove_name_icon);
        removeNameIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receipt.removeParticipant(participants.get(position));
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
