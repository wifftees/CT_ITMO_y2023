public class GameChecker {
    private static final int k = 8;

    private boolean validatePieceStep(Move move, Position currentPosition) {
        int i = move.getPieceRow();
        int j = move.getPieceColumn();
        Cell opponent = (move.getValue() == Cell.W) ? Cell.B : Cell.W;

        int[][] validCoords = new int[4][2];
        validCoords[0] = new int[]{i + 1, j + 1};
        validCoords[1] = new int[]{i + 1, j - 1};
        validCoords[2] = new int[]{i - 1, j + 1};
        validCoords[3] = new int[]{i - 1, j - 1};

        for (int k = 0; k < 4; k++) {
            if (validCoords[k][0] == move.getDestRow() && validCoords[k][1] == move.getDestColumn()) {
                return true;
            }
        }

        int[][] validCoordsJump = new int[4][2];
        validCoordsJump[0] = new int[]{i + 2, j + 2};
        validCoordsJump[1] = new int[]{i + 2, j - 2};
        validCoordsJump[2] = new int[]{i - 2, j + 2};
        validCoordsJump[3] = new int[]{i - 2, j - 2};

        for (int k = 0; k < 4; k++) {
            if (validCoordsJump[k][0] == move.getDestRow() && validCoordsJump[k][1] == move.getDestColumn()
                    && currentPosition.getCell(validCoords[k][0], validCoords[k][1]) == opponent) {
                return true;
            }
        }

        return false;
    }

    public boolean isMoveValid(Move move, Position currentPosition) {
        // checking if user take piece from valid position
        if (!currentPosition.isValid(move.getPieceRow(), move.getPieceColumn())) {
            return false;
        }

        // checking if user takes his pieces
        if (!(currentPosition.getCell(move.getPieceRow(), move.getPieceColumn()) == move.getValue())) {
            return false;
        }

        // checking if destination cell is valid 
        if (!currentPosition.isValid(move.getDestRow(), move.getDestColumn()) && currentPosition.isCellEmpty(move.getDestRow(), move.getPieceColumn())) {
            return false;
        }

        if (!validatePieceStep(move, currentPosition)) {
            return false;
        }

        return true;
    }


    public Result evaluatePosition(Position currentPosition, Cell turn) {
        Cell opponent = (turn == Cell.W) ? Cell.B : Cell.W;

        long cnt = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (currentPosition.getCell(i, j) == opponent) {
                    cnt++;
                }
            }

        }

        return (cnt == 0) ? Result.WIN : Result.UNKNOWN;
    }


}
