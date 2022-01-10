package fr.unice.polytech.citadels.strategies;

import fr.unice.polytech.citadels.GameController;
import fr.unice.polytech.citadels.Main;
import fr.unice.polytech.citadels.cards.characters.Character;
import fr.unice.polytech.citadels.cards.characters.CharacterType;
import fr.unice.polytech.citadels.cards.characters.abilities.AbilityResult;
import fr.unice.polytech.citadels.cards.characters.abilities.AbilityType;
import fr.unice.polytech.citadels.cards.districts.District;
import fr.unice.polytech.citadels.players.Districts;
import fr.unice.polytech.citadels.players.Player;
import fr.unice.polytech.citadels.players.PlayerAction;

import java.util.*;

/**
 * Basé sur les conseils de Richard ainsi que d'Alphonse sur le forum trictrac
 * https://www.trictrac.net/forum/sujet/citadelles-charte-citadelles-de-base
 */
public class AggressiveStrategy extends Strategy {
    public AggressiveStrategy(Player player) {
        super(player);
    }

    @Override
    public Character chooseCharacter(Character[] selectable, Character[] playable, List<Player> playerList) {
        if (isAboutToPlaceTheLastDistrict(selectable, playable) != null)
            return isAboutToPlaceTheLastDistrict(selectable, playable);

        if (selectAssassin(selectable, playable, playerList)) return containsCharacter(selectable, CharacterType.ASSASSIN);
        if (selectWarlord(selectable, playerList)) return containsCharacter(selectable, CharacterType.WARLORD);
        if (selectMagician(selectable, playable, playerList)) return containsCharacter(selectable, CharacterType.MAGICIAN);
        if (selectThief(selectable, playable, playerList)) return containsCharacter(selectable, CharacterType.THIEF);

        if (containsCharacter(selectable, CharacterType.THIEF) != null && player.getGold() <= 2 && GameController.round <= 4)
            return containsCharacter(selectable, CharacterType.THIEF);
        if(selectKing(selectable,playable,playerList)) return containsCharacter(selectable, CharacterType.KING);
        if(selectArchitect(selectable,playable,playerList)) return containsCharacter(selectable, CharacterType.ARCHITECT);

        return selectable[0];
    }

    public boolean selectAssassin(Character[] selectable, Character[] playable, List<Player> playerList) {
        return containsCharacter(selectable, CharacterType.ASSASSIN) != null && (containsCharacter(playable, CharacterType.MAGICIAN) != null && playerList.stream().filter(p -> !p.equals(player)).mapToInt(p -> p.getDistrictsHand().size()).sum() < player.getDistrictsHand().size() || containsCharacter(playable, CharacterType.THIEF) == null && player.getGold() >= 4 || (containsCharacter(playable, CharacterType.ARCHITECT) != null || containsCharacter(playable, CharacterType.KING) != null) && playerList.stream().anyMatch(p -> !p.equals(player) && (p.getDistrictsBoard().size() >= Main.DISTRICTS_FOR_WIN - 3 && p.getGold() >= 5 || p.getDistrictsBoard().size() == Main.DISTRICTS_FOR_WIN - 1)));
    }

    public boolean selectWarlord(Character[] selectable, List<Player> playerList) {
        return containsCharacter(selectable, CharacterType.WARLORD) != null && playerList.stream().filter(p -> !p.equals(player)).anyMatch(p -> p.getDistrictsBoard().size() == Main.DISTRICTS_FOR_WIN - 2);
    }

    public boolean selectMagician(Character[] selectable, Character[] playable, List<Player> playerList) {
        if (containsCharacter(selectable, CharacterType.MAGICIAN) == null) return false;
        if(player.getDistrictsHand().size() <= playerList.stream().filter(p -> !p.equals(player)).mapToInt(p -> p.getDistrictsHand().size()).sum()/playerList.size() && playerList.stream().filter(p -> !p.equals(player)).mapToInt(p -> p.getDistrictsHand().size()).sum()/playerList.size() >= 3 && GameController.round <= 2)
            return true;
        return false;
    }

    public boolean selectThief(Character[] selectable, Character[] playable, List<Player> playerList) {
        if (containsCharacter(selectable, CharacterType.THIEF) == null) {
            return false;
        }
        return player.getGold() <= 2 && GameController.round <= 4;
    }

