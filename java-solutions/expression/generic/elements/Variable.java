package expression.generic.elements;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class Variable<T> implements Element<T>{
    private final String symbol;
    private final Function<Integer, T> converter;


    public Variable(final String s, final Function<Integer, T> converter) {
        if (s == null) {
            throw new IllegalArgumentException("Null values not supported");
        }
        this.symbol = s;
        this.converter = converter;

    }

    @Override
    public T evaluate(final int x, final int y, final int z) {
        return switch (symbol) {
            case "x" -> converter.apply(x);
            case "y" -> converter.apply(y);
            case "z" -> converter.apply(z);
            default -> throw new IllegalStateException("Unexpected value: " + symbol);
        };
    }

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(symbol);
    }

    @Override
    public boolean equals(final Object x) {
        return (this == x) || ((x instanceof Variable<?>) && (x.toString().equals(symbol)));
    }
}
