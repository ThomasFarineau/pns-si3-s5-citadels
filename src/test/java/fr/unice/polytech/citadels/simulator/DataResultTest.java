package fr.unice.polytech.citadels.simulator;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.logging.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class DataResultTest {
    private final String file = "save/resultsTest.csv";
    private DataResult dataResult;

    @BeforeEach
    void setUp() {
        File resultsFile = new File(file);
        Path path = Paths.get(resultsFile.getParentFile().getAbsolutePath());
        try {
            Files.createDirectories(path);
            resultsFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataResult = new DataResult(file);
    }

    @AfterEach
    void tearDown() {
        File file1 = new File(file);
        file1.delete();
    }

    @Test
    void testSetUpTearDown() {
        try {
            assertFalse(new File(file).createNewFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        tearDown();
        try {
            assertTrue(new File(file).createNewFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createFile() {
        assertFalse(dataResult.createFile());
        tearDown();
        assertTrue(dataResult.createFile());
    }

    @Test
    void writeLine() {
        dataResult.writeLine("CheapDistrictStrategy", 10, dataResult.getRate(10,40), 20, dataResult.getRate(20, 40), 10, dataResult.getRate(10, 40), 15, 40);
        testEqualityFile("CheapDistrictStrategy,10,25.0,20,50.0,10,25.0,15.0,40");
    }

    @Test
    void printLine() {
        dataResult.printLine("CheapDistrictStrategy", 10, dataResult.getRate(10,40), 20, dataResult.getRate(20, 40), 10, dataResult.getRate(10, 40), 15, 40);
        testEqualityFile("CheapDistrictStrategy,10,25.0,20,50.0,10,25.0,15.0,40");
        dataResult.printLine("CheapDistrictStrategy", 30, dataResult.getRate(30,40), 0, dataResult.getRate(0, 40), 10, dataResult.getRate(10, 40), 25, 40);
        testEqualityFile("CheapDistrictStrategy,40,50.0,20,25.0,20,25.0,20.0,80");
    }

    @Test
    void deleteLine() {
        String firstContent = "CheapDistrictStrategy,10,25.0,20,50.0,10,25.0,15.0,40";
        String secondContent = "ExpGoldStrategy,10,25.0,20,50.0,10,25.0,15.0,40";
        File fileF = new File(file);
        assertEquals(fileF.length(), 0);
        dataResult.writeLine("CheapDistrictStrategy", 10, dataResult.getRate(10,40), 20, dataResult.getRate(20, 40), 10, dataResult.getRate(10, 40), 15, 40);
        testEqualityFile(firstContent);
        dataResult.writeLine("ExpGoldStrategy", 10, dataResult.getRate(10,40), 20, dataResult.getRate(20, 40), 10, dataResult.getRate(10, 40), 15, 40);
        testEqualityFile(firstContent + "\n" + secondContent);
        dataResult.deleteLine(0);
        testEqualityFile(secondContent);
    }

    @Test
    void deleteLine2() {
        String firstContent = "CheapDistrictStrategy,10,25.0,20,50.0,10,25.0,15.0,40";
        dataResult.writeLine("CheapDistrictStrategy", 10, dataResult.getRate(10,40), 20, dataResult.getRate(20, 40), 10, dataResult.getRate(10, 40), 15, 40);
        dataResult.writeLine("ExpGoldStrategy", 10, dataResult.getRate(10,40), 20, dataResult.getRate(20, 40), 10, dataResult.getRate(10, 40), 15, 40);
        dataResult.deleteLine(1);
        testEqualityFile(firstContent);
    }

    @Test
    void findIndex() {
        dataResult.writeLine("CheapDistrictStrategy", 10, dataResult.getRate(10,40), 20, dataResult.getRate(20, 40), 10, dataResult.getRate(10, 40), 15, 40);
        dataResult.writeLine("ExpGoldStrategy", 10, dataResult.getRate(10,40), 20, dataResult.getRate(20, 40), 10, dataResult.getRate(10, 40), 15, 40);
        assertEquals(dataResult.findIndex("CheapDistrictStrategy"), 0);
        assertEquals(dataResult.findIndex("ExpGoldStrategy"), 1);
    }

    @Test
    void getValue() {
        dataResult.writeLine("CheapDistrictStrategy", 10, dataResult.getRate(10,40), 20, dataResult.getRate(20, 40), 10, dataResult.getRate(10, 40), 15, 40);
        assertEquals(String.join("\n", dataResult.getValue("CheapDistrictStrategy")), "CheapDistrictStrategy\n10\n25.0\n20\n50.0\n10\n25.0\n15.0\n40");
        assertNull(dataResult.getValue("FiveColorsStrategy"));
    }

    void testEqualityFile(String s) {
        String fileContent = "";
        try {
            fileContent = String.join("\n", Files.readAllLines(Path.of(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(fileContent, s);
    }
}