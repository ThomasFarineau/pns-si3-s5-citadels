package fr.unice.polytech.citadels.strategies;

import fr.unice.polytech.citadels.GameController;
import fr.unice.polytech.citadels.cards.characters.Character;
import fr.unice.polytech.citadels.cards.characters.CharacterType;
import fr.unice.polytech.citadels.cards.characters.abilities.AbilityResult;
import fr.unice.polytech.citadels.cards.districts.District;
import fr.unice.polytech.citadels.cards.districts.DistrictType;
import fr.unice.polytech.citadels.players.Player;
import fr.unice.polytech.citadels.players.PlayerAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AggressiveStrategyTest {
    Player player;
    District districtTemple, districtWatchtower, districtBarracks, districtTavern, districtStall, districtFortress, districtMonastery, districtObservatory;

    @BeforeEach
    void setup() {
        player = new Player("AggressiveStrategy", AggressiveStrategy.class);
        districtTemple = new District(DistrictType.TEMPLE);
        districtWatchtower = new District(DistrictType.WATCHTOWER);
        districtBarracks = new District(DistrictType.BARRACKS);
        districtTavern = new District(DistrictType.TAVERN);
        districtStall = new District(DistrictType.STALL);
        districtFortress = new District(DistrictType.FORTRESS);
        districtMonastery = new District(DistrictType.MONASTERY);
        districtObservatory = new District(DistrictType.OBSERVATORY);
        player.getDistrictsBoard().addDistricts(districtTemple, districtWatchtower, districtBarracks, districtTavern, districtStall, districtFortress, districtMonastery);
        player.getDistrictsHand().addDistricts(districtObservatory);
        player.giveGold(10);
    }

    @Test
    void chooseCharacterWarlordABW() {
        Character[] playable = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.WARLORD), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        Character[] selectable = new Character[]{new Character(CharacterType.WARLORD), new Character(CharacterType.KING), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        assertEquals(player.strategy().chooseCharacter(selectable, playable, null), new Character(CharacterType.WARLORD));
    }

    @Test
    void chooseCharacterAssassinABW() {
        Character[] playable = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.WARLORD), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        Character[] selectable = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        assertEquals(player.strategy().chooseCharacter(selectable, playable, null), new Character(CharacterType.ASSASSIN));
    }

    @Test
    void chooseCharacterAssassinAW() {
        Character[] playable = new Character[]{new Character(CharacterType.THIEF), new Character(CharacterType.KING), new Character(CharacterType.WARLORD), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        Character[] selectable = new Character[]{new Character(CharacterType.WARLORD), new Character(CharacterType.KING), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        assertEquals(player.strategy().chooseCharacter(selectable, playable, null), new Character(CharacterType.ASSASSIN));
    }

    @Test
    void chooseCharacterWarlordAW() {
        Character[] playable = new Character[]{new Character(CharacterType.THIEF), new Character(CharacterType.KING), new Character(CharacterType.WARLORD), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        Character[] selectable = new Character[]{new Character(CharacterType.THIEF), new Character(CharacterType.KING), new Character(CharacterType.MERCHANT), new Character(CharacterType.WARLORD)};
        assertEquals(player.strategy().chooseCharacter(selectable, playable, null), new Character(CharacterType.WARLORD));
    }

    @Test
    void chooseCharacterAssassinAM() {
        Character[] playable = new Character[]{new Character(CharacterType.MAGICIAN), new Character(CharacterType.KING), new Character(CharacterType.THIEF), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        Character[] selectable = new Character[]{new Character(CharacterType.ASSASSIN), new Character(CharacterType.KING), new Character(CharacterType.MERCHANT), new Character(CharacterType.MAGICIAN)};
        assertEquals(player.strategy().chooseCharacter(selectable, playable, null), new Character(CharacterType.ASSASSIN));
    }

    @Test
    void chooseCharacterMagicianAM() {
        Character[] playable = new Character[]{new Character(CharacterType.MAGICIAN), new Character(CharacterType.KING), new Character(CharacterType.THIEF), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        Character[] selectable = new Character[]{new Character(CharacterType.THIEF), new Character(CharacterType.KING), new Character(CharacterType.MERCHANT), new Character(CharacterType.MAGICIAN)};
        assertEquals(player.strategy().chooseCharacter(selectable, playable, null), new Character(CharacterType.MAGICIAN));
    }

    @Test
    void chooseCharacterWarlordBW() {
        Character[] playable = new Character[]{new Character(CharacterType.WARLORD), new Character(CharacterType.KING), new Character(CharacterType.MAGICIAN), new Character(CharacterType.MERCHANT), new Character(CharacterType.BISHOP)};
        Character[] selectable = new Character[]{new Character(CharacterType.MAGICIAN), new Character(CharacterType.KING), new Character(CharacterType.BISHOP), new Character(CharacterType.WARLORD)};
        assertEquals(player.strategy().chooseCharacter(selectable, playable, null), new Character(CharacterType.WARLORD));
    }

    @Test
    void chooseCharacterBishopBW() {
        Character[] playable = new Character[]{new Character(CharacterType.WARLORD), new Character(CharacterType.KING), new Character(CharacterType.MAGICIAN), new Character(CharacterType.MERCHANT), new Character(CharacterType.BISHOP)};
        Character[] selectable = new Character[]{new Character(CharacterType.MAGICIAN), new Character(CharacterType.KING), new Character(CharacterType.BISHOP), new Character(CharacterType.MERCHANT)};
        assertEquals(player.strategy().chooseCharacter(selectable, playable, null), new Character(CharacterType.BISHOP));
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
        assertNotEquals(player.strategy().chooseCharacter(selectable, playable, null), new Character(CharacterType.ARCHITECT));
    }

    @Test
    void chooseCharacterKing() {

        Player player2 = new Player("player2", RandomStrategy.class);
        List<Player> playerList = new ArrayList<>();
        playerList.add(player);
        playerList.add(player2);
        player2.getDistrictsBoard().addDistricts(districtTemple, districtWatchtower, districtBarracks, districtTavern, districtStall, districtFortress);

        Character[] playable = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.WARLORD), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        Character[] selectable = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.ARCHITECT), new Character(CharacterType.MERCHANT)};
        assertEquals(player.strategy().chooseCharacter(selectable, playable, playerList), new Character(CharacterType.KING));
    }

    @Test
    void chooseCharacterKingFalse() {

        Player player2 = new Player("player2", RandomStrategy.class);
        List<Player> playerList = new ArrayList<>();
        playerList.add(player);
        playerList.add(player2);
        player2.getDistrictsBoard().addDistricts(districtTemple, districtWatchtower, districtBarracks, districtTavern, districtStall, districtFortress);

        Character[] playable = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.WARLORD), new Character(CharacterType.MERCHANT), new Character(CharacterType.ASSASSIN)};
        Character[] selectable = new Character[]{new Character(CharacterType.BISHOP), new Character(CharacterType.KING), new Character(CharacterType.ARCHITECT), new Character(CharacterType.WARLORD)};
        assertEquals(player.strategy().chooseCharacter(selectable, playable, playerList), new Character(CharacterType.WARLORD));
    }

    @Test
    void chooseAssassinWhenSomeoneWillWin() {
        GameController gc = new GameController();
        Player player2 = new Player("player2", AggressiveStrategy.class);
        player2.getDistrictsBoard().addDistricts(districtTemple, districtWatchtower, districtBarracks);
        player2.getDistrictsHand().addDistricts(districtTavern, districtStall, districtFortress);
        gc.init(player, player2);
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
    void whatToDoTestPlaceDistrict() {
        player.chooseCharacter(new Character(CharacterType.KING));
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
        player.giveGold(-10);
        assertEquals(player.strategy().whatToDo(), PlayerAction.COLLECT_GOLDS_FROM_DISTRICTS);
    }

    @Test
    void whatToDoTestCollectGoldsWarlord() {
        player.chooseCharacter(new Character(CharacterType.WARLORD));
        player.addAction(PlayerAction.USE_ABILITY);
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
