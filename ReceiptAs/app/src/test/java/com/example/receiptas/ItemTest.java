package com.example.receiptas;

import com.example.receiptas.model.domain_model.Item;
import com.example.receiptas.model.domain_model.Participant;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ItemTest {

    public static final String name = "item";
    public static final float price = 1.23f;
    public static ArrayList<Participant> participants;
    private Item item;

    @Before
    public void createItem(){
        this.participants = new ArrayList<>();

        for(int i=0; i < 3; i++){
            this.participants.add(mock(Participant.class));
        }

        this.item = new Item(this.name, this.price, this.participants);
    }

    @Test
    public void testGetName() {
        assertEquals(this.name, this.item.getName());
    }

    @Test
    public void testGetPrice() {
        assertEquals(this.price, this.item.getPrice(), 0.01);
    }

    @Test
    public void testGetParticipants() {
        assertEquals(this.participants.size(), this.item.getParticipants().size());
    }
}
