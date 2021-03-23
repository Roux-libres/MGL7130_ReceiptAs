package com.example.receiptas.model.domain_model;

import javax.inject.Inject;

public class Participant {
    private String name;
    private boolean payer;

    public Participant(String name, boolean payer) {
        this.name = name;
        this.payer = payer;
    }

    public String getName() {
        return name;
    }

    public boolean isPayer() {
        return payer;
    }

    public void setPayer(boolean payer) {
        this.payer = payer;
    }
}
