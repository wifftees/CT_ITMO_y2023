public interface Position {
    boolean isValid(int r, int c);

    boolean isCellEmpty(int r, int c);

    Cell getCell(int r, int c);

    String getBoardView();
}
