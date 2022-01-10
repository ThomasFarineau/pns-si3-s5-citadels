package fr.unice.polytech.citadels.simulator;

import fr.unice.polytech.citadels.players.Player;
import fr.unice.polytech.citadels.simulator.PlayerResult;
import fr.unice.polytech.citadels.strategies.CheapDistrictStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerResultTest {
    Player player;
    PlayerResult pr;

    @BeforeEach
    void setUp() {
        player = new Player("Leo", CheapDistrictStrategy.class);
        pr = new PlayerResult(player);
    }

    @Test
    void getAverage() {
        pr.addScore(20);
        pr.addScore(17);
        pr.addScore(25);
        pr.addScore(9);
        pr.addScore(34);
        assertEquals(pr.getAverage(),21);
    }

    @Test
    void getRate() {
        pr.winner();
        pr.addScore(20);
        pr.drawer();
        pr.addScore(17);
        pr.winner();
        pr.addScore(15);
        pr.looser();
        pr.addScore(11);
        pr.winner();
        pr.addScore(28);
        pr.looser();
        pr.addScore(27);
        pr.winner();
        pr.addScore(20);
        pr.looser();
        pr.addScore(27);
        pr.winner();
        pr.addScore(20);
        pr.drawer();
        pr.addScore(17);
        assertEquals(pr.getRate(pr.getWin()),50);
        assertEquals(pr.getRate(pr.getDraw()),20);
        assertEquals(pr.getRate(pr.getLose()),30);
    }

    @Test
    void getPlayer() {
        assertEquals(player,pr.getPlayer());
    }

    @Test
    void getWin() {
        for(int i = 0; i<5; i++){
            pr.winner();}
        assertEquals(pr.getWin(),5);
    }

    @Test
    void getDraw() {
        for(int i = 0; i<4; i++){
            pr.drawer();}
        assertEquals(pr.getDraw(),4);
    }

    @Test
    void getLose() {
        for(int i = 0; i<3; i++){
            pr.looser();}
        assertEquals(pr.getLose(),3);
    }

    @Test
    void getGames() {
        for(int i = 0; i<6; i++){
            pr.addScore(8);}
        assertEquals(pr.getGames(),6);
    }

    @Test
    void isSet() {
        pr.winner();
        pr.addScore(20);
        pr.drawer();
        assertFalse(pr.isSet());
        pr.addScore(17);
        pr.winner();
        pr.addScore(15);
        pr.looser();
        pr.addScore(11);
        pr.winner();
        pr.addScore(28);
        pr.looser();
        pr.addScore(27);
        pr.winner();
        pr.addScore(20);
        assertTrue(pr.isSet());
    }

    @Test
    void getStrategyName() {
        assertEquals("CheapDistrictStrategy",pr.getStrategyName());
    }

    @Test
    void string() {
        pr.winner();
        pr.addScore(20);
        pr.drawer();
        pr.addScore(17);
        pr.winner();
        pr.addScore(15);
        pr.looser();
        pr.addScore(11);
        pr.winner();
        pr.addScore(28);
        pr.looser();
        pr.addScore(27);
        pr.winner();
        pr.addScore(20);
        pr.looser();
        pr.addScore(27);
        pr.winner();
        pr.addScore(20);
        pr.drawer();
        pr.addScore(17);
        assertEquals("Leo - CheapDistrictStrategy - victoire : 5 (50.0%) - défaite : 3 (30.0%) - match nul : 2 (20.0%) - score moyen : 20.2",pr.toString());
    }
}
