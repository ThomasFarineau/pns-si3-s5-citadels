package fr.unice.polytech.citadels.cards.characters;

import java.util.stream.IntStream;

public class CharacterFactory {
    private final CharacterList characters;

    public CharacterFactory() {
        Character[] charactersArray = new Character[CharacterType.values().length];
        CharacterType[] values = CharacterType.values();
        IntStream.range(0, values.length).forEach(i -> charactersArray[i] = new Character(values[i]));
        this.characters = new CharacterList(charactersArray);
    }

    public CharacterList getCharacters() {
        return characters;
    }
}
