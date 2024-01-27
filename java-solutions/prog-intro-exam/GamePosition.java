public class GamePosition implements Position {
    private final Board linkedBoard;

    public GamePosition(Board board) {
        linkedBoard = board;
    }

    @Override
    public boolean isValid(int r, int c) {
        int[] coords = {
                r, c
        };

        return linkedBoard.isValid(coords);
    }

    @Override
    public boolean isCellEmpty(int r, int c) {
        return getCell(r, c) == Cell.E;
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
