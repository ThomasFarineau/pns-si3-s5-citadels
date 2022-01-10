package fr.unice.polytech.citadels.strategies;

import fr.unice.polytech.citadels.cards.characters.Character;
import fr.unice.polytech.citadels.cards.characters.CharacterList;
import fr.unice.polytech.citadels.cards.characters.CharacterType;
import fr.unice.polytech.citadels.cards.characters.abilities.AbilityResult;
import fr.unice.polytech.citadels.cards.districts.District;
import fr.unice.polytech.citadels.cards.districts.DistrictType;
import fr.unice.polytech.citadels.players.Player;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class StrategyTest {

    @Test
    void getPlayer() {
        Player player = new Player("thomas", ExpGoldStrategy.class);
        assertEquals(player, player.strategy().getPlayer());
    }

    @Test
    void warlordAbility() {
        Player playerLeo = new Player("Leo", RandomStrategy.class);
        Player playerThomas = new Player("Thomas", RandomStrategy.class);
        Player playerValentin = new Player("Valentin", RandomStrategy.class);
        Player playerAlexandre = new Player("Alexandre", CheapDistrictStrategy.class);

        giveFullColorSet(playerLeo);
        playerLeo.chooseCharacter(new Character(CharacterType.MERCHANT));

        giveFullColorSet(playerThomas);
        playerThomas.chooseCharacter(new Character(CharacterType.KING));
        playerThomas.getDistrictsBoard().addDistricts(new District(DistrictType.MARKET));

        playerValentin.chooseCharacter(new Character(CharacterType.ASSASSIN));
        IntStream.range(0, 6).forEach(i -> playerValentin.getDistrictsBoard().addDistricts(new District(DistrictType.MANOR)));

        playerAlexandre.chooseCharacter(new Character(CharacterType.WARLORD));

        AbilityResult result = playerAlexandre.ability(List.of(playerLeo, playerThomas, playerValentin), new CharacterList());

        assertEquals(playerLeo.getDistrictBoardSize(), 5);
        assertEquals(playerThomas.getDistrictBoardSize(), 6);
        assertEquals(playerValentin.getDistrictBoardSize(), 6);
        assertTrue(playerThomas.getDistrictsBoard().contains(result.getDistrict()));

        playerValentin.getDistrictsBoard().addDistricts(new District(DistrictType.TEMPLE));
        assertEquals(playerValentin.getDistrictBoardSize(), 7);

        result = playerAlexandre.ability(List.of(playerLeo, playerThomas, playerValentin), new CharacterList());
        assertTrue(playerValentin.getDistrictsBoard().contains(result.getDistrict()));
    }

    @Test
    void warlordAbility2() {
        Player playerLeo = new Player("Leo", RandomStrategy.class);
        Player playerThomas = new Player("Thomas", RandomStrategy.class);
        Player playerValentin = new Player("Valentin", RandomStrategy.class);
        Player playerAlexandre = new Player("Alexandre", CheapDistrictStrategy.class);

        playerLeo.chooseCharacter(new Character(CharacterType.MERCHANT));

        playerThomas.chooseCharacter(new Character(CharacterType.KING));
        playerThomas.getDistrictsBoard().addDistricts(new District(DistrictType.TEMPLE));

        playerValentin.chooseCharacter(new Character(CharacterType.ASSASSIN));
        IntStream.range(0, 2).forEach(i -> playerValentin.getDistrictsBoard().addDistricts(new District(DistrictType.TEMPLE)));

        playerAlexandre.chooseCharacter(new Character(CharacterType.WARLORD));

        AbilityResult result = playerAlexandre.ability(List.of(playerLeo, playerThomas, playerValentin), new CharacterList());

        assertTrue(playerValentin.getDistrictsBoard().contains(result.getDistrict()));
    }

    @Test
    void magicianAbility() {
        Player playerLeo = new Player("Leo", RandomStrategy.class);
        Player playerThomas = new Player("Thomas", RandomStrategy.class);
        Player playerValentin = new Player("Valentin", RandomStrategy.class);
        Player playerAlexandre = new Player("Alexandre", CheapDistrictStrategy.class);
        playerLeo.chooseCharacter(new Character(CharacterType.MERCHANT));

        playerThomas.chooseCharacter(new Character(CharacterType.KING));

        playerValentin.chooseCharacter(new Character(CharacterType.ASSASSIN));

        IntStream.range(0, 5).forEach(i -> {
            playerLeo.getDistrictsHand().addDistricts(new District(DistrictType.MANOR));
            playerThomas.getDistrictsHand().addDistricts(new District(DistrictType.MANOR));
            playerValentin.getDistrictsHand().addDistricts(new District(DistrictType.MANOR));
        });

        playerThomas.getDistrictsHand().addDistricts(new District(DistrictType.MARKET));

        playerAlexandre.chooseCharacter(new Character(CharacterType.MAGICIAN));

        AbilityResult result = playerAlexandre.ability(List.of(playerLeo, playerThomas, playerValentin), new CharacterList());

        assertEquals(playerLeo.getDistrictsHand().size(), 5);
        assertEquals(playerThomas.getDistrictsHand().size(), 6);
        assertEquals(playerValentin.getDistrictsHand().size(), 5);

        assertEquals(playerThomas.getCharacter(), result.getCharacter());

        playerValentin.getDistrictsHand().addDistricts(new District(DistrictType.TEMPLE));
        playerValentin.getDistrictsHand().addDistricts(new District(DistrictType.TEMPLE));
        assertEquals(playerValentin.getDistrictsHand().size(), 7);

        result = playerAlexandre.ability(List.of(playerLeo, playerThomas, playerValentin), new CharacterList());
        assertEquals(playerValentin.getCharacter(), result.getCharacter());
    }

    void giveFullColorSet(Player p) {
        p.getDistrictsBoard().addDistricts(new District(DistrictType.MANOR), new District(DistrictType.TEMPLE), new District(DistrictType.TAVERN), new District(DistrictType.WATCHTOWER), new District(DistrictType.COURT_OF_MIRACLES));
    }

    @Test
    void switchStrategyStartingFiveColor() {
        Player playerFiveColorsStrategy = new Player("FiveColorsStrategy", FiveColorsStrategy.class);
        giveFullColorSet(playerFiveColorsStrategy);
        playerFiveColorsStrategy.switchStrategy();
        assertNotEquals(FiveColorsStrategy.class.getSimpleName(), playerFiveColorsStrategy.getStrategy().getSimpleName());
        playerFiveColorsStrategy.getDistrictsBoard().remove(0);
        playerFiveColorsStrategy.switchStrategy();
        assertEquals(FiveColorsStrategy.class.getSimpleName(), playerFiveColorsStrategy.getStrategy().getSimpleName());
    }

    @Test
    void switchStrategyNotStartingFiveColor() {
        Player playerCheapDistrictStrategy = new Player("CheapDistrictStrategy", CheapDistrictStrategy.class);
        giveFullColorSet(playerCheapDistrictStrategy);
        playerCheapDistrictStrategy.switchStrategy();
        assertNotEquals(FiveColorsStrategy.class.getSimpleName(), playerCheapDistrictStrategy.getStrategy().getSimpleName());
        playerCheapDistrictStrategy.getDistrictsBoard().remove(0);
        playerCheapDistrictStrategy.switchStrategy();
        assertNotEquals(FiveColorsStrategy.class.getSimpleName(), playerCheapDistrictStrategy.getStrategy().getSimpleName());
    }
}