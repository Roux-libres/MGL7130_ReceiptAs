package com.example.receiptas.model.domain_model;

import java.util.ArrayList;

import javax.inject.Inject;

public class Item {
    private String name;
    private float price;
    private ArrayList<Participant> participants;

    public Item(String name, float price, ArrayList<Participant> participants) {
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

    public ArrayList<Participant> getParticipants() {
        return participants;
    }
}
