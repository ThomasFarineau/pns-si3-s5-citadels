package fr.unice.polytech.citadels;

import fr.unice.polytech.citadels.players.Player;

import java.util.Objects;
import java.util.stream.IntStream;

public class Logger {

    public static void println(int indent, String message, int level, Player player) {
        print(indent, message + "\n", level, player);
    }

    public static void println(String message, int level) {
        print(0, message + "\n", level, null);
    }

    public static void println(String message, int level, Player player) {
        print(0, message + "\n", level, player);
    }

    public static void println(int indent, String message, int level) {
        print(indent, message + "\n", level, null);
    }

    public static void print(int indent, String message, int level, Player player) {
        StringBuilder messageBuilder = new StringBuilder(message);
        if (indent > 0) messageBuilder.insert(0, "- ");
        IntStream.range(1, indent).forEach(i -> messageBuilder.insert(0, "  "));
        message = messageBuilder.toString();
        if (Main.VERBOSE.getLevel() >= level || (!Objects.isNull(Main.VERBOSE.getPlayerToFocus()) && Main.VERBOSE.getPlayerToFocus().equals(player))) {
            System.out.print(message + CColors.RESET);
        }
    }

    public static void print(String message, int level) {
        print(0, message, level, null);
    }

    public static void print(String message, int level, Player player) {
        print(0, message, level, player);
    }

    public static void print(int indent, String message, int level) {
        print(indent, message, level, null);
    }

}
