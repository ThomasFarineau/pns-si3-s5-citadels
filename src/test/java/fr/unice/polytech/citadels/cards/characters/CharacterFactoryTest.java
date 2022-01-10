package fr.unice.polytech.citadels.cards.characters;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CharacterFactoryTest {

    @Test
    void getCharacters() {
        CharacterFactory characterFactory = new CharacterFactory();
        assertEquals(8, characterFactory.getCharacters().size());
        List<Character> characterList = Arrays.stream(CharacterType.values()).map(Character::new).toList();
        assertTrue(characterFactory.getCharacters().containsAll(characterList));
    }
}