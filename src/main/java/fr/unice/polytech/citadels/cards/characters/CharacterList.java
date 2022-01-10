package fr.unice.polytech.citadels.cards.characters;

import fr.unice.polytech.citadels.Logger;
import fr.unice.polytech.citadels.players.Player;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class CharacterList extends LinkedList<Character> {

    public CharacterList(Character... characters) {
        this.addAll(List.of(characters));
    }

    public void shuffle() {
        Collections.shuffle(this);
    }

    public Character[] playableCharacters() {
        return playableCharacters(0);
    }

    public Character[] choosableCharacters() {
        return this.stream().filter(c -> c.isPlayable() && !c.isPlayed()).toArray(Character[]::new);
    }

    public Character[] playableCharacters(int fromId) {
        return this.stream().filter(c -> c.isPlayable() && c.getId() >= fromId).toArray(Character[]::new);
    }

    public void hideLastCharacter() {
        this.stream().filter(c -> !c.isPlayed()).forEach(Character::lastCard);
    }

    public void discard(int remainingCard) {
        if (remainingCard < 1) {
            Logger.print("Ecartement des cartes impossible avec " + remainingCard + " cartes.", 1);
            return;
        }
        CharacterList characters = new CharacterList();
        characters.add(this.getType(CharacterType.KING));
        this.remove(this.getType(CharacterType.KING));
        IntStream.range(1, remainingCard).mapToObj(i -> this.peek()).forEach(picked -> {
            this.remove(picked);
            characters.add(picked);
        });
        this.forEach(Character::discard);
        this.addAll(characters);
    }

    public void revealCharacter(Character c) {
        this.stream().filter(c::equals).forEach(Character::reveal);
    }

    public void chooseCharacter(Player p) {
        int i = 0;
        for (; i < this.size(); i++) {
            Character character = this.get(i);
            if (p.characterChosen().equals(character)) break;
        }
        this.get(i).play(p);
    }

    public Player getPlayerByCharacter(Character character) {
        return this.stream().filter(c -> c.equals(character)).findFirst().map(Character::getPlayer).orElse(null);
    }

    public Character getType(CharacterType type) {
        return this.stream().filter(character -> character.getType().equals(type)).findFirst().orElse(null);
    }

}
