package fr.unice.polytech.citadels.players;

import fr.unice.polytech.citadels.GameController;
import fr.unice.polytech.citadels.Main;
import fr.unice.polytech.citadels.cards.districts.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class DistrictsTest {

    @Test
    void containsByType() {
        Districts districts = new Districts();
        District a = new District(DistrictType.TEMPLE);
        District b = new District(DistrictType.MONASTERY);
        districts.addAll(List.of(a,b));

        assertTrue(districts.containsByType(DistrictType.TEMPLE));
        assertFalse(districts.containsByType(DistrictType.CATHEDRAL));
    }

    @Test
    void getByType() {
        Districts districts = new Districts();
        District a = new District(DistrictType.TEMPLE);
        District b = new District(DistrictType.MONASTERY);
        District c = new District(DistrictType.TEMPLE);
        districts.addAll(List.of(a, b, c));

        assertEquals(a, districts.getByType(DistrictType.TEMPLE)[0]);
        assertEquals(c, districts.getByType(DistrictType.TEMPLE)[1]);
        assertEquals(2, districts.getByType(DistrictType.TEMPLE).length);
    }

    @Test
    void getByColor() {
        Districts districts = new Districts();
        District a = new District(DistrictType.TEMPLE);
        District b = new District(DistrictType.MONASTERY);
        District c = new District(DistrictType.OBSERVATORY);
        districts.addAll(List.of(a, b, c));

        assertEquals(a, districts.getByColor(DistrictColor.blue)[0]);
        assertEquals(b, districts.getByColor(DistrictColor.blue)[1]);
        assertEquals(2, districts.getByColor(DistrictColor.blue).length);
    }

    @Test
    void addDistricts() {
        Districts districts = new Districts();
        District a = new District(DistrictType.TEMPLE);
        District b = new District(DistrictType.MONASTERY);
        District c = new District(DistrictType.OBSERVATORY);
        districts.addDistricts(a, b, c);
        assertEquals(3, districts.size());
        assertTrue(districts.contains(a));
        assertTrue(districts.contains(b));
        assertTrue(districts.contains(c));
    }

    @Test
    void removeDistricts() {
        Districts districts = new Districts();
        District a = new District(DistrictType.TEMPLE);
        District b = new District(DistrictType.MONASTERY);
        District c = new District(DistrictType.OBSERVATORY);
        districts.addDistricts(a, b, c);
        assertEquals(3, districts.size());
        assertTrue(districts.contains(a));
        assertTrue(districts.contains(b));
        assertTrue(districts.contains(c));
        districts.removeDistricts(a, b);
        assertEquals(1, districts.size());
        assertFalse(districts.contains(a));
        assertFalse(districts.contains(b));
        assertTrue(districts.contains(c));
    }

    @Test
    void place() {
        int r_temp = GameController.round;
        GameController.round = 2;
        Districts districts = new Districts();
        District a = new District(DistrictType.TEMPLE);
        districts.place(a);
        assertEquals(1, districts.size());
        assertTrue(districts.contains(a));
        assertEquals(2, a.getPlacedOnRound());
        GameController.round = r_temp;
    }

    @Test
    void score() {
        Districts districts = new Districts();
        District a = new District(DistrictType.TEMPLE);
        District b = new District(DistrictType.MONASTERY);
        District c = new District(DistrictType.OBSERVATORY);
        districts.addDistricts(a, b, c);

        int score = a.getScore() + b.getScore() + c.getScore();
        assertEquals(score, districts.score());
    }

    @Test
    void containsColorSet5() {
        Districts districts = new Districts();
        District a = new District(DistrictType.TEMPLE);
        District b = new District(DistrictType.CASTLE);
        District c = new District(DistrictType.BARRACKS);
        District d = new District(DistrictType.HARBOR);
        District e = new District(DistrictType.OBSERVATORY);
        districts.addDistricts(a, b, c, d, e);

        assertTrue(districts.containsColorSet());
    }

    @Test
    void containsColorSet4False() {
        int r_temp = GameController.round;
        Districts districts = new Districts();
        District a = new District(DistrictType.TEMPLE);
        District b = new District(DistrictType.CASTLE);
        District c = new District(DistrictType.HARBOR);
        District d = new District(DistrictType.HARBOR);
        District e = new District(DistrictType.OBSERVATORY);
        District f = new District(DistrictType.COURT_OF_MIRACLES);
        GameController.round = 1;
        districts.place(a);
        GameController.round = 2;
        districts.place(b);
        GameController.round = 3;
        districts.place(c);
        GameController.round = 4;
        districts.place(d);
        GameController.round = 5;
        districts.place(e);
        GameController.round = 6;
        districts.place(f);

        assertFalse(districts.containsColorSet());
        GameController.round = r_temp;
    }

    @Test
    void containsColorSet4True() {
        int r_temp = GameController.round;
        Districts districts = new Districts();
        District a = new District(DistrictType.TEMPLE);
        District b = new District(DistrictType.CASTLE);
        District c = new District(DistrictType.HARBOR);
        District d = new District(DistrictType.HARBOR);
        District e = new District(DistrictType.OBSERVATORY);
        District f = new District(DistrictType.COURT_OF_MIRACLES);
        GameController.round = 1;
        districts.place(a);
        GameController.round = 2;
        districts.place(b);
        GameController.round = 3;
        districts.place(c);
        GameController.round = 4;
        districts.place(d);
        GameController.round = 5;
        districts.place(f);
        GameController.round = 6;
        districts.place(e);

        assertTrue(districts.containsColorSet());
        GameController.round = r_temp;
    }

    @Test
    void containsColorSetFalse() {
        Districts districts = new Districts();
        District a = new District(DistrictType.TEMPLE);
        District b = new District(DistrictType.CASTLE);
        District c = new District(DistrictType.BARRACKS);
        District d = new District(DistrictType.HARBOR);
        District e = new District(DistrictType.HARBOR);
        districts.addDistricts(a, b, c, d, e);

        assertFalse(districts.containsColorSet());
    }


    @Test
    void getDestroyable() {
        Districts districts = new Districts();
        District a = new District(DistrictType.TEMPLE);
        District b = new District(DistrictType.CASTLE);
        District c = new District(DistrictType.BARRACKS);
        District d = new District(DistrictType.HARBOR);
        District e = new District(DistrictType.HARBOR);
        District f = new District(DistrictType.DUNGEON);
        districts.addDistricts(a, b, c, d, e, f);
        HashMap<District, Integer> ex = new HashMap<>();
        ex.put(a, a.getPrice()-1);
        ex.put(b, b.getPrice()-1);
        ex.put(c, c.getPrice()-1);
        ex.put(d, d.getPrice()-1);
        ex.put(e, e.getPrice()-1);
        assertEquals(ex, districts.getDestroyable());
    }

    @Test
    void getDestroyableEmpty() {
        Districts districts = new Districts();
        IntStream.range(0, Main.DISTRICTS_FOR_WIN).mapToObj(i -> new District(DistrictType.TEMPLE)).forEach(districts::addDistricts);
        HashMap<District, Integer> ex = new HashMap<>();
        assertEquals(ex, districts.getDestroyable());
    }
}