import java.io.EOFException;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class HumanPlayer implements Player {
    private final PrintStream out;
    private final Scanner in;

    public HumanPlayer(final PrintStream out, final Scanner in) {
        this.out = out;
        this.in = in;
    }

    public HumanPlayer(Scanner in) {
        this(System.out, in);
    }

    @Override
    public Move move(final Position position, final Cell turn) throws EOFException {
        while (true) {
            out.println("Position");
            out.println(position.getBoardView());

            out.println(turn + "'s move");
            out.println("Enter row and column of a piece and destination row and column");


            // handling Ctrl+D
            if (!in.hasNextLine()) {
                throw new EOFException("Input terminated");
            }

            String inputLine = in.nextLine();

            if (InputChecker.isValidInput(inputLine)) {
                try {
                    int[] lineArgs = InputManager.parseLine(inputLine, 4);
                    return new Move(lineArgs[0], lineArgs[1], lineArgs[2], lineArgs[3], turn);
                } catch (NoSuchElementException e) {
                    System.out.println("Not enough arguments");
                }
            } else {
                out.println("Please, enter correct input data");
            }
        }
    }
}
