package fr.unice.polytech.citadels.strategies;

import fr.unice.polytech.citadels.Logger;
import fr.unice.polytech.citadels.cards.characters.Character;
import fr.unice.polytech.citadels.cards.characters.CharacterFactory;
import fr.unice.polytech.citadels.cards.characters.CharacterList;
import fr.unice.polytech.citadels.cards.characters.CharacterType;
import fr.unice.polytech.citadels.cards.characters.abilities.AbilityResult;
import fr.unice.polytech.citadels.cards.districts.DistrictColor;
import fr.unice.polytech.citadels.cards.districts.District;
import fr.unice.polytech.citadels.cards.districts.DistrictType;
import fr.unice.polytech.citadels.players.Player;
import fr.unice.polytech.citadels.players.PlayerAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CheapDistrictStrategyTest {
    private Player player;

    @BeforeEach
    void setUp() {
        // Pour éviter de polluer le sout de message d'action
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        player = new Player("CheapDistrictStrategy", CheapDistrictStrategy.class);
    }

    @Test
    void chooseGoldOrDistrict_GOLD() {
        player.strategy().chooseGoldOrDistrict();
        PlayerAction result = player.strategy().chooseGoldOrDistrict();
        assertNotEquals(PlayerAction.PICK_DISTRICTS, result);
        assertEquals(PlayerAction.PICK_GOLDS, result);
        // Le joueur n'a pas d'or alors il va choisir de l'or
    }

    @Test
    void chooseCharacterTest(){
        CharacterList characters = new CharacterList();
        characters.add(new Character(CharacterType.WARLORD));
        characters.add(new Character(CharacterType.KING));
        characters.getType(CharacterType.KING).discard();
        assertEquals(characters.getType(CharacterType.WARLORD), player.strategy().chooseCharacter(characters.playableCharacters(), characters.playableCharacters(), null));
        characters.clear();
        characters.add(new Character(CharacterType.BISHOP));
        assertEquals(characters.getType(CharacterType.BISHOP), player.strategy().chooseCharacter(characters.playableCharacters(), characters.playableCharacters(), null));
    }

    @Test
    void chooseGoldOrDistrict_DISTRICT() {
        player.giveGold(2);
        player.strategy().chooseGoldOrDistrict();
        PlayerAction result = player.strategy().chooseGoldOrDistrict();
        assertEquals(PlayerAction.PICK_DISTRICTS, result);
        assertNotEquals(PlayerAction.PICK_GOLDS, result);
        // Le joueur a de l'or alors il va choisir de piocher
    }

    @Test
    void chooseDistrict() {
        District districtTemple = new District(DistrictType.TEMPLE);
        District districtCathedral = new District(DistrictType.CATHEDRAL);
        player.chooseDistrict(districtTemple, districtCathedral);
        assertTrue(player.getDistrictsHand().contains(districtTemple));
        // Le joueur va toujours choisir le quartier le moins cher
    }

    @Test
    void chooseDistrict2() {
        District districtCathedral = new District(DistrictType.CATHEDRAL);
        District districtHarbor = new District(DistrictType.HARBOR);
        player.chooseDistrict(districtCathedral, districtHarbor);
        assertTrue(player.getDistrictsHand().contains(districtHarbor));
        // Le joueur va toujours choisir le quartier le moins cher
    }

    @Test
    void placeDistrict() {
        District districtCathedral = new District(DistrictType.CATHEDRAL);
        District districtHarbor = new District(DistrictType.HARBOR);
        player.getDistrictsHand().addAll(List.of(districtCathedral, districtHarbor));
        player.giveGold(4);
        player.strategy().placeDistrict();
        assertTrue(player.getDistrictsBoard().contains(districtHarbor));
        //Le joueur va toujours placé le quartier le moins
    }

    @Test
    void placeDistrict2() {
        District districtCathedral = new District(DistrictType.CATHEDRAL);
        District districtHarbor = new District(DistrictType.HARBOR);
        player.getDistrictsHand().addAll(List.of(districtCathedral, districtHarbor));
        player.giveGold(3);
        player.strategy().placeDistrict();
        assertFalse(player.getDistrictsBoard().contains(districtCathedral));
        assertFalse(player.getDistrictsBoard().contains(districtHarbor));
        // S'il n'a pas le moyen de prendre le quartier le moins chere, alors il ne fait rien
    }

    @Test
    void whatToDoTestPlaceDistrict() {
        player.chooseCharacter(new Character(CharacterType.KING));
        player.getDistrictsHand().addDistricts(new District(DistrictType.TAVERN));
        player.giveGold(5);
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
        Player emptyBoardPlayer = player = new Player("CheapDistrictStrategy", CheapDistrictStrategy.class);
        emptyBoardPlayer.chooseCharacter(new Character(CharacterType.ARCHITECT));
        emptyBoardPlayer.giveGold(10);
        assertEquals(emptyBoardPlayer.strategy().whatToDo(), PlayerAction.FINISH);
    }

}