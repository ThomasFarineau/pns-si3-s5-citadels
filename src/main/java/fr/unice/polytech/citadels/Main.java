package fr.unice.polytech.citadels;

import fr.unice.polytech.citadels.players.Player;
import fr.unice.polytech.citadels.strategies.*;

public class Main {

    public static int DISTRICTS_FOR_WIN = 8;
    public static int DEFAULT_DISTRICTS = 4;
    public static Verbose VERBOSE = new Verbose(2);

    public static void main(String... args) {
        // Nom générer avec https://www.generateur-de-pseudo.fr/
        Player botFiveColorsStrategy = new Player("elPepito", FiveColorsStrategy.class);
        Player botExpGoldStrategy = new Player("CyberLotus", ExpGoldStrategy.class);
        Player botFiveColorsAggressiveStrategy = new Player("MadRaptor", FiveColorsAggressiveStrategy.class);
        Player botAggressiveStrategy = new Player("MadDog", AggressiveStrategy.class);
        Player botCheapDistrictStrategy = new Player("RushBi", CheapDistrictStrategy.class);
        Player botRandomStrategy = new Player("ZeroReality", RandomStrategy.class);

        GameController game = new GameController(botFiveColorsStrategy, botExpGoldStrategy, botFiveColorsAggressiveStrategy, botAggressiveStrategy, botCheapDistrictStrategy, botRandomStrategy);
        game.run();
    }
}
