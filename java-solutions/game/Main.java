package game;

import java.io.EOFException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)){
            Tournament tournament = new Tournament(
                    scanner,
                    List.of(new RandomPlayer(), new RandomPlayer()),
                    true,
                    true
            );

            while (tournament.hasNextRound()) {
                tournament.playRound();
            }

            tournament.showTable();
        } catch (EOFException ignored) { }
            // user typed Ctrl + D
    }
}
