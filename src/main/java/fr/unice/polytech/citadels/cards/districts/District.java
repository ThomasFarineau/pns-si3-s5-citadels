package fr.unice.polytech.citadels.cards.districts;

import fr.unice.polytech.citadels.CColors;
import fr.unice.polytech.citadels.GameController;

public class District {
    public static int indenter = 1;

    private final int id;
    private final DistrictType type;
    private int placedOnRound = 0;

    public District(DistrictType type) {
        id = indenter++;
        this.type = type;
    }

    public DistrictType getType() {
        return type;
    }

    public void place() {
        placedOnRound = GameController.round;
    }

    public int getPlacedOnRound() {
        return placedOnRound;
    }

    public String getName() {
        return type.getName();
    }

    public int getPrice() {
        return type.getPrice();
    }

    public DistrictColor getColor() {
        return type.getColor();
    }

    public int getScore() {
        return type.getScore();
    }

    public boolean isDestroyable() {
        return type.isDestroyable();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof District district)) return false;
        return id == district.id && type == district.type;
    }


    @Override
    public String toString(){
        return getColor().getTextColor() + getName() + " C" + getPrice() + " S" + getScore() + CColors.RESET;
    }

}
