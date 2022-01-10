package fr.unice.polytech.citadels.players;

import fr.unice.polytech.citadels.CColors;
import fr.unice.polytech.citadels.GameController;
import fr.unice.polytech.citadels.Logger;
import fr.unice.polytech.citadels.Main;
import fr.unice.polytech.citadels.cards.characters.Character;
import fr.unice.polytech.citadels.cards.characters.CharacterList;
import fr.unice.polytech.citadels.cards.characters.CharacterType;
import fr.unice.polytech.citadels.cards.characters.abilities.AbilityResult;
import fr.unice.polytech.citadels.cards.districts.District;
import fr.unice.polytech.citadels.cards.districts.DistrictType;
import fr.unice.polytech.citadels.strategies.Strategy;

import java.util.*;

public class Player {

    private final Districts districtsBoard = new Districts();
    private final List<Character> charactersToChoose = new ArrayList<>();
    private final String name;
    private final Map<Integer, List<PlayerAction>> actionsHistory = new HashMap<>();
    private Districts districtsHand = new Districts();
    private Strategy strategy;
    private Strategy startingStrategy;
    private Character character;
    private int score;
    private int gold;
    private boolean hasCrown = false;
    private boolean canPlay = true;
    private boolean hasBeenStolen = false;
    private boolean usesAbility = false;
    private int districtPlacable = 1;
    private int districtPlaced = 0;
    private boolean finishedIsLap = false;

    /**
     * Ce constructeur crée un player en lui attribuant un nom
     * ainsi qu'une stratégie parmi celles prédifinies
     */

