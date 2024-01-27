public final class Move {

    private final int pieceRow;
    private final int pieceColumn;
    private final int destRow;
    private final int destColumn;
    private final Cell value;

    public Move(
            final int pieceRow,
            final int pieceColumn,
            final int destRow,
            final int destColumn,
            final Cell value
    ) {
        this.pieceColumn = pieceColumn;
        this.pieceRow = pieceRow;
        this.destRow = destRow;
        this.destColumn = destColumn;
        this.value = value;
    }

    public int getPieceRow() {
        return pieceRow;
    }

    public int getPieceColumn() {
        return pieceColumn;
    }

    public int getDestRow() {
        return destRow;
    }

    public int getDestColumn() {
        return destColumn;
    }

    public Cell getValue() {
        return value;
    }
}
