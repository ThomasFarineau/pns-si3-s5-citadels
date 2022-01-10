package fr.unice.polytech.citadels.cards.characters;

import fr.unice.polytech.citadels.cards.districts.DistrictColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CharacterTypeTest {
    @Test
    void getOrder() {
        assertEquals(CharacterType.ASSASSIN.getOrder(),1);
    }
    @Test
    void getName() {
        assertEquals(CharacterType.THIEF.getName(),"Voleur");
    }
    @Test
    void getColorNull() {
        assertNull(CharacterType.MAGICIAN.getColor());
    }
    @Test
    void getColorReal() {
        assertEquals(CharacterType.KING.getColor(),DistrictColor.yellow);
    }
}
