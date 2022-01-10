package fr.unice.polytech.citadels;

import fr.unice.polytech.citadels.cards.characters.Character;
import fr.unice.polytech.citadels.cards.characters.CharacterFactory;
import fr.unice.polytech.citadels.cards.characters.CharacterList;
import fr.unice.polytech.citadels.cards.characters.CharacterType;
import fr.unice.polytech.citadels.cards.characters.abilities.AbilityResult;
import fr.unice.polytech.citadels.cards.districts.DistrictFactory;
import fr.unice.polytech.citadels.cards.districts.District;
import fr.unice.polytech.citadels.cards.districts.DistrictList;
import fr.unice.polytech.citadels.players.Districts;
import fr.unice.polytech.citadels.players.Player;
import fr.unice.polytech.citadels.players.PlayerList;
import fr.unice.polytech.citadels.strategies.CheapDistrictStrategy;
import fr.unice.polytech.citadels.strategies.ExpGoldStrategy;
import fr.unice.polytech.citadels.strategies.RandomStrategy;

import java.util.*;

import static fr.unice.polytech.citadels.Main.VERBOSE;

public class GameController {
    private PlayerList players;
    private DistrictList allDistricts;
    private CharacterList allCharacters;

    public static int round;

    public GameController() {
        Player bot1 = new Player("Bot 1", ExpGoldStrategy.class);
        Player bot2 = new Player("Bot 2", CheapDistrictStrategy.class);
        Player bot3 = new Player("Bot 3", RandomStrategy.class);
        Player bot4 = new Player("Bot 4", RandomStrategy.class);

        init(bot1, bot2, bot3, bot4);
    }

    public GameController(Player... players) {
        init(players);
    }

    public void init(Player... players) {
        // Ajout des joueurs dans liste de joueur
        this.players = new PlayerList(players);
        //Arrays.stream(players).forEach(player -> this.players.add(new Player(player.getName(), player.getStrategy(), this)));
        // Création de chaque carte via le CSV
        allDistricts = new DistrictFactory().getDistrictList();

        // Initialisation du nombre de tour de la partie
        round = 1;
    }

    public void run() {
        Logger.println("Lancement de la partie de citadelle: ", 1);

        allDistricts.shuffle(); // Mélange des cartes

        for (int i = 0; i < Main.DEFAULT_DISTRICTS; i++) {
            players.forEach(player -> player.getDistrictsHand().addDistricts(allDistricts.pick()));
        }

        while (players.getWinner() == null) {
            Logger.println("\n=============== Début du tour " + round + " ===============", 1);

            // Récupération de chaque carte personnages via la classe Characters
            instantiateCharacters();
            // On mélange les cartes de personnages
            allCharacters.shuffle();
            // On "écartes" x cartes personnages de la liste pour qu'il y est (nombre de joueur + 1) cartes
            allCharacters.discard(players.size() + 1);

            players.sortForCharacterChoice();

            players.forEach(this::runStrategySwitch);

            // On lance l'étape 1 du tour, le choix des personnages
            players.forEach(this::runCharacterChoice);

            // On fais en sorte que la dernière carte de jeu ne soit pas jouable / visible / joué
            allCharacters.hideLastCharacter();

            // On défini l'ordre de passage par rapport a leur id de personnage
            players.sortByCharacter();

            // On lance l'étape 2 du tour, les autres choix
            players.forEach(player -> {
                if (player.canPlay())
                    runPlayerRound(player);
                else {
                    Logger.println(1, "Le joueur " + player.getName() + " ne joue pas car il a été assassiné", 1, player);
                }
            });

            // Réinitialisation des états de chaque joueur
            players.forEach(Player::resetState);

            Logger.println("\n================ Fin du tour " + round + " ================", 1);
            // On affiche un résumé de la partie

            Logger.println("============== Résumé de la partie ==============", 1);
            players.forEach(Player::print);
            Logger.println("=================================================", 1);

            round++;
        }
        if(VERBOSE.getLevel() > 0)
            System.out.println("Partie fini: ");

        players.printPodium();
    }

    void runStrategySwitch(Player player) {
        player.switchStrategy();
    }

    void runCharacterChoice(Player player) {
        player.removeCrown();
        player.chooseCharacter(player.strategy().chooseCharacter(allCharacters.choosableCharacters(), allCharacters.playableCharacters(), players));
        allCharacters.chooseCharacter(player);
        if (player.getCharacter().getType().equals(CharacterType.KING))
            players.setFormerKing(player);
    }

