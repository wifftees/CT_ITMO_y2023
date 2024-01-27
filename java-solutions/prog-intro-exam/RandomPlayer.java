import java.util.Random;

public class RandomPlayer implements Player {
    private final Random random;

    public RandomPlayer(final Random random) {
        this.random = random;
    }

    public RandomPlayer() {
        this(new Random());
    }

    public Move move(final Position position, final Cell turn) {
        while (true) {
            int pieceR = random.nextInt(8);
            int pieceC = random.nextInt(8);
            int destR = random.nextInt(8);
            int destC = random.nextInt(8);
            final Move move = new Move(pieceR, pieceC, destR, destC, turn);
            // :NOTE: - хоть чуток разумные ходы...
            if (position.isValid(move.getPieceRow(), move.getPieceColumn()) && position.isValid(move.getDestRow(), move.getDestColumn())) {
                return move;
            }
//            return move;
        }
    }
}
