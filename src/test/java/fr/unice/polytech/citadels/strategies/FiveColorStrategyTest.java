package fr.unice.polytech.citadels.strategies;

import fr.unice.polytech.citadels.cards.characters.Character;
import fr.unice.polytech.citadels.cards.characters.CharacterType;
import fr.unice.polytech.citadels.cards.districts.District;
import fr.unice.polytech.citadels.cards.districts.DistrictColor;
import fr.unice.polytech.citadels.cards.districts.DistrictType;
import fr.unice.polytech.citadels.players.Districts;
import fr.unice.polytech.citadels.players.Player;
import fr.unice.polytech.citadels.players.PlayerAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class FiveColorStrategyTest {
    Player player;
    District districtTemple, districtWatchtower, districtBarracks;

    @BeforeEach
    void setup() {
        // Pour éviter de polluer le sout de message d'action
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        player = new Player("Player", FiveColorsStrategy.class);
        districtTemple = new District(DistrictType.TEMPLE);
        districtWatchtower = new District(DistrictType.WATCHTOWER);
        districtBarracks = new District(DistrictType.BARRACKS);
    }

    @Test
    void chooseCharacterBishop() {
        giveFullColorSet(player);
        Character[] eveque = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        Character[] warlord = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN), new Character(CharacterType.WARLORD)};
        assertEquals(new Character(CharacterType.BISHOP), player.strategy().chooseCharacter(eveque, warlord, null));
    }

    @Test
    void chooseCharacterAssassin() {
        giveFullColorSet(player);
        Character[] assassin = new Character[]{new Character(CharacterType.THIEF), new Character(CharacterType.KING), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        Character[] warlord = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN), new Character(CharacterType.WARLORD)};
        assertEquals(new Character(CharacterType.ASSASSIN), player.strategy().chooseCharacter(assassin, warlord, null));
    }

    @Test
    void chooseCharacterMagician() {
        giveFullColorSet(player);
        player.getDistrictsBoard().remove(0);
        player.getDistrictsHand().addDistricts(new District(DistrictType.COUNTER), new District(DistrictType.CHURCH));
        Character[] magician = new Character[]{new Character(CharacterType.THIEF), new Character(CharacterType.KING), new Character(CharacterType.MAGICIAN), new Character(CharacterType.ASSASSIN)};
        Character[] warlord = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN), new Character(CharacterType.WARLORD)};
        assertEquals(new Character(CharacterType.MAGICIAN), player.strategy().chooseCharacter(magician, warlord, null));
    }

    @Test
    void chooseCharacterArchitect() {
        giveFullColorSet(player);
        player.getDistrictsBoard().remove(0);
        assertEquals(0, player.getDistrictsHand().size());
        Character[] architect = new Character[]{new Character(CharacterType.THIEF), new Character(CharacterType.ARCHITECT), new Character(CharacterType.MAGICIAN), new Character(CharacterType.ASSASSIN)};
        Character[] warlord = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN), new Character(CharacterType.WARLORD)};
        assertEquals(new Character(CharacterType.ARCHITECT), player.strategy().chooseCharacter(architect, warlord, null));
    }

    @Test
    void chooseCharacterMerchant() {
        giveFullColorSet(player);
        player.getDistrictsBoard().remove(0);
        player.getDistrictsHand().addDistricts(new District(DistrictType.MANOR));
        Character[] merchant = new Character[]{new Character(CharacterType.MERCHANT), new Character(CharacterType.ARCHITECT), new Character(CharacterType.MAGICIAN), new Character(CharacterType.ASSASSIN)};
        Character[] warlord = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN), new Character(CharacterType.WARLORD)};
        assertEquals(new Character(CharacterType.MERCHANT), player.strategy().chooseCharacter(merchant, warlord, null));
    }

    void giveFullColorSet(Player p) {
        p.getDistrictsBoard().addDistricts(new District(DistrictType.MANOR), new District(DistrictType.TEMPLE), new District(DistrictType.TAVERN), new District(DistrictType.WATCHTOWER), new District(DistrictType.COURT_OF_MIRACLES));
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
        assertEquals(FiveColorsStrategy.class.getSimpleName(), player.getStrategy().getSimpleName());
        player.getDistrictsBoard().add(new District(DistrictType.MANOR));
        player.getDistrictsBoard().add(new District(DistrictType.TEMPLE));
        player.getDistrictsBoard().add(new District(DistrictType.TAVERN));
        player.getDistrictsBoard().add(new District(DistrictType.WATCHTOWER));
        player.switchStrategy();
        assertEquals(FiveColorsStrategy.class.getSimpleName(), player.getStrategy().getSimpleName());
        player.getDistrictsBoard().add(new District(DistrictType.COURT_OF_MIRACLES));
        assertEquals(FiveColorsStrategy.class.getSimpleName(), player.getStrategy().getSimpleName());
        player.switchStrategy();
        assertNotEquals(FiveColorsStrategy.class.getSimpleName(), player.getStrategy().getSimpleName());
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
        player.getDistrictsHand().addDistricts(new District(DistrictType.STALL));
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
        player.getDistrictsBoard().addDistricts(new District(DistrictType.FORTRESS));
        player.giveGold(-10);
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