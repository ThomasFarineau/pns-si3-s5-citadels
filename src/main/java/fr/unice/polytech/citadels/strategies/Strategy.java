package fr.unice.polytech.citadels.strategies;

import fr.unice.polytech.citadels.Logger;
import fr.unice.polytech.citadels.cards.characters.Character;
import fr.unice.polytech.citadels.cards.characters.CharacterList;
import fr.unice.polytech.citadels.cards.characters.CharacterType;
import fr.unice.polytech.citadels.cards.characters.abilities.AbilityResult;
import fr.unice.polytech.citadels.cards.characters.abilities.AbilityType;
import fr.unice.polytech.citadels.cards.districts.District;
import fr.unice.polytech.citadels.cards.districts.DistrictColor;
import fr.unice.polytech.citadels.players.Player;
import fr.unice.polytech.citadels.players.PlayerAction;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class Strategy {
    Player player;

    public Strategy(Player player) {
        this.player = player;
    }

    public abstract Character chooseCharacter(Character[] selectable, Character[] playable, List<Player> playerList);

    public void switchStrategy() {
        if (player.getStartingStrategy() instanceof FiveColorsStrategy && !player.getDistrictsBoard().containsColorSet()) {
            try {
                player.setStrategy(FiveColorsStrategy.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            printStrategyChange();
        }
    }

    public void printStrategyChange() {
        Logger.println(1, "" + player.getName() + " a changé de stratégie et utilise désormais " +
                player.getStrategy().getSimpleName()  + ".", 1, player);
    }

    public abstract PlayerAction chooseGoldOrDistrict();

    public abstract District chooseDistrict(District... districts);

    public abstract boolean placeDistrict();

    public PlayerAction whatToDo() {
        return PlayerAction.FINISH;
    }

    //abilities
    public void usesAbility() {
        player.setUsesAbility(true);
    }

    public AbilityResult ability(List<Player> players, CharacterList characters) {
        return switch (player.getCharacter().getName()) {
            case "Assassin" -> assassinAbility(characters.playableCharacters(CharacterType.ASSASSIN.getOrder() + 1), players);
            case "Voleur" -> thiefAbility(characters.playableCharacters(CharacterType.THIEF.getOrder() + 1));
            case "Magicien" -> magicianAbility(players);
            case "Condottière" -> warlordAbility(players.stream().filter(player1 -> !player1.getCharacter().getType().equals(CharacterType.BISHOP)).toList());
            default -> null;
        };
    }

    public AbilityResult warlordAbility(List<Player> players) {
        ArrayList<Player> editablePlayers = new ArrayList<>(players);
        editablePlayers.remove(player);
        //editablePlayers.sort(Comparator.comparingInt(Player::getDistrictBoardSize).reversed());
        editablePlayers.sort((o1, o2) -> {
            Integer size1 = o1.getDistrictBoardSize();
            Integer size2 = o2.getDistrictBoardSize();
            int sComp = size1.compareTo(size2);

            if (sComp != 0) {
                return -sComp;
            }

            Boolean color1 = o1.getDistrictsBoard().containsColorSet();
            Boolean color2 = o2.getDistrictsBoard().containsColorSet();
            return color1.compareTo(color2);
        });
        for (Player p : editablePlayers) {
            HashMap<District, Integer> map = p.getDistrictsBoard().getDestroyable();
            for (Map.Entry<District, Integer> districtIntegerEntry : map.entrySet()) {
                if (districtIntegerEntry.getValue().equals(0))
                    return new AbilityResult(AbilityType.DESTROY_DISTRICT, districtIntegerEntry.getKey());
            }
        }
        return null;
    }

    public AbilityResult magicianAbility(List<Player> players) {
        List<Player> newPlayerList = new ArrayList<>(players);
        newPlayerList.remove(player);

        Player maxSizeOfHand = null;
        for (Player player : newPlayerList)
            if (Objects.isNull(maxSizeOfHand) || maxSizeOfHand.getDistrictsHand().size() < player.getDistrictsHand().size())
                maxSizeOfHand = player;
        assert maxSizeOfHand != null;
        return new AbilityResult(AbilityType.TRADE_CARDS, maxSizeOfHand.getCharacter());
    }

    public AbilityResult thiefAbility(Character[] characters) {
        Character c = null;
        for (Character character : characters) if (character.getType().equals(CharacterType.ARCHITECT)) c = character;
        if (c == null) for (Character character : characters)
            if (character.getType().equals(CharacterType.MERCHANT)) c = character;
        if (c == null) for (Character character : characters)
            if (character.getType().equals(CharacterType.BISHOP) || character.getType().equals(CharacterType.KING))
                c = character;
        Random random = new Random();
        if (c == null) c = characters[random.nextInt(characters.length)];
        return new AbilityResult(AbilityType.STEAL, c);
    }

    public AbilityResult assassinAbility(Character[] characters, List<Player> playerList) {
        List<Character> l = new ArrayList<>(Arrays.asList(characters));
        if (player.getDistrictBoardSize() >= 5 && l.contains(new Character(CharacterType.WARLORD)))
            return new AbilityResult(AbilityType.KILL, new Character(CharacterType.WARLORD));

        if (player.getDistrictsHand().size() >= 3 && l.contains(new Character(CharacterType.MAGICIAN)))
            return new AbilityResult(AbilityType.KILL, new Character(CharacterType.MAGICIAN));

        //Sinon au hasard
        Random random = new Random();
        return new AbilityResult(AbilityType.KILL, characters[random.nextInt(characters.length)]);
    }

    public static HashMap<DistrictColor, Integer> sortByValue(HashMap<DistrictColor, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<DistrictColor, Integer>> list = new LinkedList<>(hm.entrySet());
        // Sort the list
        list.sort(Map.Entry.comparingByValue());
        // put data from sorted list to hashmap
        return IntStream.iterate(list.size() - 1, i -> i >= 0, i -> i - 1).mapToObj(list::get).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }

    public Player getPlayer() {
        return player;
    }

    Character containsCharacter(Character[] characters, CharacterType type) {
        for (Character character : characters)
            if (character.getType().equals(type)) return character;
        return null;
    }
}
