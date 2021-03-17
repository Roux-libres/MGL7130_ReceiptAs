package com.example.receiptas.model.domain_model;

import javax.inject.Inject;

public class Participant {
    private int id;
    private String name;
    private boolean payer;

    @Inject
    public Participant(int id, String name, boolean payer) {
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
