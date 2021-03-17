package com.example.receiptas.model.data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParticipantDataEntity {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("payer")
    @Expose
    private boolean payer;

    public ParticipantDataEntity(int id, String name, boolean payer) {
        this.id = id;
        this.name = name;
        this.payer = payer;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isPayer() {
        return payer;
    }
}
