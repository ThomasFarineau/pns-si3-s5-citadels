package fr.unice.polytech.citadels.cards.characters.abilities;

import fr.unice.polytech.citadels.cards.characters.CharacterType;
import fr.unice.polytech.citadels.cards.districts.District;
import fr.unice.polytech.citadels.cards.characters.Character;
import fr.unice.polytech.citadels.cards.districts.DistrictColor;
import fr.unice.polytech.citadels.cards.districts.DistrictType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbilityResultTest {
    @Test
    void getType() {
        Character ch = new Character(CharacterType.ASSASSIN);
        AbilityResult ab = new AbilityResult(AbilityType.KILL,ch);
        assertEquals(ab.getType(),AbilityType.KILL);
    }
    @Test
    void getCharacter() {
        Character ch = new Character(CharacterType.THIEF);
        AbilityResult ab = new AbilityResult(AbilityType.STEAL,ch);
        assertEquals(ab.getCharacter(),ch);
    }
    @Test
    void getDistrict() {
        District di = new District(DistrictType.MANOR);
        AbilityResult ab = new AbilityResult(AbilityType.TRADE_CARDS,di);
        assertEquals(ab.getDistrict(),di);
    }
}