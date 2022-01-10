package fr.unice.polytech.citadels.cards.characters.abilities;

import fr.unice.polytech.citadels.cards.characters.Character;
import fr.unice.polytech.citadels.cards.districts.District;

public class AbilityResult {
    private final AbilityType type;
    private Character character;
    private District district;

    public AbilityResult(AbilityType type, Character character) {
        this.type = type;
        this.character = character;
    }

    public AbilityResult(AbilityType type, District district) {
        this.type = type;
        this.district = district;
    }

    public AbilityResult(AbilityType type, Character character, District district) {
        this.type = type;
        this.character = character;
        this.district = district;
    }

    public AbilityType getType() {
        return type;
    }

    public Character getCharacter() {
        return character;
    }

    public District getDistrict() { return district; }
}
