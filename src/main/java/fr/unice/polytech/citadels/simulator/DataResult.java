package fr.unice.polytech.citadels.simulator;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DataResult {
    private final String file;

    public DataResult(String filename) {
        file = filename;
    }

    boolean createFile() {
        try {
            File resultsFile = new File(file);
            Path path = Paths.get(resultsFile.getParentFile().getAbsolutePath());
            Files.createDirectories(path);
            return resultsFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void writeLine(String strategy, int win, float winRate, int lose, float loseRate, int draw, float drawRate, float averageScore, int gamesPlayed) {
        try {
            if (findIndex(strategy) != -1) {
                deleteLine(findIndex(strategy));
            }
            CSVWriter writer = new CSVWriter(new FileWriter(file, true), ',', CSVWriter.NO_QUOTE_CHARACTER);
            String[] data = {strategy, String.valueOf(win), String.valueOf(winRate), String.valueOf(lose), String.valueOf(loseRate), String.valueOf(draw), String.valueOf(drawRate), String.valueOf(averageScore), String.valueOf(gamesPlayed)};
            writer.writeNext(data);
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void printLine(String strategy, int win, float winRate, int lose, float loseRate, int draw, float drawRate, float averageScore, int gamesPlayed) {
        createFile();
        if (getValue(strategy) == null)
            writeLine(strategy, win, winRate, lose, loseRate, draw, drawRate, averageScore, gamesPlayed);
        else {
            List<String> oldValues = getValue(strategy);
            assert oldValues != null;
            int newWin = Integer.parseInt(oldValues.get(1)) + win;
            int newLose = Integer.parseInt(oldValues.get(3)) + lose;
            int newDraw = Integer.parseInt(oldValues.get(5)) + draw;
            int newGames = Integer.parseInt(oldValues.get(8)) + gamesPlayed;
            float newWinRate = getRate(newWin, newGames);
            float newLoseRate = getRate(newLose, newGames);
            float newDrawRate = getRate(newDraw, newGames);
            float newAverage = (Float.parseFloat(oldValues.get(7)) * Integer.parseInt(oldValues.get(8))) + (averageScore * gamesPlayed);
            newAverage = newAverage / newGames;

            writeLine(strategy, newWin, newWinRate, newLose, newLoseRate, newDraw, newDrawRate, newAverage, newGames);
        }

    }

    float getRate(int type, int games) {
        return (float) ((100.0 * type) / games);
    }

    void deleteLine(int index) {
        try {
            CSVReader reader = new CSVReader(new FileReader(file), ',', CSVWriter.NO_QUOTE_CHARACTER);
            List<String[]> allElements = reader.readAll();
            reader.close();
            if (allElements.size() >= index - 1) allElements.remove(index);
            CSVWriter rewriter = new CSVWriter(new FileWriter(file), ',', CSVWriter.NO_QUOTE_CHARACTER);
            rewriter.writeAll(allElements);
            rewriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int findIndex(String strategy) {
        try {
            CSVReader reader = new CSVReader(new FileReader(file), ',', CSVWriter.NO_QUOTE_CHARACTER);
            List<String[]> allElements = reader.readAll();
            for (int i = 0; i < allElements.size(); i++) {
                if (allElements.get(i)[0].equals(strategy)) {
                    reader.close();
                    return i;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<String> getValue(String strategy) {
        if (findIndex(strategy) != -1) {
            try {
                CSVReader reader = new CSVReader(new FileReader(file), ',', CSVWriter.NO_QUOTE_CHARACTER);
                List<String> toReturn = List.of(reader.readAll().get(findIndex(strategy)));
                reader.close();
                return toReturn;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
