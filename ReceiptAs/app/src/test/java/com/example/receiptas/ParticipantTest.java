package com.example.receiptas;

import com.example.receiptas.model.domain_model.Participant;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParticipantTest {

    public static final String name = "participant";
    public static final boolean payer = false;
    private Participant participant;

    @Before
    public void createParticipant(){
        this.participant = new Participant(this.name, this.payer);
    }

    @Test
    public void testGetName() {
        assertEquals(this.name, this.participant.getName());
    }

    @Test
    public void testIsPayer() {
        assertEquals(this.payer, this.participant.isPayer());
    }

    @Test
    public void testSetPayer() {
        boolean isPayer = true;

        this.participant.setPayer(isPayer);
        assertEquals(isPayer, this.participant.isPayer());
    }
}
