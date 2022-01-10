package fr.unice.polytech.citadels;

import fr.unice.polytech.citadels.players.Player;
import fr.unice.polytech.citadels.strategies.RandomStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VerboseTest {

    @Test
    void getPlayerToFocus() {
        Player p = new Player("Bot", RandomStrategy.class);
        Verbose verbose = new Verbose(p, 2);
        assertEquals(p, verbose.getPlayerToFocus());
        assertEquals(2, verbose.getLevel());
    }

    @Test
    void getLevel() {
        Verbose verbose = new Verbose(2);
        assertEquals(2, verbose.getLevel());
        assertNull(verbose.getPlayerToFocus());
    }
}