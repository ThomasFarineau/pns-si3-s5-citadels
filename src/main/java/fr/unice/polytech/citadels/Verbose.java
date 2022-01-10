package fr.unice.polytech.citadels;

import fr.unice.polytech.citadels.players.Player;

public class Verbose {
    private Player playerToFocus = null;
    private final int level;

    // Verbose level 0 = only the score at the end
    // Verbose level 1 = public
    // Verbose level 2 = public + privates
    // Verbose level 3 = public + private + GameController action

    public Verbose(int level) {
        this.level = level;
    }

    public Verbose(Player playerToFocus, int level) {
        this.playerToFocus = playerToFocus;
        this.level = level;
    }

    public Player getPlayerToFocus() {
        return playerToFocus;
    }

    public int getLevel() {
        return level;
    }
}
