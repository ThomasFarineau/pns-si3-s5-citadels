package fr.unice.polytech.citadels.cards.districts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.IntStream;

public class DistrictList extends LinkedHashSet<District> {

    public void shuffle() {
        List<District> temp = new ArrayList<>(this);
        Collections.shuffle(temp);
        this.clear();
        this.addAll(temp);
    }

    public District pick() {
        if(size() >= 1) {
            District toPick = this.stream().toList().get(0);
            this.remove(toPick);
            return toPick;
        }
        return null;
    }

    public District[] pick(int number) {
        return IntStream.range(0, number).mapToObj(i -> pick()).toArray(District[]::new);
    }

    public void append(District... districts) {
        this.addAll(List.of(districts));
    }

}