    void runPlayerRound(Player player) {
        Logger.println("", 1);
        Logger.println(1, "Le joueur " + player.getName() + " joue: ", 1, player);

        allCharacters.revealCharacter(player.revealCharacter());
        handlePotentialSteal(player);
        player.usesAbility();
        // On demande au joueur s'il choisit de piocher ou 2 pièces d'or
        if(allDistricts.size() >= player.getCardToPick()) {
            switch (player.strategy().chooseGoldOrDistrict()) {
                case PICK_GOLDS -> player.chooseGold(2);
                case PICK_DISTRICTS -> allDistricts.append(player.chooseDistrict(allDistricts.pick(player.getCardToPick())));
            }
        } else player.giveGold(2);

        // Si c'est un marchant alors il obtient 1 gold en plus en début de tour
        if(player.isCharacter(CharacterType.MERCHANT)) {
            Logger.println(2, "Le joueur " + player.getName() + " reçoit 1 PO", 1, player);
            player.giveGold(1);
        }
        // Si c'est un architecte, alors il obtient + 2 quartiers en début de tour
        if (player.isCharacter(CharacterType.ARCHITECT) && allDistricts.size() >= player.getCardToPick()) {
            District[] districts = allDistricts.pick(2);
            Logger.println(2, "Le joueur " + player.getName() + " reçoit " + Arrays.toString(districts), 2, player);
            Logger.println(2, "Le joueur " + player.getName() + " reçoit 2 quartiers", 1, player);
            player.getDistrictsHand().addDistricts(districts);
        }

        while(!player.hasFinishedIsLap()) {
            switch (player.strategy().whatToDo()) {
                case FINISH -> player.finishIsLap();
                case PLACE_DISTRICT -> handleDistrictPlace(player);
                case COLLECT_GOLDS_FROM_DISTRICTS -> player.collectGoldsFromDistricts();
                case USE_ABILITY -> handleCharacterAbility(player);
            }
        }

        // On vérifie si le joueur à Main.DISTRICTS_FOR_WIN quartiers
        checkFirst(player);
        // Fin du tour
    }

    void handleDistrictPlace(Player player) {
        boolean hasPlaced;
        do {
            hasPlaced = player.strategy().placeDistrict();
        } while(hasPlaced && player.getDistrictPlaced() < player.getDistrictPlacable());
    }

    void handlePotentialSteal(Player player) {
        if (player.hasBeenStolen()) {
            allCharacters.getType(CharacterType.THIEF).getPlayer().giveGold(player.getGold());
            player.setGold(0);
        }
    }

    void handleCharacterAbility(Player p) {
        AbilityResult result = p.ability(List.copyOf(players), allCharacters);
        if (Objects.isNull(result)) return;

        switch (result.getType()) {
            case KILL -> kill(result.getCharacter());
            case STEAL -> steal(result.getCharacter());
            case TRADE_CARDS -> exchangeHand(p, result.getCharacter());
            case DESTROY_DISTRICT -> destroyDistrict(p, result.getDistrict());
        }
    }

    void kill(Character character) {
        if(!Objects.isNull(allCharacters.getPlayerByCharacter(character))) {
            Logger.println(2, "L'assassin choisit d'assassiner " + character + ".", 1, allCharacters.getType(CharacterType.ASSASSIN).getPlayer());
            allCharacters.getPlayerByCharacter(character).kill();
        }
    }

    void steal(Character character) {
        if(!Objects.isNull(allCharacters.getPlayerByCharacter(character))) {
            Logger.println(2, "Le voleur choisit de voler " + character + ".", 1, allCharacters.getType(CharacterType.ASSASSIN).getPlayer());
            allCharacters.getPlayerByCharacter(character).steal();
        }
    }

    void exchangeHand(Player magician, Character character) {
        if(!Objects.isNull(allCharacters.getPlayerByCharacter(character))) {
            Player target = allCharacters.getPlayerByCharacter(character);
            Districts temp = target.getDistrictsHand();
            target.setDistrictsHand(magician.getDistrictsHand());
            magician.setDistrictsHand(temp);
            Logger.println(2, "Le magicien échange ces cartes avec " + target.getName() + ".", 1, magician);
        }
    }

    void destroyDistrict(Player warlord, District district) {
        if (canDestroyDistrict(warlord, district)) {
            warlord.setGold(warlord.getGold() - district.getPrice() - 1);
            players.stream().filter(player -> player.hasPlacedDistrict(district)).forEach(player -> {
                allDistricts.append(district);
                player.getDistrictsBoard().removeDistricts(district);
                Logger.println(2, "La condotiere décide de détruire le quartier " + district + " de " + player.getName() + ".", 1, allCharacters.getType(CharacterType.WARLORD).getPlayer());
            });
        }
    }

    boolean canDestroyDistrict(Player warlord, District district) {
        return warlord.getGold() >= district.getPrice()-1;
    }

    public PlayerList getPlayers() {
        return players;
    }

    public void instantiateCharacters(){
        allCharacters = new CharacterFactory().getCharacters();
    }

    void checkFirst(Player player) {
        if (player.getDistrictsBoard().size() >= Main.DISTRICTS_FOR_WIN && getPlayers().getWinner() == null)
            players.setWinner(player.isFirst());
    }

    public CharacterList getAllCharacters() {
        return allCharacters;
    }

}
