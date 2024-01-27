import java.io.EOFException;

public class Game {
    private final boolean log;
    private final Player player1, player2;
    private final GameChecker gameChecker;
    // Game should manage players switching, so it should know whose turn is now.
    private Cell turn;

    public Game(final boolean log, final Player player1, final Player player2) {
        this.log = log;
        this.player1 = player1;
        this.player2 = player2;
        this.turn = Cell.W;
        this.gameChecker = new GameChecker();
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
        // :NOTE: * защита как в дз
        final Move move = player.move(board.getPosition(), turn);
        Result result;
        Position newPosition;

        if (gameChecker.isMoveValid(move, board.getPosition())) {
            updateIfJump(move, board);
            newPosition = board.makeMove(move);
            result = gameChecker.evaluatePosition(newPosition, turn);
            this.turn = (turn == Cell.W) ? Cell.B : Cell.W;
        } else {
            result = Result.LOSE;
        }

        // check move validity because we do not trust players

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
            default -> -1;
        };
    }

    private void log(final String message) {
        if (log) {
            System.out.println(message);
        }
    }


    private void updateIfJump(Move move, Board board) {
        int i = move.getPieceRow();
        int j = move.getPieceColumn();

        int[][] validCoords = new int[4][2];
        validCoords[0] = new int[]{i + 1, j + 1};
        validCoords[1] = new int[]{i + 1, j - 1};
        validCoords[2] = new int[]{i - 1, j + 1};
        validCoords[3] = new int[]{i - 1, j - 1};

        int[][] validCoordsJump = new int[4][2];
        validCoordsJump[0] = new int[]{i + 2, j + 2};
        validCoordsJump[1] = new int[]{i + 2, j - 2};
        validCoordsJump[2] = new int[]{i - 2, j + 2};
        validCoordsJump[3] = new int[]{i - 2, j - 2};

        for (int k = 0; k < 4; k++) {
            if (
                    validCoordsJump[k][0] == move.getDestRow()
                            && validCoordsJump[k][1] == move.getDestColumn()
            ) {
                board.updateCell(validCoords[k][0], validCoords[k][1], Cell.E);
            }
        }

    }

}
