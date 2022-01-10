package fr.unice.polytech.citadels.players;

import fr.unice.polytech.citadels.cards.characters.Character;
import fr.unice.polytech.citadels.cards.characters.CharacterType;
import fr.unice.polytech.citadels.cards.districts.*;
import fr.unice.polytech.citadels.strategies.CheapDistrictStrategy;
import fr.unice.polytech.citadels.strategies.ExpGoldStrategy;
import fr.unice.polytech.citadels.strategies.RandomStrategy;
import fr.unice.polytech.citadels.strategies.Strategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player thomas;

    @BeforeEach
    void setUp() {
        // Pour eviter de polluer le out de message d'action
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        thomas = new Player("Thomas", ExpGoldStrategy.class);
    }

    @Test
    void nonExistentStrategy() {
        assertThrows(InstantiationException.class, () -> thomas.validateStrategy(Strategy.class));
    }

    @Test
    void getGold() {
        thomas.giveGold(5);
        assertEquals(5, thomas.getGold());
        thomas.giveGold(5);
        assertEquals(10, thomas.getGold());
    }

    @Test
    void getStrategy() {
        assertEquals(ExpGoldStrategy.class, thomas.getStrategy());
    }

    @Test
    void getStrategy2() {
        Player leo = new Player("Léo", CheapDistrictStrategy.class);
        assertEquals(CheapDistrictStrategy.class, leo.getStrategy());
    }

    @Test
    void isFirst() {
        assertEquals(0, thomas.getScore());
        thomas.isFirst();
        assertEquals(2, thomas.getScore());
    }

    @Test
    void getName() {
        assertEquals("Thomas", thomas.getName());
    }

    @Test
    void setStrategy() {
        try {
            thomas.setStrategy(RandomStrategy.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(RandomStrategy.class, thomas.getStrategy());
    }

    @Test
    void strategy() {
        Strategy strategy = new ExpGoldStrategy(thomas);
        assertEquals(strategy, thomas.strategy());
    }

    @Test
    void placeDistrict() {
        District d = new District(DistrictType.TAVERN);
        thomas.giveGold(1);
        thomas.getDistrictsHand().addDistricts(d);
        assertTrue(thomas.placeDistrict(d));
        assertFalse(thomas.placeDistrict(d));
    }

    @Test
    void addDistricts() {
        District d = new District(DistrictType.TOWN_HALL);
        thomas.getDistrictsHand().addDistricts(d,d,d,d);
        assertEquals(List.of(d,d,d,d), thomas.getDistrictsHand());
    }

    @Test
    void getDistrictsBoard() {
        assertEquals(List.of(), thomas.getDistrictsBoard());
        District d = new District(DistrictType.TOWN_HALL);
        thomas.getDistrictsBoard().addAll(List.of(d,d,d,d));
        assertEquals(List.of(d,d,d,d), thomas.getDistrictsBoard());
    }

    @Test
    void getDistrictsHand() {
        assertEquals(List.of(), thomas.getDistrictsHand());
        District d = new District(DistrictType.TOWN_HALL);
        thomas.getDistrictsHand().addAll(List.of(d,d,d,d));
        assertEquals(List.of(d,d,d,d), thomas.getDistrictsHand());
    }

    @Test
    void calculateScore() {
        Player leo = new Player("Léo", CheapDistrictStrategy.class);
        District c1 = new District(DistrictType.WATCHTOWER);
        District c2 = new District(DistrictType.PALACE);
        District c3= new District(DistrictType.MONASTERY);
        District c4 = new District(DistrictType.STALL);
        District c5 = new District(DistrictType.COURT_OF_MIRACLES);
        leo.getDistrictsHand().addDistricts(c1,c2,c3,c4,c5);
        leo.giveGold(20);
        leo.placeDistrict(c1);
        leo.placeDistrict(c2);
        leo.placeDistrict(c3);
        leo.placeDistrict(c4);
        leo.placeDistrict(c5);
        assertEquals(leo.getScore(),16); //Test où le joueur a 5 quartiers avec les 5 couleurs (bonus +3)
    }

    @Test
    void calculateScore2() {
        Player leo = new Player("Léo", CheapDistrictStrategy.class);
        District c1 = new District(DistrictType.WATCHTOWER);
        District c2 = new District(DistrictType.PALACE);
        District c3= new District(DistrictType.MONASTERY);
        District c4 = new District(DistrictType.STALL);
        District c5 = new District(DistrictType.WATCHTOWER);
        leo.getDistrictsHand().addDistricts(c1,c2,c3,c4,c5);
        leo.giveGold(20);
        leo.placeDistrict(c1);
        leo.placeDistrict(c2);
        leo.placeDistrict(c3);
        leo.placeDistrict(c4);
        leo.placeDistrict(c5);
        assertEquals(leo.getScore(),12); //Test où le joueur a 5 quartiers et aucun bonus
    }
    @Test
    void calculateScore3() {
        Player leo = new Player("Léo", CheapDistrictStrategy.class);
        District c1 = new District(DistrictType.WATCHTOWER);
        District c2 = new District(DistrictType.PALACE);
        District c3= new District(DistrictType.MONASTERY);
        District c4 = new District(DistrictType.STALL);
        District c5 = new District(DistrictType.WATCHTOWER);
        District c6 = new District(DistrictType.TAVERN);
        District c7 = new District(DistrictType.TEMPLE);
        District c8 = new District(DistrictType.WATCHTOWER);
        leo.getDistrictsHand().addDistricts(c1,c2,c3,c4,c5,c6,c7,c8);
        leo.giveGold(20);
        leo.placeDistrict(c1);
        leo.placeDistrict(c2);
        leo.placeDistrict(c3);
        leo.placeDistrict(c4);
        leo.placeDistrict(c5);
        leo.placeDistrict(c6);
        leo.placeDistrict(c7);
        leo.placeDistrict(c8);
        assertEquals(leo.getScore(),17); //Test où le joueur a 8 quartiers et n'a pas fini premier (bonus +2)
    }
    @Test
    void calculateScore4() {
        Player leo = new Player("Léo", CheapDistrictStrategy.class);
        District c1 = new District(DistrictType.WATCHTOWER);
        District c2 = new District(DistrictType.PALACE);
        District c3= new District(DistrictType.MONASTERY);
        District c4 = new District(DistrictType.STALL);
        District c5 = new District(DistrictType.WATCHTOWER);
        District c6 = new District(DistrictType.TAVERN);
        District c7 = new District(DistrictType.TEMPLE);
        District c8 = new District(DistrictType.WATCHTOWER);
        leo.getDistrictsHand().addDistricts(c1,c2,c3,c4,c5,c6,c7,c8);
        leo.giveGold(20);
        leo.placeDistrict(c1);
        leo.placeDistrict(c2);
        leo.placeDistrict(c3);
        leo.placeDistrict(c4);
        leo.placeDistrict(c5);
        leo.placeDistrict(c6);
        leo.placeDistrict(c7);
        leo.placeDistrict(c8);
        leo.isFirst();
        assertEquals(leo.getScore(),19); //Test où le joueur a 8 quartiers et a fini premier (bonus +4)
    }

    @Test
    void collectGoldFromDistricts1() {
        Player leo = new Player("Léo", CheapDistrictStrategy.class);
        District c1 = new District(DistrictType.WATCHTOWER);
        District c2 = new District(DistrictType.WATCHTOWER);
        District c3 = new District(DistrictType.WATCHTOWER);
        leo.giveGold(3);
        leo.getDistrictsHand().addDistricts(c1, c2, c3);
        leo.placeDistrict(c1);
        leo.placeDistrict(c2);
        leo.placeDistrict(c3);
        leo.chooseCharacter(new Character(CharacterType.WARLORD));
        assertEquals(leo.getGold(), 0);
        leo.collectGoldsFromDistricts();
        assertEquals(leo.getGold(), 3);
    }

    @Test
    void collectGoldFromDistricts2() {
        Player leo = new Player("Léo", CheapDistrictStrategy.class);
        District c4 = new District(DistrictType.TEMPLE);
        District c5 = new District(DistrictType.TEMPLE);
        leo.giveGold(2);
        leo.getDistrictsHand().addDistricts(c4,c5);
        leo.placeDistrict(c4);
        leo.placeDistrict(c5);
        leo.chooseCharacter(new Character(CharacterType.BISHOP));
        assertEquals(leo.getGold(),0);
        leo.collectGoldsFromDistricts();
        assertEquals(leo.getGold(),2);
    }

    @Test
    void collectGoldFromDistricts3() {
        Player leo = new Player("Léo", CheapDistrictStrategy.class);
        District c1 = new District(DistrictType.WATCHTOWER);
        District c2 = new District(DistrictType.MANOR);
        District c3 = new District(DistrictType.COURT_OF_MIRACLES);
        District c4 = new District(DistrictType.TAVERN);
        District c5 = new District(DistrictType.TEMPLE);
        leo.giveGold(5);
        leo.getDistrictsHand().addDistricts(c1, c2, c3, c4, c5);
        leo.placeDistrict(c1);
        leo.placeDistrict(c2);
        leo.placeDistrict(c3);
        leo.placeDistrict(c4);
        leo.placeDistrict(c5);
        leo.chooseCharacter(new Character(CharacterType.ARCHITECT));
        assertEquals(leo.getGold(), 0);
        leo.collectGoldsFromDistricts();
        assertEquals(leo.getGold(), 0);
    }

    @Test
    void chooseCharacter(){
        Player leo = new Player("Léo", CheapDistrictStrategy.class);
        assertNull(leo.getCharacter());
        leo.chooseCharacter(new Character(CharacterType.WARLORD));
        assertEquals(leo.getCharacter().toString(),new Character(CharacterType.WARLORD).toString());
        leo.chooseCharacter(new Character(CharacterType.BISHOP));
        assertEquals(leo.getCharacter().toString(),new Character(CharacterType.BISHOP).toString());
    }

    @Test
    void removeBoardDistrict(){
        Player player = new Player("player",ExpGoldStrategy.class);
        District district = new District(DistrictType.WATCHTOWER);
        List<District> districtsBoard = new ArrayList<>();

        player.getDistrictsHand().addDistricts(district);
        player.placeDistrict(district);
        player.getDistrictsBoard().removeDistricts(district);
        assertEquals(player.getDistrictsBoard(),districtsBoard);
    }

    @Test
    void hasPlacedDistrict(){
        Player player = new Player("player",ExpGoldStrategy.class);
        District district = new District(DistrictType.WATCHTOWER);
        player.giveGold(DistrictType.WATCHTOWER.getPrice());
        player.getDistrictsHand().addDistricts(district);
        player.placeDistrict(district);
        assertTrue(player.hasPlacedDistrict(district));
    }

    @Test
    void canPlaceMultipleDistricts(){
        District d1 = new District(DistrictType.BARRACKS);
        District d2 = new District(DistrictType.BARRACKS);
        District d3 = new District(DistrictType.BARRACKS);
        thomas.getDistrictsHand().addDistricts(d1, d2, d3);
        thomas.giveGold(5);
        assertFalse(thomas.canPlaceMultipleDistricts(1));
        thomas.giveGold(1);
        assertTrue(thomas.canPlaceMultipleDistricts(1));
    }

    @Test
    void giveCrown(){
        Player player = new Player("player",ExpGoldStrategy.class);
        player.giveCrown();
        assertTrue(player.getCrown());
    }

    @Test
    void removeCrown(){
        Player player = new Player("player",ExpGoldStrategy.class);
        player.removeCrown();
        assertFalse(player.getCrown());
    }

    @Test
    void giveDistrict(){
        Player player = new Player("player",ExpGoldStrategy.class);
        District district1 = new District(DistrictType.WATCHTOWER);
        District district2 = new District(DistrictType.WATCHTOWER);
        List<District> districtHand = new ArrayList<>();

        districtHand.add(district1);
        districtHand.add(district2);
        player.getDistrictsHand().addDistricts(district1,district2);
        assertEquals(player.getDistrictsHand(),districtHand);
    }

    @Test
    void setGold(){
        Player player = new Player("player",ExpGoldStrategy.class);
        player.setGold(5);
        assertEquals(5,player.getGold());
    }

    @Test
    void setDistrictsHand(){
        Player player = new Player("player",ExpGoldStrategy.class);
        District district1 = new District(DistrictType.WATCHTOWER);
        District district2 = new District(DistrictType.WATCHTOWER);
        Districts districtHand = new Districts();

        districtHand.add(district1);
        districtHand.add(district2);
        player.setDistrictsHand(districtHand);
        assertEquals(player.getDistrictsHand(),districtHand);
    }

    @Test
    void getCharactersToChoose(){
        Player player = new Player("player",ExpGoldStrategy.class);
        Character perso1 = new Character(CharacterType.ASSASSIN);
        Character perso2 = new Character(CharacterType.WARLORD);
        List<Character> charactersToChoose = new ArrayList<>();

        charactersToChoose.add(perso1);
        charactersToChoose.add(perso2);
        player.getCharactersToChoose().addAll(List.of(perso1,perso2));
        assertEquals(player.getCharactersToChoose(),charactersToChoose);
    }

    @Test
    void characterChoosen(){
        Player player = new Player("player", RandomStrategy.class);
        Character bishop = new Character(CharacterType.BISHOP);
        player.chooseCharacter(bishop);
        assertEquals(player.characterChosen(),bishop);
    }

    @Test
    void getUsesAbilityFalse(){
        Player player = new Player("joueur",ExpGoldStrategy.class);
        assertFalse(player.getUsesAbility());
    }

    @Test
    void setUsesAbility(){
        Player player = new Player("player",ExpGoldStrategy.class);
        player.setUsesAbility(true);
        assertTrue(player.getUsesAbility());
    }

    @Test
    void useLaboratory() {
        Player player = new Player("player", RandomStrategy.class);
        District d = new District(DistrictType.LABORATORY);
        District toDraw = new District(DistrictType.BARRACKS);
        player.getDistrictsBoard().addDistricts(d);
        player.getDistrictsHand().addDistricts(toDraw);

        assertEquals(0, player.getGold());
        assertEquals(toDraw, player.useLaboratory(toDraw));
        assertEquals(1, player.getGold());
    }

    @Test
    void useManufacture() {
        Player player = new Player("player", RandomStrategy.class);
        District d = new District(DistrictType.MANUFACTURE);
        player.giveGold(3);
        player.getDistrictsBoard().addDistricts(d);

        DistrictList dl = new DistrictFactory().getDistrictList();

        assertEquals(0, player.getDistrictsHand().size());
        player.useManufacture(dl.pick(3));
        assertEquals(0, player.getGold());
        assertEquals(3, player.getDistrictsHand().size());
    }

    @Test
    void useCemetery() {
        Player player = new Player("player", ExpGoldStrategy.class);
        District d = new District(DistrictType.CEMETERY);
        player.giveGold(1);
        player.getDistrictsBoard().addDistricts(d);
        District toSave = new District(DistrictType.BARRACKS);

        assertEquals(0, player.getDistrictsHand().size());
        if(player.askForRetrieveInHand(toSave)) {
            player.useCemetery(toSave);
            assertEquals(0, player.getGold());
            assertEquals(1, player.getDistrictsHand().size());
        } else {
            player.useCemetery(toSave);
            assertEquals(1, player.getGold());
            assertEquals(0, player.getDistrictsHand().size());
        }
    }

    @Test
    void schoolOfMagic() {
        Player player = new Player("player", RandomStrategy.class);
        player.chooseCharacter(new Character(CharacterType.WARLORD));
        District a = new District(DistrictType.BARRACKS);
        District b = new District(DistrictType.MANOR);
        District c = new District(DistrictType.JAIL);
        District d = new District(DistrictType.SCHOOL_OF_MAGIC);
        player.getDistrictsBoard().addDistricts(a, b, c, d);

        assertEquals(0, player.getGold());
        player.collectGoldsFromDistricts();
        assertEquals(3, player.getGold()); // 2 quatiers + école de magie

        player.chooseCharacter(new Character(CharacterType.KING));

        player.collectGoldsFromDistricts();
        assertEquals(5, player.getGold()); // 1 quatier + école de magie
    }

    @Test
    void kill(){
        Player player = new Player("player", RandomStrategy.class);
        player.kill();
        assertFalse(player.canPlay());
    }

    @Test
    void canPlay(){
        Player player = new Player("player", RandomStrategy.class);
        assertTrue(player.canPlay());
    }

    @Test
    void canPlayFalse(){
        Player player = new Player("player", RandomStrategy.class);
        player.kill();
        assertFalse(player.canPlay());
    }

    @Test
    void steal(){
        Player player = new Player("player", RandomStrategy.class);
        player.steal();
        assertTrue(player.hasBeenStolen());
    }

    @Test
    void hasBeenStolen(){
        Player player = new Player("player", RandomStrategy.class);
        Player player2 = new Player("player2", RandomStrategy.class);
        player.steal();
        assertTrue(player.hasBeenStolen());
        assertFalse(player2.hasBeenStolen());
    }

    @Test
    void observatory() {
        Player player = new Player("player", RandomStrategy.class);
        assertEquals(2, player.getCardToPick());
        player.getDistrictsBoard().addDistricts(new District(DistrictType.OBSERVATORY));
        assertEquals(3, player.getCardToPick());
    }

    @Test
    void getDistrictPlacableNew(){
        Player player = new Player("player", RandomStrategy.class);
        assertEquals(1,player.getDistrictPlacable());
    }

    @Test
    void getDistrictPlacable(){
        Player player = new Player("player", RandomStrategy.class);
        player.chooseCharacter(new Character(CharacterType.ARCHITECT));
        assertEquals(3,player.getDistrictPlacable());
    }

    @Test
    void getStartingStrategy(){
        Player player = new Player("player", ExpGoldStrategy.class);
        assertEquals(ExpGoldStrategy.class,player.getStrategy());
    }

    @Test
    void getDistrictsBoardSize(){
        Player player = new Player("player", ExpGoldStrategy.class);
        District district = new District(DistrictType.JAIL);

        player.setGold(10);
        player.chooseDistrict(district);
        player.placeDistrict(district);
        assertEquals(player.getDistrictBoardSize(),1);

    }

}