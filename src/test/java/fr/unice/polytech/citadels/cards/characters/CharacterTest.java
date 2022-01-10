package fr.unice.polytech.citadels.cards.characters;

import fr.unice.polytech.citadels.cards.districts.DistrictColor;
import fr.unice.polytech.citadels.players.Player;
import fr.unice.polytech.citadels.strategies.CheapDistrictStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterTest {

    @Test
    void getName() {
        Character ch = new Character(CharacterType.ASSASSIN);
        assertEquals(ch.getName(), "Assassin");
    }

    @Test
    void getId() {
        Character ch = new Character(CharacterType.THIEF);
        assertEquals(ch.getId(), 2);
    }

    @Test
    void getDistrictColorNull() {
        Character ch = new Character(CharacterType.MAGICIAN);
        assertNull(ch.getDistrictColor());
    }

    @Test
    void getDistrictColorReal() {
        Character ch = new Character(CharacterType.KING);
        assertEquals(ch.getDistrictColor(), DistrictColor.yellow);
    }

    @Test
    void isPlayed() {
        Character ch = new Character(CharacterType.BISHOP);
        assertFalse(ch.isPlayed());
    }

    @Test
    void isPlayable() {
        Character ch = new Character(CharacterType.MERCHANT);
        assertTrue(ch.isPlayable());
    }

    @Test
    void getType() {
        Character ch = new Character(CharacterType.ARCHITECT);
        assertEquals(ch.getType(), CharacterType.ARCHITECT);
    }

    @Test
    void isVisible() {
        Character ch = new Character(CharacterType.WARLORD);
        assertFalse(ch.isVisible());
    }

    @Test
    void reveal() {
        Character ch = new Character(CharacterType.ASSASSIN);
        ch.reveal();
        assertTrue(ch.isVisible());
    }

    @Test
    void discard() {
        Character ch = new Character(CharacterType.THIEF);
        ch.discard();
        assertFalse(ch.isPlayed());
        assertFalse(ch.isPlayable());
        assertTrue(ch.isVisible());
    }

    @Test
    void getPlayer() {
        Character ch = new Character(CharacterType.MAGICIAN);
        Player leo = new Player("Léo", CheapDistrictStrategy.class);
        ch.play(leo);
        assertEquals(ch.getPlayer(), leo);
    }

    @Test
    void lastCard() {
        Character ch = new Character(CharacterType.KING);
        ch.lastCard();
        assertTrue(ch.isPlayable());
        assertNull(ch.getPlayer());
    }

    @Test
    void equalsDifferent() {
        Character ch1 = new Character(CharacterType.BISHOP);
        Character ch2 = new Character(CharacterType.MERCHANT);
        assertNotEquals(ch1, ch2);
    }

    @Test
    void equalsSameIdToo() {
        Character ch1 = new Character(CharacterType.ARCHITECT);
        Character ch1_ = new Character(CharacterType.ARCHITECT);
        assertEquals(ch1, ch1_);
    }

    @Test
    void equalsNotCharacter() {
        Character ch1 = new Character(CharacterType.ARCHITECT);
        Object object = new Object();
        assertNotEquals(ch1, object);
    }
    
}
