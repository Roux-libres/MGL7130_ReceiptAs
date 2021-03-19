package com.example.receiptas.model.data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ItemDataEntity {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("price")
    @Expose
    private float price;

    @SerializedName("participants")
    @Expose
    private ArrayList<Integer> participants;

    public ItemDataEntity(String name, float price, ArrayList<Integer> participants) {
        this.name = name;
        this.price = price;
        this.participants = participants;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public ArrayList<Integer> getParticipants() {
        return participants;
    }
}
