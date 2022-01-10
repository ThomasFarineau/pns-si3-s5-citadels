package fr.unice.polytech.citadels.strategies;

import fr.unice.polytech.citadels.cards.characters.Character;
import fr.unice.polytech.citadels.cards.characters.abilities.AbilityResult;
import fr.unice.polytech.citadels.cards.characters.abilities.AbilityType;
import fr.unice.polytech.citadels.cards.districts.District;
import fr.unice.polytech.citadels.players.Districts;
import fr.unice.polytech.citadels.players.Player;
import fr.unice.polytech.citadels.players.PlayerAction;

import java.util.*;

public final class RandomStrategy extends Strategy {

    public RandomStrategy(Player player) {
        super(player);
    }

    public Character chooseCharacter(Character[] selectable, Character[] playable, List<Player> playerList) {
        // Choix d'un personnage aléatoirement parmi se proposer
        return selectable[new Random().nextInt(selectable.length)];
    }

    @Override
    public PlayerAction chooseGoldOrDistrict() {
        // Soit 1 le joueur prend de l'or, soit 2 il choisit de piocher
        return (new Random().nextInt(2) + 1) == 1 ? PlayerAction.PICK_GOLDS : PlayerAction.PICK_DISTRICTS;
    }

    public District chooseDistrict(District... districts) {
        Random random = new Random();
        // Choix d'un district aléatoirement parmis ce proposer
        return districts[random.nextInt(districts.length)];
    }

    @Override
    public PlayerAction whatToDo() {
        int i = randomInt(1, 4);
        if (i == 2 && player.canPlaceDistrict()) {
            return PlayerAction.PLACE_DISTRICT;
        } else if (i == 3 && player.canCollectGoldsFromDistrict()) {
            return PlayerAction.COLLECT_GOLDS_FROM_DISTRICTS;
        } else if (i == 4 && player.canUseAbility()) {
            return PlayerAction.USE_ABILITY;
        }
        return PlayerAction.FINISH;
    }

    /**
     * Choisi aléatoirement parmi les districts de la main un quartier à placer sur le plateau du joueur
     */
    @Override
    public boolean placeDistrict() {
        Random random = new Random();
        if (random.nextInt(2) == 0) {
            District d;
            int counter = 0;
            boolean hasPlaced;
            if (super.player.getDistrictsHand().size() > 0) {
                do {
                    counter++;
                    d = super.player.getDistrictsHand().get(random.nextInt(super.player.getDistrictsHand().size()));
                } while (!(hasPlaced =super.player.placeDistrict(d)) && counter != super.player.getDistrictsHand().size());
                return hasPlaced;
            }
        }
        return false;
    }

    @Override
    public AbilityResult warlordAbility(List<Player> players) {
        Random random = new Random();
        List<Districts> boards = players.stream().map(Player::getDistrictsBoard).toList();
        List<District> allBoards = boards.stream().flatMap(Collection::stream).toList();
        if (allBoards.size() > 0)
            return new AbilityResult(AbilityType.DESTROY_DISTRICT, allBoards.get(random.nextInt(allBoards.size())));
        return null;
    }

    @Override
    public AbilityResult magicianAbility(List<Player> players) {
        Random random = new Random();
        List<Player> newPlayerList = new ArrayList<>(players);
        newPlayerList.remove(super.player);
        return new AbilityResult(AbilityType.TRADE_CARDS, newPlayerList.get(random.nextInt(newPlayerList.size())).getCharacter());
    }

    public AbilityResult assassinAbility(Character[] characters) {
        Random random = new Random();
        return new AbilityResult(AbilityType.KILL, characters[random.nextInt(characters.length)]);
    }

    @Override
    public AbilityResult thiefAbility(Character[] characters) {
        Random random = new Random();
        return new AbilityResult(AbilityType.STEAL, characters[random.nextInt(characters.length)]);
    }

    private int randomInt(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        RandomStrategy that = (RandomStrategy) obj;
        return Objects.equals(super.player, that.player);
    }
}
