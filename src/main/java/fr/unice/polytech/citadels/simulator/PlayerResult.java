package fr.unice.polytech.citadels.simulator;

import fr.unice.polytech.citadels.players.Player;

public class PlayerResult {
    private final Player player;
    private int win = 0;
    private int draw = 0;
    private int lose = 0;
    private int score = 0;
    private int games = 0;

    public PlayerResult(Player player) {
        this.player = player;
    }

    public float getAverage() {
        return (float) ((0.0 + score) / games);
    }

    public float getRate(int type) {
        return getRate(type, games);
    }

    public float getRate(int type, int games) {
        return (float) ((100.0 * type) / games);
    }

    public Player getPlayer() {
        return player;
    }

    public Integer getWin() {
        return win;
    }

    public Integer getDraw() {
        return draw;
    }

    public Integer getLose() {
        return lose;
    }

    public Integer getGames() {
        return games;
    }

    public void winner() {
        this.win++;
    }

    public void looser() {
        this.lose++;
    }

    public void drawer() {
        this.draw++;
    }

    public void addScore(int scoretot) {
        this.score += scoretot;
        this.games++;
    }

    public boolean isSet() {
        return win + lose + draw == games;
    }

    public String getStrategyName() {
        return (player.getStartingStrategy() != null ? player.getStartingStrategy().getClass().getSimpleName() : player.getStrategy().getSimpleName());
    }

    public String toString() {
        return player.getName() + " - " + getStrategyName() + " - victoire : " + win + " (" + getRate(win) + "%)" + " - défaite : " + lose + " (" + getRate(lose) + "%)" + " - match nul : " + draw + " (" + getRate(draw) + "%)" + " - score moyen : " + getAverage();
    }

}