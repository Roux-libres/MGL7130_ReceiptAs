package com.example.receiptas.model.domain_model;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

public class Receipt {
    private String name;
    private Date date;
    private ArrayList<Item> items;
    private ArrayList<Participant> participants;

    public Receipt() {

    }

    public Receipt(String name, Date date, ArrayList<Item> items, ArrayList<Participant> participants) {
        this.name = name;
        this.date = date;
        this.items = items;
        this.participants = participants;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void setParticipants(ArrayList<Participant> participants) {
        this.participants = participants;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public void addParticipantByName(String name) {
        this.participants.add(new Participant(name, false));
    }
}
