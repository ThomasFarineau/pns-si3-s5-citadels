package fr.unice.polytech.citadels.cards.characters;

import fr.unice.polytech.citadels.CColors;
import fr.unice.polytech.citadels.cards.districts.DistrictColor;
import fr.unice.polytech.citadels.players.Player;

public class Character {
    private final CharacterType type;

    private Player player = null;
    private boolean playable = true;
    private boolean visible = false;

    public Character(CharacterType type) {
        this.type = type;
    }

    public String getName() {
        return type.getName();
    }

    public int getId() {
        return type.getOrder();
    }

    public DistrictColor getDistrictColor() {
        return type.getColor();
    }

    public boolean isPlayed() {
        return player != null;
    }

    public boolean isPlayable() {
        return playable && !visible;
    }

    public CharacterType getType() {
        return type;
    }

    public boolean isVisible() {
        return visible;
    }

    public void reveal() {
        visible = true;
    }

    public void discard() {
        playable = false;
        visible = true;
    }

    public void play(Player player) {
        this.player = player;
    }

    public void lastCard() {
        playable = true;
        visible = false;
        player = null;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Character c))
            return false;
        return getName().equals(c.getName()) && getId() == c.getId();
    }

    @Override
    public String toString() {
        if(type.getColor() != null) {
            return type.getColor().getTextColor() + getName() + "#" + getId() + CColors.RESET;
        }
        return "\033[0;37m" + getName() + "#" + getId() + CColors.RESET;
    }
}