    public boolean selectArchitect(Character[] selectable, Character[] playable, List<Player> playerList) {
        if (containsCharacter(selectable, CharacterType.ARCHITECT) == null) return false;
        return (player.getGold()>=4 && player.getDistrictsBoard().size()>=5 && player.getDistrictsHand().size()>=1);
    }

    public boolean selectKing(Character[] selectable, Character[] playable, List<Player> playerList) {
        if (containsCharacter(selectable, CharacterType.KING) == null) return false;
        ArrayList<Player> newPlayerList = new ArrayList<>(playerList);
        newPlayerList.remove(player);
        for(Player p : newPlayerList) {
            if (p.getDistrictsBoard().size() == Main.DISTRICTS_FOR_WIN - 2) {
                return true;
            }
        }
        return false;
    }

    public boolean selectBishop(Character[] selectable, Character[] playable, List<Player> playerList) {
        return ((Arrays.stream(selectable).filter(c -> c.getType()!=CharacterType.BISHOP)).count()!=0);
    }

    public boolean isAboutToWin(Player p) {
        return p.getDistrictsBoard().size() == Main.DISTRICTS_FOR_WIN - 1 && p.mayPurchaseADistrict();
    }

    public Character isAboutToPlaceTheLastDistrict(Character[] selectable, Character[] playable) {
        if (isAboutToWin(player)) {
            if (containsCharacter(playable, CharacterType.WARLORD) != null && containsCharacter(playable, CharacterType.ASSASSIN) != null && containsCharacter(playable, CharacterType.BISHOP) != null) {
                if (containsCharacter(selectable, CharacterType.WARLORD) != null)
                    return containsCharacter(selectable, CharacterType.WARLORD);
                if (containsCharacter(selectable, CharacterType.ASSASSIN) != null)
                    return containsCharacter(selectable, CharacterType.ASSASSIN);
            }
            if (containsCharacter(playable, CharacterType.WARLORD) != null && containsCharacter(playable, CharacterType.ASSASSIN) != null && containsCharacter(playable, CharacterType.BISHOP) == null) {
                if (containsCharacter(selectable, CharacterType.ASSASSIN) != null)
                    return containsCharacter(selectable, CharacterType.ASSASSIN);
                if (containsCharacter(selectable, CharacterType.WARLORD) != null)
                    return containsCharacter(selectable, CharacterType.WARLORD);
            }
            if (containsCharacter(playable, CharacterType.WARLORD) == null && containsCharacter(playable, CharacterType.ASSASSIN) != null && containsCharacter(playable, CharacterType.MAGICIAN) != null) {
                if (containsCharacter(selectable, CharacterType.ASSASSIN) != null)
                    return containsCharacter(selectable, CharacterType.ASSASSIN);
                if (containsCharacter(selectable, CharacterType.MAGICIAN) != null)
                    return containsCharacter(selectable, CharacterType.MAGICIAN);
            }
            if (containsCharacter(playable, CharacterType.WARLORD) != null && containsCharacter(playable, CharacterType.ASSASSIN) == null && containsCharacter(playable, CharacterType.BISHOP) != null) {
                if (containsCharacter(selectable, CharacterType.WARLORD) != null)
                    return containsCharacter(selectable, CharacterType.WARLORD);
                if (containsCharacter(selectable, CharacterType.BISHOP) != null)
                    return containsCharacter(selectable, CharacterType.BISHOP);
            }
        }
        return null;
    }

    @Override
    public PlayerAction whatToDo() {
        // Si le joueur peut utiliser son ability alors il l'a joue
        if (player.canUseAbility()) return PlayerAction.USE_ABILITY;
        // Si on peut le poser alors on choisit de poser son quartier
        if (player.mayPurchaseADistrict() && player.canPlaceDistrict())
            return PlayerAction.PLACE_DISTRICT;
        // Si on peut récupérer les po avec les districts alors on le fait
        if (getPlayer().canCollectGoldsFromDistrict()) return PlayerAction.COLLECT_GOLDS_FROM_DISTRICTS;
        return PlayerAction.FINISH;
    }

    @Override
    public PlayerAction chooseGoldOrDistrict() {
        // Soit 1 le joueur prend de l'or, soit 2 il choisit de piocher
        return (new Random().nextInt(2) + 1) == 1 ? PlayerAction.PICK_GOLDS : PlayerAction.PICK_DISTRICTS;
    }

