package com.example.receiptas.model.data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParticipantDataEntity {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("payer")
    @Expose
    private boolean payer;

    public ParticipantDataEntity(String name, boolean payer) {
        this.name = name;
        this.payer = payer;
    }

    public String getName() {
        return name;
    }

    public boolean isPayer() {
        return payer;
    }
}
