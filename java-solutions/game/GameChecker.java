package game;

import java.util.List;

public class GameChecker {
    private final int k;
    private static final int rowToAdd = 2;
    public GameChecker(int k) {
        this.k = k;
    }

    private Cell makeMargin(int rMargin, int cMargin, int[] start, Position position) {
        return position.getCell(start[0] + rMargin, start[1] + cMargin);
    }

    private int[] makeMargin(int rMargin, int cMargin, int[] start) {
        int[] result = new int[2];
        result[0] = start[0] + rMargin;
        result[1] = start[1] + cMargin;
        return result;
    }

    private boolean cellsEqual(Cell cell1, Cell cell2) {
        return cell1 == cell2;
    }

    private boolean checkDownLine(int height, int[] start, Position position, Cell turn, int numToWin) {
        int cnt = 0;
        for (int i = 0; i < numToWin && start[0] + i < height; i++)  {
            if (!cellsEqual(
                    makeMargin(i, 0, start, position), turn
            )) {
                return false;
            }

            cnt++;
        }

        return (cnt == numToWin);
    }

    private boolean checkRightLine(int width, int[] start, Position position, Cell turn, int numToWin) {
        int cnt = 0;
        for (int i = 0; i < numToWin && start[1] + i < width; i++) {
            if(!cellsEqual(
                    makeMargin(0, i, start, position), turn
            )) {
               return false;
            }

            cnt++;
        }

        return (cnt == numToWin);
    }

    private boolean checkDiagonalLine1(int width, int height, int[] start, Position position, Cell turn, int numToWin) {
        int cnt = 0;
        for (int i = 0; i < numToWin && i + start[0] < height && i + start[1] < width; i++) {
            if(!cellsEqual(
                    makeMargin(i, i, start, position), turn
            )) {
                return false;
            }

            cnt++;
        }

        return (cnt == numToWin);
    }

    private boolean checkDiagonalLine2(int width, int[] start, Position position, Cell turn, int numToWin) {
        int cnt = 0;
        for (int i = 0; i < k && start[0] - i >= 0 && i + start[1] < width; i++) {
            if(!cellsEqual(
                    makeMargin(-i, i, start, position), turn
            )) {
                return false;
            }

            cnt++;
        }

        return (cnt == numToWin);
    }

    public Result evaluatePosition(int boardWidth, int boardHeight, Position position, Cell turn) {
        int empty = 0;
        for (int i = 0; i < boardHeight; i++) {
           for (int j = 0; j < boardWidth; j++) {
               if (position.getCell(i, j) == Cell.E) {
                   empty++;
               }

               int[] currentPosition = {i, j};
               List<Boolean> comb = List.of(
                       checkDownLine(boardHeight, currentPosition, position, turn, k),
                       checkDiagonalLine1(boardWidth, boardHeight, currentPosition, position, turn, k),
                       checkDiagonalLine2(boardWidth, currentPosition, position, turn, k),
                       checkRightLine(boardWidth, currentPosition, position, turn, k)
               );

               if (comb.contains(true)) {
                   return Result.WIN;
               }
           }
        }

        if (empty == 0) {
            return Result.DRAW;
        }

        return Result.UNKNOWN;
    }

    public boolean isCombinationAppeared(Board board, Position position, Move lastMove, Cell turn) {
        int[] lastMoveCoords = {lastMove.getRow(), lastMove.getColumn()};
        for (int i = 0; i < rowToAdd; i++) {
            int[] startDown = makeMargin(-i, 0, lastMoveCoords);
            int[] startRight = makeMargin(0, -i, lastMoveCoords);
            int[] startDiag1 = makeMargin(-i, -i, lastMoveCoords);
            int[] startDiag2 = makeMargin(i, -i, lastMoveCoords);

            if (
                    (board.isValid(startDown) &&
                            checkDownLine(board.getHeight(), startDown, position, turn, rowToAdd)) ||
                    (board.isValid(startRight) &&
                            checkRightLine(board.getWidth(), startRight, position, turn, rowToAdd)) ||
                    (board.isValid(startDiag1) &&
                            checkDiagonalLine1(board.getWidth(), board.getHeight(), startDiag2, position, turn, rowToAdd)) ||
                    (board.isValid(startDiag2) &&
                                    checkDiagonalLine2(board.getWidth(), startDiag2, position, turn, rowToAdd))
            ) {
                return true;
            }
        }

        return false;
    }
}
