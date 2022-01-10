package fr.unice.polytech.citadels.cards.districts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistrictListTest {
    private DistrictList districts;

    @BeforeEach
    void setUp() {
        districts = new DistrictFactory().getDistrictList();
        District.indenter = 0;
    }

    @Test
    void shuffle() {
        // Dans la lecture du CSV, le premier c'est le Manoir, testons alors le manoir
        District d = districts.pick();
        District.indenter = 1;
        District district = new District(DistrictType.MANOR);
        assertEquals(district, d);
        districts.shuffle();
        d = districts.pick();
        assertNotEquals(district, d);
    }

    @Test
    void pick() {
        District d = districts.pick();
        District.indenter = 1;
        District district = new District(DistrictType.MANOR);
        assertEquals(district, d);
    }

    @Test
    void testPick2Districts() {
        District[] d = districts.pick(2);
        assertEquals(2, d.length);
    }

    @Test
    void testPick10Districts() {
        District[] d = districts.pick(10);
        assertEquals(10, d.length);
    }

    @Test
    void append() {
        assertEquals(64, districts.size());
        District d = districts.pick();
        assertEquals(63, districts.size());
        districts.append(d);
        assertEquals(64, districts.size());
    }
}