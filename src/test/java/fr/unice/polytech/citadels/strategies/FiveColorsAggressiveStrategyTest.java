package fr.unice.polytech.citadels.strategies;

import fr.unice.polytech.citadels.GameController;
import fr.unice.polytech.citadels.cards.characters.Character;
import fr.unice.polytech.citadels.cards.characters.CharacterType;
import fr.unice.polytech.citadels.cards.characters.abilities.AbilityResult;
import fr.unice.polytech.citadels.cards.districts.District;
import fr.unice.polytech.citadels.cards.districts.DistrictColor;
import fr.unice.polytech.citadels.cards.districts.DistrictType;
import fr.unice.polytech.citadels.players.Districts;
import fr.unice.polytech.citadels.players.Player;
import fr.unice.polytech.citadels.players.PlayerAction;
import fr.unice.polytech.citadels.players.PlayerList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FiveColorsAggressiveStrategyTest {
    Player player, playerfull;
    District districtTemple, districtWatchtower, districtBarracks, districtTavern, districtStall, districtFortress, districtMonastery, districtObservatory;
    PlayerList playerList;

    @BeforeEach
    void setup() {
        // Pour éviter de polluer le sout de message d'action
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        player = new Player("Player", FiveColorsAggressiveStrategy.class);
        districtTemple = new District(DistrictType.TEMPLE);
        districtWatchtower = new District(DistrictType.WATCHTOWER);
        districtBarracks = new District(DistrictType.BARRACKS);

        playerfull = new Player("FiveColorsAggressiveStrategy", FiveColorsAggressiveStrategy.class);
        districtTavern = new District(DistrictType.TAVERN);
        districtStall = new District(DistrictType.STALL);
        districtFortress = new District(DistrictType.FORTRESS);
        districtMonastery = new District(DistrictType.MONASTERY);
        districtObservatory = new District(DistrictType.OBSERVATORY);
        playerfull.getDistrictsBoard().addDistricts(districtTemple, districtWatchtower, districtBarracks, districtTavern, districtStall, districtFortress, districtMonastery);
        playerfull.getDistrictsHand().addDistricts(districtObservatory);
        playerfull.giveGold(10);

        playerList = new PlayerList(player, playerfull);

    }

    @Test
    void chooseCharacterWarlordABW() {
        Character[] playable = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.WARLORD), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        Character[] selectable = new Character[]{new Character(CharacterType.WARLORD), new Character(CharacterType.KING), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        assertEquals(playerfull.strategy().chooseCharacter(selectable, playable, playerList), new Character(CharacterType.WARLORD));
    }

    @Test
    void chooseCharacterAssassinABW() {
        Character[] playable = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.WARLORD), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        Character[] selectable = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        assertEquals(playerfull.strategy().chooseCharacter(selectable, playable, playerList), new Character(CharacterType.ASSASSIN));
    }

    @Test
    void chooseCharacterAssassinAW() {
        Character[] playable = new Character[]{new Character(CharacterType.THIEF), new Character(CharacterType.KING), new Character(CharacterType.WARLORD), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        Character[] selectable = new Character[]{new Character(CharacterType.WARLORD), new Character(CharacterType.KING), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        assertEquals(playerfull.strategy().chooseCharacter(selectable, playable, playerList), new Character(CharacterType.ASSASSIN));
    }

    @Test
    void chooseCharacterWarlordAW() {
        Character[] playable = new Character[]{new Character(CharacterType.THIEF), new Character(CharacterType.KING), new Character(CharacterType.WARLORD), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        Character[] selectable = new Character[]{new Character(CharacterType.THIEF), new Character(CharacterType.KING), new Character(CharacterType.MERCHANT), new Character(CharacterType.WARLORD)};
        assertEquals(playerfull.strategy().chooseCharacter(selectable, playable, playerList), new Character(CharacterType.WARLORD));
    }

    @Test
    void chooseCharacterAssassinAM() {
        Character[] playable = new Character[]{new Character(CharacterType.MAGICIAN), new Character(CharacterType.KING), new Character(CharacterType.THIEF), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        Character[] selectable = new Character[]{new Character(CharacterType.ASSASSIN), new Character(CharacterType.KING), new Character(CharacterType.MERCHANT), new Character(CharacterType.MAGICIAN)};
        assertEquals(playerfull.strategy().chooseCharacter(selectable, playable, playerList), new Character(CharacterType.ASSASSIN));
    }

    @Test
    void chooseCharacterMagicianAM() {
        Character[] playable = new Character[]{new Character(CharacterType.MAGICIAN), new Character(CharacterType.KING), new Character(CharacterType.THIEF), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        Character[] selectable = new Character[]{new Character(CharacterType.THIEF), new Character(CharacterType.KING), new Character(CharacterType.MERCHANT), new Character(CharacterType.MAGICIAN)};
        assertEquals(playerfull.strategy().chooseCharacter(selectable, playable, playerList), new Character(CharacterType.MAGICIAN));
    }

    @Test
    void chooseCharacterWarlordBW() {
        Character[] playable = new Character[]{new Character(CharacterType.WARLORD), new Character(CharacterType.KING), new Character(CharacterType.MAGICIAN), new Character(CharacterType.MERCHANT), new Character(CharacterType.BISHOP)};
        Character[] selectable = new Character[]{new Character(CharacterType.MAGICIAN), new Character(CharacterType.KING), new Character(CharacterType.BISHOP), new Character(CharacterType.WARLORD)};
        assertEquals(playerfull.strategy().chooseCharacter(selectable, playable, playerList), new Character(CharacterType.WARLORD));
    }

    @Test
    void chooseCharacterBishopBW() {
        Character[] playable = new Character[]{new Character(CharacterType.WARLORD), new Character(CharacterType.KING), new Character(CharacterType.MAGICIAN), new Character(CharacterType.MERCHANT), new Character(CharacterType.BISHOP)};
        Character[] selectable = new Character[]{new Character(CharacterType.MAGICIAN), new Character(CharacterType.KING), new Character(CharacterType.BISHOP), new Character(CharacterType.MERCHANT)};
        assertEquals(playerfull.strategy().chooseCharacter(selectable, playable, playerList), new Character(CharacterType.BISHOP));
    }


    @Test
    void chooseCharacterArchiect() {
        Player player1 = new Player("player1", AggressiveStrategy.class);
        Player player2 = new Player("player2", AggressiveStrategy.class);
        List<Player> playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);
        player2.getDistrictsBoard().addDistricts(districtTemple, districtWatchtower, districtBarracks, districtTavern);
        player1.getDistrictsBoard().addDistricts(districtTemple, districtWatchtower, districtBarracks, districtTavern, districtStall, districtObservatory);
        player1.getDistrictsHand().add(districtFortress);
        player1.getDistrictsHand().add(districtMonastery);
        player1.setGold(5);

        Character[] playable = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.WARLORD), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        Character[] selectable = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.ARCHITECT), new Character(CharacterType.MERCHANT)};
        assertEquals(player1.strategy().chooseCharacter(selectable, playable, playerList), new Character(CharacterType.ARCHITECT));
    }

    @Test
    void chooseCharacterArchiectFalse() {
        Character[] playable = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.WARLORD), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        Character[] selectable = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.MERCHANT), new Character(CharacterType.ARCHITECT), new Character(CharacterType.ASSASSIN)};
        assertNotEquals(playerfull.strategy().chooseCharacter(selectable, playable, playerList), new Character(CharacterType.ARCHITECT));
    }

    @Test
    void chooseCharacterKing() {

        Player player2 = new Player("player2", RandomStrategy.class);
        List<Player> playerList = new ArrayList<>();
        playerList.add(playerfull);
        playerList.add(player2);
        player2.getDistrictsBoard().addDistricts(districtTemple, districtWatchtower, districtBarracks, districtTavern, districtStall, districtFortress);

        Character[] playable = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.WARLORD), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        Character[] selectable = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.ARCHITECT), new Character(CharacterType.MERCHANT)};
        assertEquals(playerfull.strategy().chooseCharacter(selectable, playable, playerList), new Character(CharacterType.KING));
    }

    @Test
    void chooseCharacterKingFalse() {

        Player player2 = new Player("player2", RandomStrategy.class);
        List<Player> playerList = new ArrayList<>();
        playerList.add(playerfull);
        playerList.add(player2);
        player2.getDistrictsBoard().addDistricts(districtTemple, districtWatchtower, districtBarracks, districtTavern, districtStall, districtFortress);

        Character[] playable = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.WARLORD), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        Character[] selectable = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.ARCHITECT), new Character(CharacterType.WARLORD)};
        assertEquals(playerfull.strategy().chooseCharacter(selectable, playable, playerList), new Character(CharacterType.WARLORD));
    }

    @Test
    void chooseAssassinWhenSomeoneWillWin() {
        GameController gc = new GameController();
        Player player2 = new Player("player2", AggressiveStrategy.class);
        player2.getDistrictsBoard().addDistricts(districtTemple, districtWatchtower, districtBarracks);
        player2.getDistrictsHand().addDistricts(districtTavern, districtStall, districtFortress);
        gc.init(playerfull, player2);
        Character[] playable = new Character[]{new Character(CharacterType.ASSASSIN), new Character(CharacterType.KING), new Character(CharacterType.MAGICIAN), new Character(CharacterType.MERCHANT), new Character(CharacterType.BISHOP), new Character(CharacterType.THIEF)};
        Character[] selectable = new Character[]{new Character(CharacterType.MAGICIAN), new Character(CharacterType.KING), new Character(CharacterType.BISHOP), new Character(CharacterType.ASSASSIN), new Character(CharacterType.THIEF)};
        assertEquals(player2.strategy().chooseCharacter(selectable, playable, gc.getPlayers()), new Character(CharacterType.ASSASSIN));
    }

    @Test
    void chooseAssassinWhenWithALotOfGold() {
        GameController gc = new GameController();
        Player player2 = new Player("player2", AggressiveStrategy.class);
        player2.getDistrictsBoard().addDistricts(districtTemple, districtWatchtower, districtBarracks);
        player2.getDistrictsHand().addDistricts(districtTavern);
        player2.giveGold(20);
        Player player3 = new Player("player3", AggressiveStrategy.class);
        player3.getDistrictsBoard().addDistricts(districtTemple, districtWatchtower, districtBarracks);
        player3.getDistrictsHand().addDistricts(districtTavern, districtStall, districtFortress);
        gc.init(player2, player3);
        Character[] playable = new Character[]{new Character(CharacterType.ASSASSIN), new Character(CharacterType.KING), new Character(CharacterType.MAGICIAN), new Character(CharacterType.MERCHANT), new Character(CharacterType.BISHOP)};
        Character[] selectable = new Character[]{new Character(CharacterType.MAGICIAN), new Character(CharacterType.KING), new Character(CharacterType.BISHOP), new Character(CharacterType.ASSASSIN)};
        assertEquals(player2.strategy().chooseCharacter(selectable, playable, gc.getPlayers()), new Character(CharacterType.ASSASSIN));
    }

    @Test
    void chooseAssassinWhenWithCardsInHand() {
        GameController gc = new GameController();
        Player player2 = new Player("player2", AggressiveStrategy.class);
        player2.getDistrictsBoard().addDistricts(districtTemple);
        player2.getDistrictsHand().addDistricts(districtWatchtower, districtBarracks, districtTavern, districtStall, districtFortress, districtMonastery);
        Player player3 = new Player("player3", AggressiveStrategy.class);
        player2.getDistrictsBoard().addDistricts(districtTemple, districtWatchtower, districtBarracks);

        gc.init(player2, player3);
        Character[] playable = new Character[]{new Character(CharacterType.ASSASSIN), new Character(CharacterType.KING), new Character(CharacterType.MAGICIAN), new Character(CharacterType.MERCHANT), new Character(CharacterType.BISHOP)};
        Character[] selectable = new Character[]{new Character(CharacterType.MAGICIAN), new Character(CharacterType.KING), new Character(CharacterType.BISHOP), new Character(CharacterType.ASSASSIN)};
        assertEquals(player2.strategy().chooseCharacter(selectable, playable, gc.getPlayers()), new Character(CharacterType.ASSASSIN));
    }

    @Test
    void chooseWarlordWhenSomeoneWillWin() {
        GameController gc = new GameController();
        Player player2 = new Player("player2", AggressiveStrategy.class);
        player2.getDistrictsBoard().addDistricts(districtTemple, districtWatchtower, districtBarracks);
        player2.getDistrictsHand().addDistricts(districtTavern);
        Player player3 = new Player("player3", AggressiveStrategy.class);
        player3.getDistrictsBoard().addDistricts(districtTemple, districtWatchtower, districtBarracks, districtTavern, districtStall, districtFortress);
        gc.init(player2, player3);
        Character[] playable = new Character[]{new Character(CharacterType.WARLORD), new Character(CharacterType.KING), new Character(CharacterType.MERCHANT), new Character(CharacterType.BISHOP), new Character(CharacterType.ASSASSIN)};
        Character[] selectable = new Character[]{new Character(CharacterType.KING), new Character(CharacterType.BISHOP), new Character(CharacterType.WARLORD), new Character(CharacterType.MERCHANT)};
        assertEquals(player2.strategy().chooseCharacter(selectable, playable, gc.getPlayers()), new Character(CharacterType.WARLORD));
    }

    @Test
    void assassinKillsThief() {
        GameController gc = new GameController();
        Player player2 = new Player("player2", AggressiveStrategy.class);
        Player player3 = new Player("player3", AggressiveStrategy.class);
        gc.init(player2, player3);
        player2.chooseCharacter(new Character(CharacterType.ASSASSIN));
        Character[] selectable = new Character[]{new Character(CharacterType.KING), new Character(CharacterType.BISHOP), new Character(CharacterType.THIEF), new Character(CharacterType.MERCHANT)};
        assertNotEquals(player2.strategy().assassinAbility(selectable, gc.getPlayers()).getCharacter(), new Character(CharacterType.THIEF));
        player2.giveGold(5);
        assertEquals(player2.strategy().assassinAbility(selectable, gc.getPlayers()).getCharacter(), new Character(CharacterType.THIEF));
    }

    @Test
    void assassinKillsWarlord() {
        GameController gc = new GameController();
        Player player2 = new Player("player2", AggressiveStrategy.class);
        Player player3 = new Player("player3", AggressiveStrategy.class);
        gc.init(player2, player3);
        player2.chooseCharacter(new Character(CharacterType.ASSASSIN));
        Character[] selectable = new Character[]{new Character(CharacterType.KING), new Character(CharacterType.WARLORD), new Character(CharacterType.THIEF), new Character(CharacterType.MERCHANT)};
        assertNotEquals(player2.strategy().assassinAbility(selectable, gc.getPlayers()).getCharacter(), new Character(CharacterType.WARLORD));
        player2.getDistrictsBoard().addDistricts(districtTemple, districtWatchtower, districtBarracks, districtObservatory, districtStall, districtFortress, districtMonastery);
        player2.getDistrictsHand().addDistricts(districtTavern);
        assertNotEquals(player2.strategy().assassinAbility(selectable, gc.getPlayers()).getCharacter(), new Character(CharacterType.WARLORD));
        player2.giveGold(1);
        assertEquals(player2.strategy().assassinAbility(selectable, gc.getPlayers()).getCharacter(), new Character(CharacterType.WARLORD));
    }

    @Test
    void assassinKillsMagician() {
        GameController gc = new GameController();
        Player player2 = new Player("player2", AggressiveStrategy.class);
        Player player3 = new Player("player3", AggressiveStrategy.class);
        gc.init(player2, player3);
        player2.chooseCharacter(new Character(CharacterType.ASSASSIN));
        Character[] selectable = new Character[]{new Character(CharacterType.KING), new Character(CharacterType.WARLORD), new Character(CharacterType.MAGICIAN), new Character(CharacterType.MERCHANT)};
        assertNotEquals(player2.strategy().assassinAbility(selectable, gc.getPlayers()).getCharacter(), new Character(CharacterType.MAGICIAN));
        player2.getDistrictsHand().addDistricts(districtTemple, districtWatchtower);
        assertNotEquals(player2.strategy().assassinAbility(selectable, gc.getPlayers()).getCharacter(), new Character(CharacterType.MAGICIAN));
        player2.getDistrictsHand().addDistricts(districtBarracks);
        assertEquals(player2.strategy().assassinAbility(selectable, gc.getPlayers()).getCharacter(), new Character(CharacterType.MAGICIAN));
    }

    @Test
    void assassinKillsArchitect() {
        GameController gc = new GameController();
        Player player2 = new Player("player2", AggressiveStrategy.class);
        Player player3 = new Player("player3", AggressiveStrategy.class);
        gc.init(player2, player3);
        player2.chooseCharacter(new Character(CharacterType.ASSASSIN));
        Character[] selectable = new Character[]{new Character(CharacterType.KING), new Character(CharacterType.ARCHITECT), new Character(CharacterType.MAGICIAN), new Character(CharacterType.MERCHANT)};
        assertNotEquals(player2.strategy().assassinAbility(selectable, gc.getPlayers()).getCharacter(), new Character(CharacterType.ARCHITECT));
        player3.giveGold(4);
        player3.getDistrictsHand().addDistricts(districtTemple);
        assertNotEquals(player2.strategy().assassinAbility(selectable, gc.getPlayers()).getCharacter(), new Character(CharacterType.ARCHITECT));
        player3.getDistrictsBoard().addDistricts(districtWatchtower, districtBarracks, districtTavern, districtStall, districtFortress);
        assertEquals(player2.strategy().assassinAbility(selectable, gc.getPlayers()).getCharacter(), new Character(CharacterType.ARCHITECT));
    }

    @Test
    void warlordTest() {
        GameController gc = new GameController();
        Player player2 = new Player("player2", AggressiveStrategy.class);
        Player player3 = new Player("player3", AggressiveStrategy.class);
        gc.init(player2, player3);
        player2.chooseCharacter(new Character(CharacterType.WARLORD));
        assertNull(player2.strategy().warlordAbility(gc.getPlayers()));
        player3.giveGold(4);
        player3.getDistrictsHand().addDistricts(districtTemple);
        player3.getDistrictsBoard().addDistricts(districtBarracks, districtBarracks, districtBarracks, districtBarracks, districtWatchtower, districtBarracks, districtBarracks);
        AbilityResult ar = player2.strategy().warlordAbility(gc.getPlayers());
        assertNotNull(ar);
        assertTrue(player3.getDistrictsBoard().contains(ar.getDistrict()));
        assertEquals(districtWatchtower, ar.getDistrict());
    }

    @Test
    void magicianTest() {
        GameController gc = new GameController();
        Player player2 = new Player("player2", AggressiveStrategy.class);
        Player player3 = new Player("player3", AggressiveStrategy.class);
        gc.init(player2, player3);
        player3.chooseCharacter(new Character(CharacterType.BISHOP));
        player2.chooseCharacter(new Character(CharacterType.MAGICIAN));
        assertNull(player2.strategy().magicianAbility(gc.getPlayers()));
        player3.giveGold(4);
        player3.getDistrictsHand().addDistricts(districtTemple);
        player3.getDistrictsBoard().addDistricts(districtBarracks, districtBarracks, districtBarracks, districtBarracks, districtWatchtower, districtBarracks, districtBarracks);
        AbilityResult ar = player2.strategy().magicianAbility(gc.getPlayers());
        assertNotNull(ar);
        assertEquals(player3.getCharacter(), ar.getCharacter());
    }

    @Test
    void chooseGoldOrDistrict() {
        player.getDistrictsHand().addDistricts(districtTemple);
        player.getDistrictsBoard().addDistricts(districtWatchtower, districtBarracks);
        PlayerAction result = player.strategy().chooseGoldOrDistrict();
        assertEquals(PlayerAction.PICK_GOLDS, result);
        assertNotEquals(PlayerAction.PICK_DISTRICTS, result);
    }

    @Test
    void chooseGoldOrDistrict2() {
        player.getDistrictsHand().addDistricts(districtWatchtower);
        player.getDistrictsBoard().addDistricts(districtTemple, districtBarracks);
        PlayerAction result = player.strategy().chooseGoldOrDistrict();
        assertNotEquals(PlayerAction.PICK_GOLDS, result);
        assertEquals(PlayerAction.PICK_DISTRICTS, result);
    }

    @Test
    void chooseDistrict() {
        player.getDistrictsHand().addDistricts(districtBarracks);
        District chosenDistrict = player.strategy().chooseDistrict(districtTemple, districtWatchtower);
        assertEquals(districtTemple, chosenDistrict); // Il choisit le temple puisqu'il dispose deja d'un quartier rouge
    }

    @Test
    void chooseDistrict2() {
        player.getDistrictsHand().addDistricts(districtTemple);
        District chosenDistrict = player.strategy().chooseDistrict(districtWatchtower, districtBarracks);
        assertEquals(chosenDistrict.getColor(), DistrictColor.red); // On ne peut pas choisir, car il prend aléatoirement entre les deux rouges
    }

    @Test
    void placeDistrict() {
        player.giveGold(50);
        District toPlace = new District(DistrictType.COURT_OF_MIRACLES);
        player.getDistrictsBoard().addDistricts(new District(DistrictType.MANOR));
        IntStream.range(0, 5).forEach(i -> player.getDistrictsHand().addDistricts(new District(DistrictType.CASTLE)));
        player.getDistrictsHand().addDistricts(toPlace);
        player.strategy().placeDistrict();
        assertTrue(player.getDistrictsBoard().contains(toPlace));
    }

    @Test
    void placeDistrict2() {
        player.giveGold(50);
        District toPlace = new District(DistrictType.COURT_OF_MIRACLES);
        player.getDistrictsBoard().addDistricts(new District(DistrictType.MANOR));
        IntStream.range(0, 5).forEach(i -> player.getDistrictsHand().addDistricts(new District(DistrictType.CASTLE)));
        player.getDistrictsHand().addDistricts(new District(DistrictType.OBSERVATORY), toPlace);
        player.strategy().placeDistrict();
        assertTrue(player.getDistrictsBoard().contains(toPlace));
    }

    @Test
    void placeDistrict3() {
        player.giveGold(1);
        District toPlace = new District(DistrictType.WATCHTOWER);
        player.getDistrictsBoard().addDistricts(new District(DistrictType.MANOR));
        IntStream.range(0, 5).forEach(i -> player.getDistrictsHand().addDistricts(new District(DistrictType.CASTLE)));
        player.getDistrictsHand().addDistricts(toPlace, new District(DistrictType.COURT_OF_MIRACLES));
        player.strategy().placeDistrict();
        assertTrue(player.getDistrictsBoard().contains(toPlace));
    }

    @Test
    void switchStrategy() {
        assertEquals(FiveColorsAggressiveStrategy.class.getSimpleName(), player.getStrategy().getSimpleName());
        player.getDistrictsBoard().add(new District(DistrictType.MANOR));
        player.getDistrictsBoard().add(new District(DistrictType.TEMPLE));
        player.getDistrictsBoard().add(new District(DistrictType.TAVERN));
        player.getDistrictsBoard().add(new District(DistrictType.WATCHTOWER));
        player.switchStrategy();
        assertEquals(FiveColorsAggressiveStrategy.class.getSimpleName(), player.getStrategy().getSimpleName());
        player.getDistrictsBoard().add(new District(DistrictType.COURT_OF_MIRACLES));
        assertEquals(FiveColorsAggressiveStrategy.class.getSimpleName(), player.getStrategy().getSimpleName());
        player.switchStrategy();
        assertNotEquals(FiveColorsAggressiveStrategy.class.getSimpleName(), player.getStrategy().getSimpleName());
    }

    @Test
    void switchStrategyCheap() {

        Districts districts = new Districts();
        player.setGold(30);
        District districtUniversity = new District(DistrictType.UNIVERSITY);
        District districtDraconianHarbour = new District(DistrictType.DRACONIAN_HARBOR);
        District districtHarbor = new District(DistrictType.HARBOR);
        District districtBarracks = new District(DistrictType.BARRACKS);
        District districtMonastery = new District(DistrictType.MONASTERY);
        District districtManor = new District(DistrictType.MANOR);
        districts.addDistricts(districtUniversity, districtDraconianHarbour, districtHarbor, districtBarracks, districtMonastery, districtManor);
        player.setDistrictsHand(districts);

        player.placeDistrict(districtUniversity);
        player.placeDistrict(districtDraconianHarbour);
        player.placeDistrict(districtHarbor);
        player.placeDistrict(districtBarracks);
        player.placeDistrict(districtMonastery);
        player.placeDistrict(districtManor);

        assertTrue(player.getScore() > 26);
        player.switchStrategy();
        assertEquals(CheapDistrictStrategy.class.getSimpleName(), player.getStrategy().getSimpleName());
    }

    @Test
    void switchStrategyCheap2() {

        Districts districts = new Districts();
        player.setGold(30);
        District districtUniversity = new District(DistrictType.UNIVERSITY);
        District districtChurch = new District(DistrictType.CHURCH);
        District districtHarbor = new District(DistrictType.HARBOR);
        District districtBarracks = new District(DistrictType.BARRACKS);
        District districtMonastery = new District(DistrictType.MONASTERY);
        District districtManor = new District(DistrictType.MANOR);
        districts.addDistricts(districtUniversity, districtChurch, districtHarbor, districtBarracks, districtMonastery, districtManor);
        player.setDistrictsHand(districts);

        player.placeDistrict(districtUniversity);
        player.placeDistrict(districtChurch);
        player.placeDistrict(districtHarbor);
        player.placeDistrict(districtBarracks);
        player.placeDistrict(districtMonastery);
        player.placeDistrict(districtManor);

        assertEquals(26, player.getScore());
        player.switchStrategy();
        assertEquals(CheapDistrictStrategy.class.getSimpleName(), player.getStrategy().getSimpleName());
    }

    @Test
    void switchStrategyExp() {

        Districts districts = new Districts();
        player.setGold(30);
        District districtUniversity = new District(DistrictType.UNIVERSITY);
        District districtHarbor = new District(DistrictType.HARBOR);
        District districtBarracks = new District(DistrictType.BARRACKS);
        District districtMonastery = new District(DistrictType.MONASTERY);
        District districtManor = new District(DistrictType.MANOR);
        districts.addDistricts(districtUniversity, districtHarbor, districtBarracks, districtMonastery, districtManor);
        player.setDistrictsHand(districts);

        player.placeDistrict(districtUniversity);
        player.placeDistrict(districtHarbor);
        player.placeDistrict(districtBarracks);
        player.placeDistrict(districtMonastery);
        player.placeDistrict(districtManor);

        assertTrue(player.getScore() < 26);
        player.switchStrategy();
        assertEquals(ExpGoldStrategy.class.getSimpleName(), player.getStrategy().getSimpleName());
    }

    @Test
    void whatToDoTestPlaceDistrict() {
        player.chooseCharacter(new Character(CharacterType.KING));
        player.getDistrictsHand().addDistricts(districtWatchtower);
        player.giveGold(10);
        assertEquals(player.strategy().whatToDo(), PlayerAction.PLACE_DISTRICT);
    }

    @Test
    void whatToDoTestUseAbilityAssassin() {
        player.chooseCharacter(new Character(CharacterType.ASSASSIN));
        assertEquals(player.strategy().whatToDo(), PlayerAction.USE_ABILITY);
    }

    @Test
    void whatToDoTestUseAbilityWarlord() {
        player.chooseCharacter(new Character(CharacterType.WARLORD));
        assertEquals(player.strategy().whatToDo(), PlayerAction.USE_ABILITY);
    }

    @Test
    void whatToDoTestUseAbilityThief() {
        player.chooseCharacter(new Character(CharacterType.THIEF));
        assertEquals(player.strategy().whatToDo(), PlayerAction.USE_ABILITY);
    }

    @Test
    void whatToDoTestUseAbilityMagician() {
        player.chooseCharacter(new Character(CharacterType.MAGICIAN));
        assertEquals(player.strategy().whatToDo(), PlayerAction.USE_ABILITY);
    }

    @Test
    void whatToDoTestCollectGoldsKing() {
        player.chooseCharacter(new Character(CharacterType.KING));
        player.getDistrictsBoard().addDistricts(new District(DistrictType.CASTLE));
        player.giveGold(-10);
        assertEquals(player.strategy().whatToDo(), PlayerAction.COLLECT_GOLDS_FROM_DISTRICTS);
    }

    @Test
    void whatToDoTestCollectGoldsMerchant() {
        player.chooseCharacter(new Character(CharacterType.MERCHANT));
        player.getDistrictsBoard().addDistricts(new District(DistrictType.TAVERN));
        player.giveGold(-10);
        assertEquals(player.strategy().whatToDo(), PlayerAction.COLLECT_GOLDS_FROM_DISTRICTS);
    }

    @Test
    void whatToDoTestCollectGoldsWarlord() {
        player.chooseCharacter(new Character(CharacterType.WARLORD));
        player.addAction(PlayerAction.USE_ABILITY);
        player.giveGold(-10);
        player.getDistrictsBoard().addDistricts(districtWatchtower);
        assertEquals(player.strategy().whatToDo(), PlayerAction.COLLECT_GOLDS_FROM_DISTRICTS);
    }

    @Test
    void whatToDoTestFinishKingWithNoYellow() {
        player.chooseCharacter(new Character(CharacterType.KING));
        player.giveGold(-10);
        assertEquals(player.strategy().whatToDo(), PlayerAction.FINISH);
    }

    @Test
    void whatToDoTestFinishNoBoard() {
        Player emptyBoardPlayer = player = new Player("AggressiveStrategy", AggressiveStrategy.class);
        emptyBoardPlayer.chooseCharacter(new Character(CharacterType.ARCHITECT));
        emptyBoardPlayer.giveGold(10);
        assertEquals(emptyBoardPlayer.strategy().whatToDo(), PlayerAction.FINISH);
    }

}
