package fr.unice.polytech.citadels.simulator;

import fr.unice.polytech.citadels.GameController;
import fr.unice.polytech.citadels.Main;
import fr.unice.polytech.citadels.Verbose;
import fr.unice.polytech.citadels.players.Player;
import fr.unice.polytech.citadels.strategies.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Simulator {
    private Player[] players;
    private final int iteration;
    private final String results;
    private final boolean doesSave;
    private final int verboseLevel;
    private final GameController gameController;
    private final List<Class<? extends  Strategy>> strategies = new ArrayList<>();

    @SafeVarargs
    public Simulator(int iteration, boolean doesSave, int verboseLevel, String filename, GameController gc, Class<? extends Strategy>... strategies) {
        this.strategies.addAll(Arrays.asList(strategies));
        this.players = IntStream.range(0, this.strategies.size()).mapToObj(i -> new Player("Bot " + (i + 1), this.strategies.get(i))).toArray(Player[]::new);
        this.iteration = iteration;
        this.results = filename;
        this.doesSave = doesSave;
        this.verboseLevel = verboseLevel;
        this.gameController = gc;
    }

    @SafeVarargs
    public Simulator(int iteration, boolean doesSave, GameController gc, Class<? extends Strategy>... strategies) {
        this.strategies.addAll(Arrays.asList(strategies));
        this.players = IntStream.range(0, this.strategies.size()).mapToObj(i -> new Player("Bot " + (i + 1), this.strategies.get(i))).toArray(Player[]::new);
        this.iteration = iteration;
        this.results = "save/results.csv";
        this.doesSave = doesSave;
        this.verboseLevel = -1;
        this.gameController = gc;
    }

    public void run() {
        Main.VERBOSE = new Verbose(verboseLevel);
        Map<String, PlayerResult> playerResults = Arrays.stream(players).collect(Collectors.toMap(Player::getName, PlayerResult::new, (a, b) -> b));
        for (int i = 0; i < iteration; i++) {
            this.players = IntStream.range(0, this.strategies.size()).mapToObj(j -> new Player("Bot " + (j + 1), this.strategies.get(j))).toArray(Player[]::new);
            gameController.init(players);
            gameController.run();
            // On ajoute le score dans un premier temps
            gameController.getPlayers().forEach(player -> playerResults.get(player.getName()).addScore(player.getScore()));
            // On vérifie si c'est le résultat des meilleurs joueurs sont égaux pour savoir s'ils ont match nul
            boolean isDraw = (gameController.getPlayers().isDraw());
            // S'il y a match nul on met dans le résultat nul pour chaque joueur ayant le même score que le joueur 0 du podium
            // Sinon on met en gagnant le premier
            if (isDraw)
                Arrays.stream(players).filter(player -> player.getScore() == gameController.getPlayers().playersPodium().get(0).getScore()).forEach(player -> playerResults.get(player.getName()).drawer());
            else playerResults.get(gameController.getPlayers().playersPodium().get(0).getName()).winner();
            // Tout les autres sont perdants
            Arrays.stream(players).filter(player -> !playerResults.get(player.getName()).isSet()).forEach(player -> playerResults.get(player.getName()).looser());

            gameController.getPlayers().reset();
        }
        Map<String, PlayerResult> treeMap = new TreeMap<>(playerResults);
        treeMap.forEach((key, value) -> System.out.println(value));

        if (doesSave) save(playerResults);
    }

    public void save(Map<String, PlayerResult> playerResults) {
        DataResult dataResult = new DataResult(results);
        playerResults.forEach((key, value) -> dataResult.printLine(value.getStrategyName(), value.getWin(), value.getRate(value.getWin()), value.getLose(), value.getRate(value.getLose()), value.getDraw(), value.getRate(value.getDraw()), value.getAverage(), value.getGames()));
    }

    private static final int SIMULATE_EACH_STRATEGY = 0;
    private static final int SIMULATE_FIVE_COLORS_STRATEGY = 1;
    private static final int SIMULATE_EXP_GOLD_STRATEGY = 2;
    private static final int SIMULATE_FIVE_COLORS_AGGRESSIVE_STRATEGY = 3;
    private static final int SIMULATE_AGGRESSIVE_STRATEGY = 4;
    private static final int SIMULATE_CHEAP_DISTRICT_STRATEGY = 5;
    private static final int SIMULATE_RANDOM_STRATEGY = 6;

    public static void main(String... args) {
        int toSimulate = SIMULATE_EACH_STRATEGY;

        Simulator simulator = switch (toSimulate) {
            case SIMULATE_EACH_STRATEGY -> new Simulator(1000, true, new GameController(), FiveColorsStrategy.class, ExpGoldStrategy.class, CheapDistrictStrategy.class, RandomStrategy.class, AggressiveStrategy.class, FiveColorsAggressiveStrategy.class);
            case SIMULATE_FIVE_COLORS_STRATEGY -> new Simulator(1000, true, new GameController(), FiveColorsStrategy.class, FiveColorsStrategy.class, FiveColorsStrategy.class, FiveColorsStrategy.class);
            case SIMULATE_EXP_GOLD_STRATEGY -> new Simulator(1000, true, new GameController(), ExpGoldStrategy.class, ExpGoldStrategy.class, ExpGoldStrategy.class, ExpGoldStrategy.class);
            case SIMULATE_FIVE_COLORS_AGGRESSIVE_STRATEGY -> new Simulator(1000, true, new GameController(), FiveColorsAggressiveStrategy.class, FiveColorsAggressiveStrategy.class, FiveColorsAggressiveStrategy.class, FiveColorsAggressiveStrategy.class);
            case SIMULATE_AGGRESSIVE_STRATEGY -> new Simulator(1000, true, new GameController(), AggressiveStrategy.class, AggressiveStrategy.class, AggressiveStrategy.class, AggressiveStrategy.class);
            case SIMULATE_CHEAP_DISTRICT_STRATEGY -> new Simulator(1000, true, new GameController(), CheapDistrictStrategy.class, CheapDistrictStrategy.class, CheapDistrictStrategy.class, CheapDistrictStrategy.class);
            case SIMULATE_RANDOM_STRATEGY -> new Simulator(1000, true, new GameController(), RandomStrategy.class, RandomStrategy.class, RandomStrategy.class, RandomStrategy.class);
            default -> null;
        };
        assert simulator != null;
        simulator.run();
    }
}

