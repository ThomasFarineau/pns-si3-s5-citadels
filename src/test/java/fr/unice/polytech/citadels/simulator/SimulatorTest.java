package fr.unice.polytech.citadels.simulator;

import fr.unice.polytech.citadels.GameController;
import fr.unice.polytech.citadels.strategies.CheapDistrictStrategy;
import fr.unice.polytech.citadels.strategies.ExpGoldStrategy;
import fr.unice.polytech.citadels.strategies.FiveColorsStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.File;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;

class SimulatorTest {
    @Mock
    GameController mock = spy(new GameController());

    @Mock
    Simulator mock2 = spy(new Simulator(10, true, -1, "save/testMockCSV" , new GameController(), ExpGoldStrategy.class, FiveColorsStrategy.class));

    @Test
    void testMultipleGames() {
        Simulator sim = spy(new Simulator(100, false, -1, "Not needed", mock, CheapDistrictStrategy.class, FiveColorsStrategy.class));
        sim.run();
        Mockito.verify(mock, Mockito.times(100)).run();
        Mockito.verify(sim, never()).save(null);
    }

    @Test
    void testCSV() {
        mock2.run();
        Mockito.verify(mock2, Mockito.times(1)).run();
        File file1 = new File("save/testMockCSV");
        file1.delete();
    }

}