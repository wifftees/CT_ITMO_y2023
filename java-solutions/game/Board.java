package game;

public interface Board {
    int getWidth();
    int getHeight();
    Position getPosition();
    Cell getCell(int r, int c);
    Position makeMove(Move move);
    boolean isValid(int[] coords);
}
