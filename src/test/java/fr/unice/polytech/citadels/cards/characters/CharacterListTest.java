package fr.unice.polytech.citadels.cards.characters;

import fr.unice.polytech.citadels.players.Player;
import fr.unice.polytech.citadels.strategies.RandomStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterListTest {
    private CharacterList characters;
    @BeforeEach
    void setUp() {
        characters = new CharacterFactory().getCharacters();
    }

    @Test
    void shuffle() {
        CharacterList characters1 = new CharacterList();
        characters1.addAll(characters);
        assertEquals(characters1, characters);
        characters.shuffle();
        assertNotEquals(characters1, characters);
    }

    @Test
    void playableCharacters() {
        characters.discard(5);
        assertEquals(5, characters.playableCharacters().length);
    }

    @Test
    void choosableCharacters() {
        assertEquals(8, characters.choosableCharacters().length);
        characters.discard(5);
        assertEquals(5, characters.choosableCharacters().length);
        Player player = new Player("Bot", RandomStrategy.class);
        player.chooseCharacter(characters.getType(CharacterType.KING));
        characters.chooseCharacter(player);
        assertEquals(4, characters.choosableCharacters().length);

    }

    @Test
    void testPlayableCharacters() {
        assertEquals(7, characters.playableCharacters(2).length);
    }

    @Test
    void hideLastCharacter() {
        assertEquals(8, characters.playableCharacters().length);
        characters.discard(1);
        assertEquals(1, characters.playableCharacters().length);
        characters.hideLastCharacter();
        assertFalse(characters.playableCharacters()[0].isVisible());
    }

    @Test
    void hideLastCharacterMessage() {
        assertEquals(8, characters.playableCharacters().length);
        characters.discard(0);
        assertEquals(8, characters.playableCharacters().length);
    }

    @Test
    void discard() {
        assertEquals(8, characters.playableCharacters().length);
        characters.discard(4);
        assertEquals(4, characters.playableCharacters().length);
    }

    @Test
    void revealCharacter() {
        Character character = characters.getType(CharacterType.ASSASSIN);
        characters.revealCharacter(character);
        assertTrue(character.isVisible());
    }

    @Test
    void chooseCharacter() {
        Player player = new Player("Bot", RandomStrategy.class);
        Character character = characters.getType(CharacterType.KING);
        player.chooseCharacter(character);
        characters.chooseCharacter(player);
        assertEquals(character.getPlayer(), player);
        assertTrue(character.isPlayed());
    }

    @Test
    void getPlayerByCharacter() {
        Player player = new Player("Bot", RandomStrategy.class);
        Character character = characters.getType(CharacterType.KING);
        player.chooseCharacter(character);
        characters.chooseCharacter(player);
        assertEquals(characters.getPlayerByCharacter(character), player);
    }

    @Test
    void getType() {
        Character king = new Character(CharacterType.KING);
        assertEquals(king, characters.getType(CharacterType.KING));
    }
}