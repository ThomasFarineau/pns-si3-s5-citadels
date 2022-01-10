package fr.unice.polytech.citadels.strategies;

import fr.unice.polytech.citadels.cards.characters.Character;
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

public final class CheapDistrictStrategy extends Strategy {

    public CheapDistrictStrategy(Player player) {
        super(player);
    }

    public static HashMap<DistrictColor, Integer> sortByValue(HashMap<DistrictColor, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<DistrictColor, Integer>> list = new LinkedList<>(hm.entrySet());
        // Sort the list
        list.sort(Map.Entry.comparingByValue());
        // put data from sorted list to hashmap
        return IntStream.iterate(list.size() - 1, i -> i >= 0, i -> i - 1).mapToObj(list::get).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }

    @Override
    public Character chooseCharacter(Character[] selectable, Character[] playable, List<Player> playerList) {
        HashMap<DistrictColor, Integer> colorIntegerMap = new HashMap<>();
        for (District district : player.getDistrictsBoard())
            colorIntegerMap.put(district.getColor(), colorIntegerMap.containsKey(district.getColor()) ? colorIntegerMap.get(district.getColor()) + 1 : 1);

        colorIntegerMap = sortByValue(colorIntegerMap);

        for (Map.Entry<DistrictColor, Integer> colorIntegerEntry : colorIntegerMap.entrySet())
            for (Character character : player.getCharactersToChoose())
                if (character.getDistrictColor() != null && colorIntegerEntry.getKey().equals(character.getDistrictColor()))
                    return character;

        if (player.getCharactersToChoose().contains(new Character(CharacterType.ARCHITECT)) && player.canPlaceMultipleDistricts(3))
            return player.getCharactersToChoose().get(player.getCharactersToChoose().indexOf(new Character(CharacterType.ARCHITECT)));
        return selectable[new Random().nextInt(selectable.length)];
    }

    @Override
    public PlayerAction chooseGoldOrDistrict() {
        return player.getGold() == 0 ? PlayerAction.PICK_GOLDS : PlayerAction.PICK_DISTRICTS;
    }

    public District chooseDistrict(District[] districts) {
        // Choix du quartier le moins cher
        return Arrays.stream(districts).min(Comparator.comparingInt(District::getPrice)).orElse(null);
    }

    @Override
    public boolean placeDistrict() {
        if (player.getDistrictsHand().size() > 0) {
            District moinsCherDeLaMain = player.getDistrictsHand().stream().min(Comparator.comparingInt(District::getPrice)).orElse(null);
            return player.placeDistrict(moinsCherDeLaMain);
        }
        return false;
    }

    @Override
    public PlayerAction whatToDo() {
        // On regarde son quartier le plus cher
        District cheapOne = player.getDistrictsHand().stream().min(Comparator.comparingInt(District::getPrice)).orElse(null);
        // Si le joueur peut utiliser son ability alors il l'a joue
        if(player.canUseAbility()) return PlayerAction.USE_ABILITY;
        // Si on peut le poser alors on choisit de poser son quartier
        if(cheapOne != null && cheapOne.getPrice() <= getPlayer().getGold() && getPlayer().canPlaceDistrict())
            return PlayerAction.PLACE_DISTRICT;
        // Si on peut récupérer les po avec les districts alors on le fait
        if(getPlayer().canCollectGoldsFromDistrict()) return PlayerAction.COLLECT_GOLDS_FROM_DISTRICTS;
        return PlayerAction.FINISH;
    }

    @Override
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

    @Override
    public AbilityResult assassinAbility(Character[] characters, List<Player> playerList) {
        Random random = new Random();
        return new AbilityResult(AbilityType.KILL, characters[random.nextInt(characters.length)]);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CheapDistrictStrategy) obj;
        return Objects.equals(this.player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player);
    }
}
