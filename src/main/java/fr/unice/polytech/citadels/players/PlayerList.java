package fr.unice.polytech.citadels.players;

import fr.unice.polytech.citadels.CColors;
import fr.unice.polytech.citadels.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlayerList extends ArrayList<Player> {
    Player winner = null;
    Player formerKing = null;

    public PlayerList(Player... players) {
        this.addAll(List.of(players));
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public void setFormerKing(Player formerKing) {
        this.formerKing = formerKing;
    }

    public void printPodium() {
        List<Player> podium = new ArrayList<>(this);
        podium.sort((o1, o2) -> o2.getScore() - o1.getScore());
        int place = 1;
        for (Player player : podium)
            Logger.println("Place " + place++ + ": " + player.getName() + " (" + ((player.getStartingStrategy() != null) ? player.getStartingStrategy().getClass().getSimpleName() : player.getStrategy().getSimpleName()) + ") avec un score de " + player.getScore() + ".", 0);
        Logger.println("", 1);
        Player first = podium.get(0);
        if (!first.getStrategy().getSimpleName().equals(first.getStartingStrategy().getClass().getSimpleName()))
            Logger.println(CColors.YELLOW_BOLD + "\uD83C\uDFC6 Le gagnant est " + first.getName() + " en utilisant les stratégies " + first.getStartingStrategy().getClass().getSimpleName() + " et " + first.getStrategy().getSimpleName() + " \uD83C\uDFC6", 1);
        else
            Logger.println(CColors.YELLOW_BOLD + "\uD83C\uDFC6 Le gagnant est " + first.getName() + " en utilisant la stratégie " + first.getStrategy().getSimpleName() + " \uD83C\uDFC6", 1);
    }

    public void sortByCharacter() {
        this.sort(Comparator.comparingInt(p -> p.getCharacter().getId()));
    }

    public void sortForCharacterChoice() {
        if (formerKing != null && this.indexOf(formerKing) != 0) swapChars(0, this.indexOf(formerKing));
    }

    public void swapChars(int a, int b) {
        Player temp = this.get(a);
        this.set(a, this.get(b));
        this.set(b, temp);
    }

    public void reset() {
        this.forEach(Player::reset);
    }

    public List<Player> playersPodium() {
        List<Player> podium = new ArrayList<>(this);
        podium.sort((o1, o2) -> o2.getScore() - o1.getScore());
        return podium;
    }

    public boolean isDraw() {
        return playersPodium().get(0).getScore() == playersPodium().get(1).getScore();
    }
}
