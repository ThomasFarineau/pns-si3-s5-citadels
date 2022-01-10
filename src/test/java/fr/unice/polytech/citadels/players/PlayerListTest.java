package fr.unice.polytech.citadels.players;

import fr.unice.polytech.citadels.cards.characters.Character;
import fr.unice.polytech.citadels.cards.characters.CharacterType;
import fr.unice.polytech.citadels.strategies.ExpGoldStrategy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerListTest {

    @Test
    void getWinner() {
        Player player1 = new Player("player", ExpGoldStrategy.class);
        Player player2 = new Player("player", ExpGoldStrategy.class);
        PlayerList playerList = new PlayerList(player1,player2);

        playerList.setWinner(player1);
        assertEquals(playerList.getWinner(),player1);
    }

    @Test
    void setWinner() {
        Player player = new Player("player", ExpGoldStrategy.class);
        PlayerList playerList = new PlayerList(player);

        playerList.setWinner(player);
        assertEquals(playerList.getWinner(),player);
    }

    @Test
    void setFormerKing() {
        Player player = new Player("player", ExpGoldStrategy.class);
        PlayerList playerList = new PlayerList(player);

        playerList.setFormerKing(player);
        assertEquals(playerList.formerKing,player);
    }

    @Test
    void printPodium() {
    }

    @Test
    void sortByCharacter() {
        Player player = new Player("player", ExpGoldStrategy.class);
        Player player2 = new Player("player2", ExpGoldStrategy.class);
        player.chooseCharacter(new Character(CharacterType.WARLORD));
        player2.chooseCharacter(new Character(CharacterType.ASSASSIN));
        PlayerList playerList = new PlayerList(player,player2);

        playerList.sortByCharacter();
        assertEquals(playerList.get(0),player2);
        assertEquals(playerList.get(1),player);
    }

    @Test
    void sortForCharacterChoice() {
        Player player = new Player("player", ExpGoldStrategy.class);
        PlayerList playerList = new PlayerList(player);
        playerList.setFormerKing(player);
        playerList.sortForCharacterChoice();
        assertEquals(playerList.indexOf(player),0);
    }

    // Dans ce test, le formerking vaut "null", il ne se passe donc rien
    @Test
    void sortForCharacterChoiceNull(){
        Player player = new Player("player", ExpGoldStrategy.class);
        Player player2 = new Player("player2", ExpGoldStrategy.class);
        PlayerList playerList = new PlayerList(player);
        playerList.sortForCharacterChoice();
        assertNotEquals(playerList.indexOf(player2),0);
    }
}