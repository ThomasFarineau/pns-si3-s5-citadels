package fr.unice.polytech.citadels.cards.districts;

import org.junit.jupiter.api.Test;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DistrictFactoryTest {

    @Test
    void getDistrictList() {
        DistrictFactory districtFactory = new DistrictFactory();
        assertEquals(64, districtFactory.getDistrictList().size());
    }

}