    @Override
    public District chooseDistrict(District... districts) {
        Random random = new Random();
        // Choix d'un district aléatoirement parmis ce proposer
        return districts[random.nextInt(districts.length)];
    }

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
                } while (!(hasPlaced = super.player.placeDistrict(d)) && counter != super.player.getDistrictsHand().size());
                return hasPlaced;
            }
        }
        return false;
    }

    /**
     * Assassin :
     * +
     * Il est peu vulnérable, sauf au condottiere et au magicien
     * Il peut ralentir un autre joueur
     * -
     * Il ne rapporte RIEN. Un joueur qui prend trop fréquemment l'assassin gagne rarement
     * Quand vous avez beaucoup de cartes et que les autres ont des mains vides, prenez l'assassin pour assassiner le magicien
     *
     * - Je ne tue pas le Voleur sauf :
     * >si je veux éviter qu’un joueur s’enrichisse honteusement
     * >si je suis certain que le Voleur a été pris par un joueur en passe de gagner
     * Sachant que le Voleur ne peut pas voler l’Assassin, vous ne risquez rien.
     * Tuer le Voleur élimine un personnage agressif, donc une action à l’encontre de vos adversaires.
     *
     * - Je ne tue pas le Condottiere sauf :
     * >si je suis en tête
     * >si je pense que le Condottiere a été pris par un joueur en passe de gagner
     * Si vous n’êtes pas en tête, vous ne risquez rien (sauf si vous êtes le seul à avoir un quartier à 1 PO)
     * Tuer le Condottiere élimine un personnage agressif, donc une action à l’encontre de vos adversaires.
     * */

    @Override
    public AbilityResult assassinAbility(Character[] characters, List<Player> playerList) {
        List<Character> playable = new ArrayList<>(Arrays.asList(characters));
        if (player.getGold() >= 5 && playable.contains(new Character(CharacterType.THIEF))) {
            return new AbilityResult(AbilityType.KILL, new Character(CharacterType.THIEF));
        }
        if (isAboutToWin(player) && playable.contains(new Character(CharacterType.WARLORD))) {
            return new AbilityResult(AbilityType.KILL, new Character(CharacterType.WARLORD));
        }
        if (player.getDistrictsHand().size() >= 3 && playable.contains(new Character(CharacterType.MAGICIAN))) {
            return new AbilityResult(AbilityType.KILL, new Character(CharacterType.MAGICIAN));
        }
        if (playable.contains(new Character(CharacterType.ARCHITECT))) {
            if (playerList.stream().filter(this::architectConditions).toList().size() != 0) {
                return new AbilityResult(AbilityType.KILL, new Character(CharacterType.ARCHITECT));
            }
        }
        return new AbilityResult(AbilityType.KILL,characters[0]);
    }

    @Override
    public AbilityResult magicianAbility(List<Player> players) {
        List<Player> newPlayerList = new ArrayList<>(players);
        newPlayerList.remove(player);
        for (Player player1 : newPlayerList) {
            if (isAboutToWin(player1)) {
                return new AbilityResult(AbilityType.TRADE_CARDS, player1.getCharacter());
            }
        }
        return null;
    }

    @Override
    public AbilityResult warlordAbility(List<Player> players) {
        ArrayList<Player> editable = new ArrayList<>(players);
        editable.remove(player);
        for (Player player1 : editable) {
            if (isAboutToWin(player1)) {
                Districts d = player1.getDistrictsBoard();
                d.sort(Comparator.comparingInt(District::getPrice));
                return new AbilityResult(AbilityType.DESTROY_DISTRICT, d.get(0));
            }
        }
        return null;
    }

    public boolean architectConditions(Player p) {
        return p.getGold() >= 4 && p.getDistrictsHand().size() >=1 && p.getDistrictBoardSize() >= 5;
    }

    @Override
    public AbilityResult thiefAbility(Character[] characters) {
        Random random = new Random();
        // Choix d'une cible aléatoire dans l'espoir de tomber sur quelqu'un avec beaucoup de gold
        return new AbilityResult(AbilityType.STEAL,characters[random.nextInt(characters.length)]);
    }
}
