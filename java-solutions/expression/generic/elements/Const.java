package expression.generic.elements;

import java.util.Objects;
import java.util.function.Function;

public class Const<T> implements Element<T> {
    private final T value;
    private final Function<T, Integer> backConverter;

    public Const(final Integer v, final Function<Integer, T> converter, final Function<T, Integer> backConverter) {
        this.value = converter.apply(v);
        this.backConverter = backConverter;
    }

    @Override
    public T evaluate(final int x, final int y, final int z) {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    @Override
    public String toString() {
        return Integer.toString(backConverter.apply(value));
    }
    @Override
    public boolean equals(final Object x) {
        return (this == x) || ((x instanceof Const) && (value == ((Const<?>) x).evaluate(0, 0, 0)));
    }
}