    public Player(String name, Class<? extends Strategy> strategyClass) {
        this.name = name;
        try {
            this.strategy = validateStrategy(strategyClass);
            this.startingStrategy = validateStrategy(strategyClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        score = 0;
    }

    public District[] chooseDistrict(District... districts) {
        addAction(PlayerAction.PICK_DISTRICTS);

        Logger.println(2, "Le joueur " + name + " a choisi de piocher 2 quartiers.", 1, this);

        District[] toReturn = new District[districts.length - 1];
        if (districtsBoard.containsByType(DistrictType.LIBRARY)) {
            Arrays.stream(districts).forEach(district -> districtsHand.addDistricts(districts));
            Logger.println(2, "Le joueur a récupéré " + Arrays.toString(districts) + ".", 2, this);
            Logger.println(2, "Le joueur prend toutes les quartiers proposés grâce a la carte " + districtsBoard.getByType(DistrictType.LIBRARY)[0] + ".", 1, this);
            return new District[0];
        }
        Logger.println(2, "Le joueur " + name + " choisit un quartier parmi " + Arrays.toString(districts) + ".", 2, this);
        Logger.println(2, "Le joueur " + name + " choisit un quartier.", 1, this);
        District chosen = strategy.chooseDistrict(districts);
        Logger.println(2, "Le joueur a choisi " + chosen + ".", 2, this);
        districtsHand.addDistricts(chosen);
        int i = 0;
        for (District district : districts)
            if (district != chosen) toReturn[i++] = district;
        return toReturn;
    }

    public void chooseGold(int gold) {
        addAction(PlayerAction.PICK_GOLDS);

        Logger.println(2, "Le joueur " + name + " a choisi 2 PO.", 1, this);
        this.gold += gold;
    }

    public void switchStrategy() {
        strategy.switchStrategy();
    }

    public boolean hasFinishedIsLap() {
        return finishedIsLap;
    }

    public void finishIsLap() {
        finishedIsLap = true;
    }

    public boolean isCharacter(CharacterType characterType) {
        return getCharacter().getType().equals(characterType);
    }

    public void resetState() {      // Réinitialise les attributs du player
        finishedIsLap = false;
        canPlay = true;
        hasBeenStolen = false;
        districtPlaced = 0;
        districtPlacable = 1;
    }

    public void reset() {
        resetState();
        gold = 0;
        districtsHand.clear();
        districtsBoard.clear();
    }

    public boolean hasPlacedDistrict(District district) {
        return districtsBoard.contains(district);
    }

    public void giveCrown() {
        this.hasCrown = true;
    }

    public void removeCrown() {
        this.hasCrown = false;
    }

    public void giveGold(int amount) {
        gold += amount;
    }

    public boolean getCrown() {
        return hasCrown;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void collectGoldsFromDistricts() {
        if (character.getDistrictColor() != null) {
            int i = (int) districtsBoard.stream().filter(d -> d.getColor().equals(character.getDistrictColor())).count();
            if (districtsBoard.containsByType(DistrictType.SCHOOL_OF_MAGIC)) i++;
            if (i > 0) {
                addAction(PlayerAction.COLLECT_GOLDS_FROM_DISTRICTS);
                Logger.println(2, "Le joueur " + name + " a récupéré " + i + " PO grace a ses quartiers.", 1, this);
                giveGold(i);
            } else Logger.println(2, "Le joueur " + name + " n'a rien récupéré avec ses quartiers.", 2, this);
        }
    }

    public Character revealCharacter() {
        addAction(PlayerAction.REVEAL_CHARACTER);

        Logger.println(2, "Le joueur " + name + " révèle son personnage qui était " + character + ".", 1, this);
        return character;
    }

    public AbilityResult ability(List<Player> players, CharacterList characters) {
        addAction(PlayerAction.USE_ABILITY);
        return strategy.ability(players, characters);
    }

    public Character getCharacter() {
        return character;
    }

    public void chooseCharacter(Character character) {
        addAction(PlayerAction.CHOOSE_CHARACTER);

        this.character = character;
        Logger.println(2, "Le joueur " + name + " a choisi un personnage.", 1, this);
        Logger.println(2, "Le joueur " + name + " choisit de jouer " + character + ".", 2, this);
        if (character.getType().equals(CharacterType.KING)) giveCrown();
        if (character.getType().equals(CharacterType.ARCHITECT)) districtPlacable = 3;
        charactersToChoose.remove(character);
    }

    public Character characterChosen() {
        charactersToChoose.clear();
        return character;
    }

    public boolean canPlaceMultipleDistricts(int minCost) {
        List<District> available;
        available = districtsHand.stream().filter(district -> district.getPrice() >= minCost).toList();
        System.out.println(available.size());
        for (int i = 0; i < available.size() - 1; i++) {
            for (int j = i + 1; j < available.size(); j++) {
                if (available.get(i).getPrice() + available.get(j).getPrice() <= gold) return true;
            }
        }
        return false;
    }

    public Class<? extends Strategy> getStrategy() {
        return strategy.getClass();
    }

    public void setStrategy(Class<? extends Strategy> strategyClass) throws Exception {
        this.strategy = validateStrategy(strategyClass);
    }

    public Player isFirst() {
        score += 2; // Ajoute deux au score, car le joueur finit premier
        return this;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public Strategy strategy() {
        return strategy;
    }

    public Districts getDistrictsBoard() {
        return districtsBoard;
    }

    public Districts getDistrictsHand() {
        return districtsHand;
    }

    public void setDistrictsHand(Districts districtsHand) {
        this.districtsHand = districtsHand;
    }

    public boolean placeDistrict(District d) {

        if (canPlaceDistrict(d)) {
            addAction(PlayerAction.PLACE_DISTRICT);

            districtsHand.remove(d);
            districtsBoard.place(d);
            gold -= d.getPrice();
            Logger.println(2, "Le joueur " + name + " a placé " + d + ".", 1, this);
            incrementDistrictPlaced();
            calculateScore();
            return true;
        }
        return false;
    }

    public void kill() {
        canPlay = false;
    }

    public void steal() {
        hasBeenStolen = true;
    }

    public boolean mayPurchaseADistrict() {
        return districtsHand.stream().anyMatch(district -> district.getPrice() <= gold);
    }

    public List<Character> getCharactersToChoose() {
        return charactersToChoose;
    }

    private boolean canPlaceDistrict(District d) {
        return districtsHand.contains(d) && d.getPrice() <= gold;
    }

    private void calculateScore() {
        score = 0;
        score += districtsBoard.score(); // Ajoute le score de chaque quartiers au score du joueur
        score += districtsBoard.containsColorSet() ? 3 : 0; // Ajoute 3 points si le joueur a 5 couleurs de quartiers différentes
        score += (districtsBoard.size() == Main.DISTRICTS_FOR_WIN) ? 2 : 0; // Ajoute 2 points si le joueur a 8 quartiers
    }

    Strategy validateStrategy(Class<? extends Strategy> strategyClass) throws Exception {
        return strategyClass.getConstructor(Player.class).newInstance(this);
    }

    public boolean canPlay() {
        return canPlay;
    }

    public boolean hasBeenStolen() {
        return hasBeenStolen;
    }

    public void usesAbility() {
        strategy.usesAbility();
    }

    public boolean getUsesAbility() {
        return usesAbility;
    }

    public void setUsesAbility(boolean usesAbility) {
        this.usesAbility = usesAbility;
    }

    public int getDistrictPlacable() {
        return districtPlacable;
    }

    public int getDistrictPlaced() {
        return districtPlaced;
    }

    public void incrementDistrictPlaced() {
        districtPlaced++;
    }

    public District useLaboratory(District d) {
        if (districtsBoard.containsByType(DistrictType.LABORATORY) && districtsHand.contains(d)) {
            districtsHand.removeDistricts(d);
            gold += 1;
            return d;
        }
        return null;
    }

    public void useManufacture(District... districts) {
        if (districtsBoard.containsByType(DistrictType.MANUFACTURE) && gold >= 3) {
            System.out.println("oui");
            districtsHand.addDistricts(districts);
            gold -= 3;
        }
    }

    public void useCemetery(District d) {
        if (districtsBoard.containsByType(DistrictType.CEMETERY) && gold >= 1) {
            if (askForRetrieveInHand(d)) {
                districtsHand.add(d);
                gold -= 1;
            }
        }
    }

    public int getDistrictBoardSize() {
        return districtsBoard.size();
    }

    public Strategy getStartingStrategy() {
        return startingStrategy;
    }

    boolean askForRetrieveInHand(District district) {
        return true;
    }

    public int getCardToPick() {
        return districtsBoard.containsByType(DistrictType.OBSERVATORY) ? 3 : 2;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Player p)) return false;
        return name.equals(p.getName());
    }

    public void addAction(PlayerAction action) {
        List<PlayerAction> actions = new ArrayList<>();
        if (actionsHistory.containsKey(GameController.round)) actions = actionsHistory.get(GameController.round);
        actions.add(action);
        if (actionsHistory.containsKey(GameController.round)) actionsHistory.replace(GameController.round, actions);
        else actionsHistory.put(GameController.round, actions);
    }

    public boolean canPlaceDistrict() {
        int p = (int) actionsHistory.get(GameController.round).stream().filter(a -> a.equals(PlayerAction.PLACE_DISTRICT)).count();
        return p < getDistrictPlacable() && mayPurchaseADistrict();
    }

    public boolean canCollectGoldsFromDistrict() {
        if (!actionsHistory.get(GameController.round).contains(PlayerAction.COLLECT_GOLDS_FROM_DISTRICTS) && character.getDistrictColor() != null) {
            int i = (int) districtsBoard.stream().filter(d -> d.getColor().equals(character.getDistrictColor())).count();
            if (districtsBoard.containsByType(DistrictType.SCHOOL_OF_MAGIC)) i++;
            return i > 0;
        }
        return false;
    }

    public boolean canUseAbility() {
        return !actionsHistory.get(GameController.round).contains(PlayerAction.USE_ABILITY) && (character.getType().equals(CharacterType.MAGICIAN) || character.getType().equals(CharacterType.THIEF) || character.getType().equals(CharacterType.ASSASSIN) || character.getType().equals(CharacterType.WARLORD));
    }

    public void print() {
        Logger.println(0, "Joueur " + name, 1);
        Logger.println(2, "Stratégie: " + getStrategy().getSimpleName(), 2);
        Logger.println(2, "Personnage choisit: " + character.toString(), 1);
        Logger.println(2, "Pièces d'or: " + CColors.YELLOW + getGold() + " PO", 1);
        Logger.println(2, "Quartier dans la main: " + getDistrictsHand().size(), 1);
        Logger.println(4, "Main: " + getDistrictsHand(), 2);
        Logger.println(2, "Plateau: " + getDistrictsBoard(), 1);
        Logger.println(4, "Score du plateau: " + getDistrictsBoard().score(), 1);
        Logger.println(4, "Nombre de quartier posé: " + getDistrictsBoard().size(), 1);
        Logger.println(2, "Historique des actions: " + actionsHistory.get(GameController.round), 2);
        Logger.println(0, "", 1);
    }
}
