import java.io.EOFException;
import java.util.List;
import java.util.Scanner;

// :NOTE: * ЭВРИСТИЧЕСКИЙ ИГРОК
public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Tournament tournament = new Tournament(
                    List.of(new RandomPlayer(), new RandomPlayer(), new RandomPlayer(), new RandomPlayer()),
                    true,
                    true
            );

            while (tournament.hasNextRound()) {
                tournament.playRound();
            }

            tournament.showTable();
        } catch (EOFException ignored) {
            System.out.println("Input terminated");
        }
    }
}
