package fr.unice.polytech.citadels.cards.districts;

import fr.unice.polytech.citadels.CColors;

public enum DistrictColor {

    yellow(CColors.YELLOW),
    blue(CColors.BLUE),
    green(CColors.GREEN),
    red(CColors.RED),
    purple(CColors.MAGENTA);

    private final CColors textColor;

    DistrictColor(CColors textColor) {
        this.textColor = textColor;
    }

    public CColors getTextColor() {
        return textColor;
    }
}
