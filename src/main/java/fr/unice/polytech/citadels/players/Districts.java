package fr.unice.polytech.citadels.players;

import fr.unice.polytech.citadels.GameController;
import fr.unice.polytech.citadels.Main;
import fr.unice.polytech.citadels.cards.districts.District;
import fr.unice.polytech.citadels.cards.districts.DistrictColor;
import fr.unice.polytech.citadels.cards.districts.DistrictType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Districts extends ArrayList<District> {

    public boolean containsByType(DistrictType type) {
        return this.stream().anyMatch(district -> district.getType().equals(type));
    }

    public District[] getByType(DistrictType type) {
        return this.stream().filter(district -> district.getType().equals(type)).toArray(District[]::new);
    }

    public District[] getByColor(DistrictColor color) {
        return this.stream().filter(district -> district.getColor().equals(color)).toArray(District[]::new);
    }

    public void addDistricts(District... districts) {
        this.addAll(List.of(districts));
    }

    public void removeDistricts(District... districts) {
        this.removeAll(List.of(districts));
    }

    // Ne pas utiliser cette méthode s'il s'agit d'une main
    public void place(District district) {
        this.add(district);
        district.place();
    }

    public int score() {
        return this.stream().mapToInt(District::getScore).sum();
    }

    public boolean containsColorSet() {
        Set<DistrictColor> colorSet = this.stream().map(District::getColor).collect(Collectors.toSet()); // Compte le nombre de couleurs de quartiers différents
        if(colorSet.size() == 5) return true;
        // Si contiens 2 cartes violettes && Si contient la cour des miracles
        if (colorSet.size() == 4 && this.getByColor(DistrictColor.purple).length >= 2 && getByType(DistrictType.COURT_OF_MIRACLES).length > 0) {
            District district = getByType(DistrictType.COURT_OF_MIRACLES)[0];
            // Return si n'a pas été placé au dernier tour
            return district.getPlacedOnRound() < GameController.round;
        }
        return false;
    }

    public HashMap<District, Integer> getDestroyable() {
        HashMap<District, Integer> map = new HashMap<>();
        if(this.size() < Main.DISTRICTS_FOR_WIN) for (District d : this) {
            if (d.isDestroyable()) map.put(d, d.getPrice() - 1);
        }
        return map;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        IntStream.range(0, this.size()).forEach(i -> {
            string.append(this.get(i));
            if (i <= this.size() - 2) string.append(", ");
        });
        return string.toString();
    }
}
