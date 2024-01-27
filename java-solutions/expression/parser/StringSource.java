package expression.parser;

public class StringSource implements CharSource {
    private final String data;
    private int index = 0;

    public StringSource(String input) {
        data = input;
    }

    @Override
    public boolean hasNext() {
        return index < data.length();
    }

    @Override
    public char next() {
        return data.charAt(index++);
    }

    @Override
    public IllegalArgumentException error(String message) {
        return new IllegalArgumentException(
                String.format("Current index: %d, %s", index, message)
        );
    }
}
