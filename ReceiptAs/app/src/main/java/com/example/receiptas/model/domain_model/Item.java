package com.example.receiptas.model.domain_model;

import java.util.ArrayList;

import javax.inject.Inject;

public class Item {
    private int id;
    private String name;
    private float price;
    private ArrayList<Participant> participants;

    public Item(int id, String name, float price, ArrayList<Participant> participants) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.participants = participants;
    }

    public int getId() {
        return id;
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
