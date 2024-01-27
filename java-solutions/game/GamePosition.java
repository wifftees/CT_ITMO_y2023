package game;

public class GamePosition implements Position {
    private final Board linkedBoard;

    public GamePosition(Board board) {
        linkedBoard = board;
    }

    @Override
    public boolean isValid(final Move move) {
        int[] coords = {move.getRow(), move.getColumn()};
        return linkedBoard.isValid(coords) && getCell(move.getRow(), move.getColumn()) == Cell.E;
    }

    @Override
    public Cell getCell(int r, int c) {
        return linkedBoard.getCell(r, c);
    }

    @Override
    public String getBoardView() {
        return linkedBoard.toString();
    }
}
