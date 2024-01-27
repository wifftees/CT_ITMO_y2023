package game;

import java.io.EOFException;

public class Game {
    private final boolean log;
    private final Player player1, player2;
    // Game should manage players switching, so it should know whose turn is now.
    private Cell turn;
    private final GameChecker gameChecker;

    public Game(final boolean log, final Player player1, final Player player2, int k) {
        this.log = log;
        this.player1 = player1;
        this.player2 = player2;
        this.turn = Cell.X;

        // we do not trust game creator
        if (k <= 0) {
            throw new IllegalArgumentException("Invalid k value");
        }

        this.gameChecker = new GameChecker(k);
    }

    public Player getPlayer(int number) {
        return (number == 1) ? player1 : player2;
    }

    public int play(Board board) throws EOFException {
        while (true) {
            int result1 = -2;
            while (result1 == -2) {
                result1 = move(board, player1, 1);
            }

            if (result1 != -1) {
                return result1;
            }

            int result2 = -2;
            while (result2 == -2) {
                result2 = move(board, player2, 2);
            }

            if (result2 != -1) {
                return result2;
            }
        }
    }

    private int move(final Board board, final Player player, final int no) throws EOFException {
        final Move move = player.move(board.getPosition(), turn);
        final Result result;

        // check move validity because we do not trust players
        if (board.getPosition().isValid(move)) {
            final Position newPosition = board.makeMove(move);
            result = gameChecker.evaluatePosition(board.getWidth(), board.getHeight(), newPosition, turn);

            if (result == Result.UNKNOWN && gameChecker.isCombinationAppeared(
                    board, newPosition, move, turn)) {
               return -2;
            };

            this.turn = (turn == Cell.X) ? Cell.O : Cell.X;
        } else {
            result = Result.LOSE;
        }


        log("Player " + no + " move: " + move);
        log("Position:\n" + board);

        return switch (result) {
            case WIN -> {
                log("Player " + no + " won");
                yield no;
            }
            case LOSE -> {
                log("Player " + no + " lose");
                yield 3 - no;
            }
            case DRAW -> {
                log("Draw");
                yield 0;
            }
            default -> -1;
        };
    }

    private void log(final String message) {
        if (log) {
            System.out.println(message);
        }
    }
}
