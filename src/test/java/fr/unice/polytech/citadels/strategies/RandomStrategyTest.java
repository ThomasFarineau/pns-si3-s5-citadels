package fr.unice.polytech.citadels.strategies;

import fr.unice.polytech.citadels.cards.characters.Character;
import fr.unice.polytech.citadels.cards.characters.CharacterFactory;
import fr.unice.polytech.citadels.cards.characters.CharacterList;
import fr.unice.polytech.citadels.cards.characters.CharacterType;
import fr.unice.polytech.citadels.cards.characters.abilities.AbilityResult;
import fr.unice.polytech.citadels.cards.characters.abilities.AbilityType;
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

class RandomStrategyTest {
    private Player player;

    @BeforeEach
    void setUp() {
        // Pour éviter de polluer le sout de message d'action
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        player = new Player("RandomStrategy", RandomStrategy.class);
    }

    @Test
    void chooseGoldOrDistrict() {
        PlayerAction result = player.strategy().chooseGoldOrDistrict();
        assertTrue(result == PlayerAction.PICK_GOLDS ^ result == PlayerAction.PICK_DISTRICTS);
    }

    @Test
    void chooseGoldOrDistrict_GOLD() {
        PlayerAction result = player.strategy().chooseGoldOrDistrict();
        while (result != PlayerAction.PICK_GOLDS) result = player.strategy().chooseGoldOrDistrict();
        assertEquals(PlayerAction.PICK_GOLDS, result);
    }

    @Test
    void chooseGoldOrDistrict_DISTRICT() {
        PlayerAction result = player.strategy().chooseGoldOrDistrict();
        while (result != PlayerAction.PICK_DISTRICTS) result = player.strategy().chooseGoldOrDistrict();
        assertEquals(PlayerAction.PICK_DISTRICTS, result);
    }

    @Test
    void chooseDistrict() {
        District districtTemple = new District(DistrictType.TEMPLE);
        District districtCathedral = new District(DistrictType.CATHEDRAL);
        player.chooseDistrict(districtTemple, districtCathedral);
        assertTrue(player.getDistrictsHand().contains(districtTemple) ^ player.getDistrictsHand().contains(districtCathedral));
    }

    @Test
    void placeDistrict() {
        District districtTemple = new District(DistrictType.TEMPLE);
        District districtCathedral = new District(DistrictType.CATHEDRAL);
        player.chooseDistrict(districtTemple, districtCathedral);
        player.giveGold(4);
        player.strategy().placeDistrict();
        assertFalse(player.getDistrictsBoard().contains(districtTemple) && player.getDistrictsBoard().contains(districtCathedral));
    }

    @Test
    void warlordAbility() {
        int bound = 100;
        Player player1;
        Player player2;
        District districtFortress = new District(DistrictType.FORTRESS);
        District districtMonastery = new District(DistrictType.MONASTERY);
        for (int i = 0; i < bound; i++) {
            player1 = new Player("Player1", RandomStrategy.class);
            player1.chooseCharacter(new Character(CharacterType.BISHOP));
            player2 = new Player("Player2", RandomStrategy.class);
            player2.chooseCharacter(new Character(CharacterType.MERCHANT));
            player1.getDistrictsBoard().addDistricts(districtFortress, districtMonastery);
            player1.chooseCharacter(new Character(CharacterType.WARLORD));
            AbilityResult result = player1.ability(List.of(player1, player2), new CharacterList());
            assertTrue(result.getDistrict() == districtFortress || result.getDistrict() == districtMonastery);
            assertEquals(result.getType(), AbilityType.DESTROY_DISTRICT);
        }
    }

    @Test
    void magicianAbility() {
        int bound = 100;
        Player player2 = new Player("P1", RandomStrategy.class);
        District districtFortress = new District(DistrictType.FORTRESS);
        District districtMonastery = new District(DistrictType.MONASTERY);
        player2.getDistrictsHand().addDistricts(districtFortress);
        player.getDistrictsHand().addDistricts(districtMonastery);
        for (int i = 0; i < bound; i++) {
            player.chooseCharacter(new Character(CharacterType.MAGICIAN));
            AbilityResult result = player.ability(List.of(player2), new CharacterList());
            assertEquals(result.getCharacter(), player2.getCharacter());
            assertEquals(result.getType(), AbilityType.TRADE_CARDS);
        }
    }

    @Test
    void thiefAbility() {
        int bound = 100;
        for (int i = 0; i < bound; i++) {
            CharacterList characters = new CharacterFactory().getCharacters();
            characters.getType(CharacterType.THIEF).play(player);
            player.chooseCharacter(new Character(CharacterType.THIEF));
            AbilityResult result = player.strategy().ability(new ArrayList<>(), characters);
            assertNotSame(result.getCharacter().getType(), CharacterType.ASSASSIN);
            assertNotSame(result.getCharacter().getType(), CharacterType.THIEF);
            assertEquals(AbilityType.STEAL, result.getType());
        }
    }

    @Test
    void assassinAbility() {
        int bound = 100;
        for (int i = 0; i < bound; i++) {
            CharacterList characters = new CharacterFactory().getCharacters();
            characters.getType(CharacterType.ASSASSIN).play(player);
            player.chooseCharacter(new Character(CharacterType.ASSASSIN));
            AbilityResult result = player.strategy().ability(new ArrayList<>(), characters);
            assertNotSame(result.getCharacter().getType(), CharacterType.ASSASSIN);
            assertEquals(AbilityType.KILL, result.getType());
        }
    }
}