package game;

import java.util.Arrays;
import java.util.Map;

/*
    Position is board with less extensive API. It restricts
    user possibilities therefore enhance code safety
 */
public class MNBoard implements Board {
    private static final Map<Cell, Character> SYMBOLS = Map.of(
            Cell.X, 'X',
            Cell.O, 'O',
            Cell.E, '.'
    );

    private final Cell[][] cells;
    // keep link to only one GamePosition to prevent from object multiplicity
    // de-facto GamePosition is proxy-class which is used to prevent Board methods abuse
    private final Position currentPosition;

    public MNBoard(int m, int n) throws IllegalArgumentException {
        // we do not trust game creators
        if (m <= 0 || n <= 0) {
            throw new IllegalArgumentException("Size parameters are not allowed");
        }

        this.cells = new Cell[m][n];

        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }

        currentPosition = new GamePosition(this);
    }

    @Override
    public int getWidth() {
        return cells[0].length;
    }

    @Override
    public int getHeight() {
        return cells.length;
    }

    @Override
    public Position getPosition() {
        return currentPosition;
    }

    @Override
    public Position makeMove(final Move move) {
        // check because we do not trust Game
        if (currentPosition.isValid(move)) {
            cells[move.getRow()][move.getColumn()] = move.getValue();
        }
        return currentPosition;
    }

    @Override
    public Cell getCell(final int r, final int c) {
        return cells[r][c];
    }

    @Override
    public boolean isValid(int[] coords) {
        return (coords[0] >= 0 && coords[0] < getHeight() && coords[1] >= 0 && coords[1] < getWidth());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(" ");
        for (int i = 0; i < getWidth(); i++) {
            sb.append(i);
        }
        for (int r = 0; r < getHeight(); r++) {
            sb.append("\n");
            sb.append(r);
            for (int c = 0; c < getWidth(); c++) {
                sb.append(SYMBOLS.get(cells[r][c]));
            }
        }
        return sb.toString();
    }
}
