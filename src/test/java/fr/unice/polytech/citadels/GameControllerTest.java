package fr.unice.polytech.citadels;

import fr.unice.polytech.citadels.cards.characters.Character;
import fr.unice.polytech.citadels.cards.characters.CharacterType;
import fr.unice.polytech.citadels.cards.districts.District;
import fr.unice.polytech.citadels.cards.districts.DistrictColor;
import fr.unice.polytech.citadels.cards.districts.DistrictType;
import fr.unice.polytech.citadels.players.Player;
import fr.unice.polytech.citadels.strategies.CheapDistrictStrategy;
import fr.unice.polytech.citadels.strategies.ExpGoldStrategy;
import fr.unice.polytech.citadels.strategies.FiveColorsStrategy;
import fr.unice.polytech.citadels.strategies.RandomStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    Player[] players;
    private GameController game;

    @BeforeEach
    void setUp() {
        Player thomas = new Player("Thomas", ExpGoldStrategy.class);
        Player leo = new Player("Leo", CheapDistrictStrategy.class);
        Player valentin = new Player("Valentin", RandomStrategy.class);
        Player alexandre = new Player("Alexandre", RandomStrategy.class);
        players = new Player[]{thomas, leo, valentin, alexandre};
        game = new GameController(players);
    }

    @Test
    void GameController() {
        GameController gameWP = new GameController();
        assertEquals(4, gameWP.getPlayers().size());
    }

    @Test
    void init() {
        assertEquals(List.of(players), game.getPlayers());
    }

    @Test
    void run() {
        game.run();
        assertNotNull(game.getPlayers().getWinner());
    }

    @Test
    void checkFirst() {
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        Player first = game.getPlayers().get(0);
        first.giveGold(60);
        assertEquals(first, game.getPlayers().get(0));

        for (int i = 0; i < 4; i++) {
            District d = new District(DistrictType.FORTRESS);
            first.getDistrictsHand().addDistricts(d);
            first.placeDistrict(d);
        }
        assertEquals(4, first.getDistrictsBoard().size());
        game.checkFirst(first);
        assertNull(game.getPlayers().getWinner());

        for (int i = 0; i < 4; i++) {
            District d = new District(DistrictType.BARRACKS);
            first.getDistrictsHand().addDistricts(d);
            first.placeDistrict(d);
        }
        assertEquals(8, first.getDistrictsBoard().size());

        game.checkFirst(first);
        assertEquals(first, game.getPlayers().getWinner());
    }

    @Test
    void printPodium() {
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        game.getPlayers().printPodium();
    }

    @Test
    void defaultDistrictsTest(){
        game.getPlayers().setWinner(players[0]);
        //On lance la partie en ayant déjà un gagnant, de cette manière les tours ne vont pas se jouer, les joueurs
        //vont simplement recevoir les quartiers de début de partie
        game.run();
        List<Player> players = game.getPlayers();
        players.forEach(player -> assertEquals(Main.DEFAULT_DISTRICTS, player.getDistrictsHand().size()));
    }

    @Test
    void turnOrder(){
        List<Player> joueurs = game.getPlayers();
        joueurs.get(0).chooseCharacter(new Character(CharacterType.KING));
        joueurs.get(1).chooseCharacter(new Character(CharacterType.BISHOP));
        joueurs.get(2).chooseCharacter(new Character(CharacterType.ASSASSIN));
        joueurs.get(3).chooseCharacter(new Character(CharacterType.WARLORD));
        game.getPlayers().sortByCharacter();
        assertEquals(joueurs.get(0).getCharacter().toString(), new Character(CharacterType.ASSASSIN).toString());
        assertEquals(joueurs.get(1).getCharacter().toString(), new Character(CharacterType.KING).toString());
        assertEquals(joueurs.get(2).getCharacter().toString(), new Character(CharacterType.BISHOP).toString());
        assertEquals(joueurs.get(3).getCharacter().toString(), new Character(CharacterType.WARLORD).toString());
    }

    @Test
    void exchangeHand(){
        game.instantiateCharacters();
        District d1 = new District(DistrictType.MONASTERY);
        District d2 = new District(DistrictType.BARRACKS);
        assertEquals(0, players[0].getDistrictsHand().size());
        assertEquals(0, players[1].getDistrictsHand().size());
        players[0].getDistrictsHand().add(d1);
        players[1].getDistrictsHand().add(d2);
        players[1].chooseCharacter(new Character(CharacterType.WARLORD));
        game.getAllCharacters().chooseCharacter(players[1]);
        assertEquals(d1, players[0].getDistrictsHand().get(0));
        assertEquals(d2, players[1].getDistrictsHand().get(0));
        assertEquals(1, players[0].getDistrictsHand().size());
        assertEquals(1, players[1].getDistrictsHand().size());
        game.exchangeHand(players[0], new Character(CharacterType.WARLORD));
        assertEquals(d2, players[0].getDistrictsHand().get(0));
        assertEquals(d1, players[1].getDistrictsHand().get(0));
        assertEquals(1, players[0].getDistrictsHand().size());
        assertEquals(1, players[1].getDistrictsHand().size());
    }

    @Test
    void destroyDistrict() {
        game.instantiateCharacters();
        players[0].chooseCharacter(new Character(CharacterType.WARLORD));
        players[0].giveGold(2);
        game.getAllCharacters().chooseCharacter(players[0]);
        District d1 = new District(DistrictType.MONASTERY);
        District d2 = new District(DistrictType.MONASTERY);
        District d3 = new District(DistrictType.MONASTERY);
        District d4 = new District(DistrictType.MONASTERY);
        players[0].getDistrictsBoard().add(d1);
        players[1].getDistrictsBoard().add(d2);
        players[2].getDistrictsBoard().add(d3);
        players[3].getDistrictsBoard().add(d4);
        game.destroyDistrict(players[0], d3);
        assertTrue(players[0].getDistrictsBoard().contains(d1));
        assertTrue(players[1].getDistrictsBoard().contains(d2));
        assertFalse(players[2].getDistrictsBoard().contains(d3));
        assertTrue(players[3].getDistrictsBoard().contains(d4));
    }

    @Test
    void cantDestroyDistrict() {
        game.instantiateCharacters();
        players[0].chooseCharacter(new Character(CharacterType.WARLORD));
        players[0].giveGold(1);
        game.getAllCharacters().chooseCharacter(players[0]);
        District d1 = new District(DistrictType.MONASTERY);
        District d2 = new District(DistrictType.MONASTERY);
        District d3 = new District(DistrictType.MONASTERY);
        District d4 = new District(DistrictType.MONASTERY);
        players[0].getDistrictsBoard().add(d1);
        players[1].getDistrictsBoard().add(d2);
        players[2].getDistrictsBoard().add(d3);
        players[3].getDistrictsBoard().add(d4);
        game.destroyDistrict(players[0], d3);
        assertTrue(players[0].getDistrictsBoard().contains(d1));
        assertTrue(players[1].getDistrictsBoard().contains(d2));
        assertTrue(players[2].getDistrictsBoard().contains(d3));
        assertTrue(players[3].getDistrictsBoard().contains(d4));
    }

    @Test
    void kill() {
        players[0].kill();
        assertFalse(players[0].canPlay());
        assertTrue(players[1].canPlay());
        assertTrue(players[2].canPlay());
        assertTrue(players[3].canPlay());
    }
    @Test
    void steal() {
        players[1].steal();
        assertFalse(players[0].hasBeenStolen());
        assertTrue(players[1].hasBeenStolen());
        assertFalse(players[2].hasBeenStolen());
        assertFalse(players[3].hasBeenStolen());
    }

}