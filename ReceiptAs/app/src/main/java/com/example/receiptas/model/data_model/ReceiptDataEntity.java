package com.example.receiptas.model.data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

public class ReceiptDataEntity {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("date")
    @Expose
    private Date date;

    @SerializedName("items")
    @Expose
    private ArrayList<ItemDataEntity> items;

    @SerializedName("participants")
    @Expose
    private ArrayList<ParticipantDataEntity> participants;

    public ReceiptDataEntity(int id, String name, Date date, ArrayList<ItemDataEntity> items, ArrayList<ParticipantDataEntity> participants) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.items = items;
        this.participants = participants;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public ArrayList<ItemDataEntity> getItems() {
        return items;
    }

    public ArrayList<ParticipantDataEntity> getParticipants() {
        return participants;
    }
}
