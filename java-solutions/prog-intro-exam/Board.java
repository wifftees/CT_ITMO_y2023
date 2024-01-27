import java.util.Arrays;
import java.util.Map;

/*
    Position is board with less extensive API. It restricts
    user possibilities therefore enhance code safety
 */
public class Board {
    private static final Map<Cell, Character> SYMBOLS = Map.of(
            Cell.W, 'W',
            Cell.B, 'B',
            Cell.E, '.'
    );

    private final Cell[][] cells;
    // keep link to only one GamePosition to prevent from object multiplicity
    // de-facto GamePosition is proxy-class which is used to prevent Board methods abuse
    private final Position currentPosition;

    public Board() throws IllegalArgumentException {
        this.cells = new Cell[8][8];

        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }


        int k = 1;
        for (int i = 0; i < 3; i++) {
            for (int j = k; j < 8; j += 2) {
                cells[i][j] = Cell.B;
            }

            k = (k == 1) ? 0 : 1;
        }


        for (int i = 5; i < 8; i++) {
            for (int j = k; j < 8; j += 2) {
                cells[i][j] = Cell.W;
            }

            k = (k == 1) ? 0 : 1;
        }

        currentPosition = new GamePosition(this);
    }

    public int getWidth() {
        return cells[0].length;
    }

    public int getHeight() {
        return cells.length;
    }

    public Position getPosition() {
        return currentPosition;
    }

    public Position makeMove(final Move move) {
        cells[move.getDestRow()][move.getDestColumn()] = move.getValue();
        cells[move.getPieceRow()][move.getPieceColumn()] = Cell.E;
        return currentPosition;
    }

    public void updateCell(int r, int c, Cell value) {
        int[] coords = new int[]{r, c};

        if (isValid(coords)) {
            cells[r][c] = value;
        }
    }

    public Cell getCell(final int r, final int c) {
        return cells[r][c];
    }

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
