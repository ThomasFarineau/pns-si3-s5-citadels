package fr.unice.polytech.citadels.cards.characters;

import fr.unice.polytech.citadels.cards.districts.DistrictColor;

public enum CharacterType {
    ASSASSIN(1, "Assassin", null),
    THIEF(2, "Voleur", null),
    MAGICIAN(3, "Magicien", null),
    KING(4, "Roi", DistrictColor.yellow),
    BISHOP(5, "Évêque", DistrictColor.blue),
    MERCHANT(6, "Marchand", DistrictColor.green),
    ARCHITECT(7, "Architecte", null),
    WARLORD(8, "Condottière", DistrictColor.red);

    private final int order;
    private final String name;
    private final DistrictColor color;

    CharacterType(int order, String name, DistrictColor color) {
        this.order = order;
        this.name = name;
        this.color = color;
    }

    public int getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }

    public DistrictColor getColor() {
        return color;
    }
}
