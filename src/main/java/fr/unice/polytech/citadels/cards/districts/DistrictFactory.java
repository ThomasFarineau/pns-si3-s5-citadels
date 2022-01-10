package fr.unice.polytech.citadels.cards.districts;

import java.util.Arrays;
import java.util.stream.IntStream;

public class DistrictFactory {
    private final DistrictList districtList = new DistrictList();

    public DistrictFactory() {
        District.indenter = 1;
        Arrays.stream(DistrictType.values()).forEach(value -> IntStream.range(0, value.getAmount()).mapToObj(i -> new District(value)).forEach(districtList::append));
    }

    public DistrictList getDistrictList() {
        return districtList;
    }

}
