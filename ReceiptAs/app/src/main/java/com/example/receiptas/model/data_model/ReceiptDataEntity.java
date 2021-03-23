package com.example.receiptas.model.data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;

public class ReceiptDataEntity {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("date")
    @Expose
    private Date date;

    @SerializedName("currency")
    @Expose
    private Currency currency;

    @SerializedName("items")
    @Expose
    private ArrayList<ItemDataEntity> items;

    @SerializedName("participants")
    @Expose
    private ArrayList<ParticipantDataEntity> participants;

    public ReceiptDataEntity(String name, Date date, Currency currency, ArrayList<ItemDataEntity> items, ArrayList<ParticipantDataEntity> participants) {
        this.name = name;
        this.date = date;
        this.date = date;
        this.currency = currency;
        this.items = items;
        this.participants = participants;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public Currency getCurrency() {
        return currency;
    }

    public ArrayList<ItemDataEntity> getItems() {
        return items;
    }

    public ArrayList<ParticipantDataEntity> getParticipants() {
        return participants;
    }
}
