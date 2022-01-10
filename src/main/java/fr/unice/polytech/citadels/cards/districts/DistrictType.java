package fr.unice.polytech.citadels.cards.districts;

import java.util.Arrays;

public enum DistrictType {
    // NOBLE DISTRICTS
    MANOR("Manoir", 5, DistrictColor.yellow, 3, 3, true, ""),
    CASTLE("Château", 4, DistrictColor.yellow, 4, 4, true, ""),
    PALACE("Palais", 2, DistrictColor.yellow, 5, 5, true, ""),
    // RELIGIOUS DISTRICTS
    TEMPLE("Temple", 3, DistrictColor.blue, 1, 1, true, ""),
    CHURCH("Église", 4, DistrictColor.blue, 2, 2, true, ""),
    MONASTERY("Monastère", 3, DistrictColor.blue, 3, 3, true, ""),
    CATHEDRAL("Cathédrale", 2, DistrictColor.blue, 5, 5, true, ""),
    // TRADE DISTRICTS
    TAVERN("Taverne", 5, DistrictColor.green, 1, 1, true, ""),
    STALL("Échoppe", 3, DistrictColor.green, 2, 2, true, ""),
    MARKET("Marché", 4, DistrictColor.green, 2, 2, true, ""),
    COUNTER("Comptoir", 3, DistrictColor.green, 3, 3, true, ""),
    HARBOR("Port", 3, DistrictColor.green, 4, 4, true, ""),
    TOWN_HALL("Hôtel de ville", 2, DistrictColor.green, 5, 5, true, ""),
    // WAR DISTRICTS
    WATCHTOWER("Tour de guet", 3, DistrictColor.red, 1, 1, true, ""),
    JAIL("Prison", 3, DistrictColor.red, 2, 2, true, ""),
    BARRACKS("Caserne", 3, DistrictColor.red, 3, 3, true, ""),
    FORTRESS("Forteresse", 2, DistrictColor.red, 5, 5, true, ""),
    // UNIQUE DISTRICT
    COURT_OF_MIRACLES("Cour des miracles", 1, DistrictColor.purple, 2, 2, true, "Pour le décompte final des points, la cour des miracles est considérée comme un quartier de la couleur de votre choix. Vous ne pouvez pas utilisez cette capacité si vous avez construit la cour des miracles au dernier tour de jeu."),
    DUNGEON("Donjon", 1, DistrictColor.purple, 3, 3, false, "Le Donjon ne peut pas être détruit par le Condottière."),
    LABORATORY("Laboratoire", 1, DistrictColor.purple, 5, 5, true, "Une fois par tour, vous pouvez vous défausser d'une carte quartier de votre main et recevoir une pièce d'or en contrepartie."),
    MANUFACTURE("Manufacture", 1, DistrictColor.purple, 5, 5, true, "Une fois par tour, vous pouvez payer trois pièces d'or pour piocher trois cartes."),
    OBSERVATORY("Observatoire", 1, DistrictColor.purple, 5, 5, true, "Si vous choisissez de piocher des cartes au début de votre tour, vous en piochez trois, en choisissez une et défaussez les deux autres."),
    CEMETERY("Cimetière", 1, DistrictColor.purple, 5, 5, true, "Lorsque le Condottière détruit un quartier, vous pouvez payer une pièce d'or pour le reprendre dans votre main. Vous ne pouvez pas faire cela si vous êtes vous-même Condottiere."),
    LIBRARY("Bibliothèque", 1, DistrictColor.purple, 6, 6, true, "Si vous choisissez de piocher des cartes au début de votre tour, vous en piochez deux et les conservez toutes les deux."),
    SCHOOL_OF_MAGIC("École de magie", 1, DistrictColor.purple, 6, 6, true, "Pour la perception des revenus, l'école de magie est considérée comme un quartier de la couleur de votre choix, elle vous rapporte donc si vous êtes, Roi, Evèque, Marchand ou Condottiere."),
    UNIVERSITY("Université", 1, DistrictColor.purple, 6, 8, true, "Cette réalisation de prestige (nul n'a jamais compris à quoi pouvait bien servir une université) coûte six pièces d'or à bâtir mais vaux huit points dans le décompte de fin de partie."),
    DRACONIAN_HARBOR("Dracoport", 1, DistrictColor.purple, 6, 8, true, "Cette réalisation de prestige (on n'a pas vu de dragon dans le Royaume depuis bientôt mille ans) coûte six pièces d'or à bâtir mais vaut huit points dans le décompte de fin de partie.");

    private final String name;
    private final int amount;
    private final DistrictColor color;
    private final int price;
    private final int score;
    private final boolean destroyable;
    private final String description;

    DistrictType(String name, int amount, DistrictColor color, int price, int score, boolean destroyable, String description) {
        this.name = name;
        this.amount = amount;
        this.color = color;
        this.price = price;
        this.score = score;
        this.destroyable = destroyable;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public DistrictColor getColor() {
        return color;
    }

    public int getPrice() {
        return price;
    }

    public int getScore() {
        return score;
    }

    public boolean isDestroyable() {
        return destroyable;
    }

    public String getDescription() {
        return description;
    }

    public static DistrictType[] valuesOfColor(DistrictColor color) {
        return Arrays.stream(values()).filter(value -> value.getColor().equals(color)).toArray(DistrictType[]::new);
    }

}
