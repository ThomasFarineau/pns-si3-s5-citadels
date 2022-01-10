package fr.unice.polytech.citadels.cards.districts;

import fr.unice.polytech.citadels.CColors;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistrictColorTest {

    @Test
    void getTextColor_red() {
        assertEquals(CColors.RED, DistrictColor.red.getTextColor());
    }

    @Test
    void getTextColor_blue() {
        assertEquals(CColors.BLUE, DistrictColor.blue.getTextColor());
    }

    @Test
    void getTextColor_green() {
        assertEquals(CColors.GREEN, DistrictColor.green.getTextColor());
    }

    @Test
    void getTextColor_yellow() {
        assertEquals(CColors.YELLOW, DistrictColor.yellow.getTextColor());
    }

    @Test
    void getTextColor_purple() {
        assertEquals(CColors.MAGENTA, DistrictColor.purple.getTextColor());
    }

    @Test
    void values() {
        assertEquals(5, DistrictColor.values().length);
    }

    @Test
    void valueOf_red() {
        assertEquals(DistrictColor.red, DistrictColor.valueOf("red"));
    }

    @Test
    void valueOf_blue() {
        assertEquals(DistrictColor.blue, DistrictColor.valueOf("blue"));
    }

    @Test
    void valueOf_yellow() {
        assertEquals(DistrictColor.yellow, DistrictColor.valueOf("yellow"));
    }

    @Test
    void valueOf_green() {
        assertEquals(DistrictColor.green, DistrictColor.valueOf("green"));
    }

    @Test
    void valueOf_purple() {
        assertEquals(DistrictColor.purple, DistrictColor.valueOf("purple"));
    }

}