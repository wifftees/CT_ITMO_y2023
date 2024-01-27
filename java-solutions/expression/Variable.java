package expression;

import java.util.Objects;

public class Variable implements Element {
    private final String symbol;

    public Variable(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Null values not supported");
        }
        symbol = s;
    }



    @Override
    public int evaluate(int x, int y, int z) {
        return switch (symbol) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new IllegalStateException("Unexpected value: " + symbol);
        };
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public boolean equals(Object x) {
        return (this == x) || ((x instanceof Variable) && (x.toString().equals(symbol)));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(symbol);
    }
}
