package fr.unice.polytech.citadels.cards.districts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistrictTest {

    @BeforeEach
    void setUp() {
        District.indenter = 0;
    }

    @Test
    void getPrice() {
        District d = new District(DistrictType.BARRACKS);
        assertEquals(3, d.getPrice());
    }

    @Test
    void getColor() {
        District d = new District(DistrictType.BARRACKS);
        assertEquals(DistrictColor.red, d.getColor());
    }

    @Test
    void getScore() {
        District d = new District(DistrictType.BARRACKS);
        assertEquals(3, d.getScore());
    }

    @Test
    void testEquals() {
        District d = new District(DistrictType.BARRACKS);
        District.indenter = 0;
        District same = new District(DistrictType.BARRACKS);
        assertEquals(d, same);
    }
}