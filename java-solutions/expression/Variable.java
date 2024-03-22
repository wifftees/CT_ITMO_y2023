package expression;

import java.util.List;
import java.util.Objects;

public class Variable implements Element {
    private String symbol = "default";
    private int index;

    public Variable(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Null values not supported");
        }
        symbol = s;
    }

    public Variable(int i) {
        index = i;
    }

    public Variable(int i, String s) {
        this(i);
        symbol = s;
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return variables.get(index);
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
