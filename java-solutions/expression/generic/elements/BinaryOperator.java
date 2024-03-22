package expression.generic.elements;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public abstract class BinaryOperator<T> extends Operator<T> {
    protected final List<Element<T>> list;
    private final BiFunction<T, T, T> calc;

    protected BinaryOperator(
            final Element<T> element1, final Element<T> element2, final BiFunction<T, T, T> calc
    ) throws IllegalArgumentException {
        if (element1 == null || element2 == null) {
            throw new IllegalArgumentException("Null value is not supported");
        }
        this.list = List.of(element1, element2);
        this.calc = calc;
    }

    private Element<T> getLeftElement() {
        return list.get(0);
    }

    private Element<T> getRightElement() {
        return list.get(1);
    }

    @Override
    public T evaluate(int x, int y, int z) {
        return calc.apply(list.get(0).evaluate(x, y, z), list.get(1).evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return String.format(
                "(%s %s %s)",
                list.get(0).toString(),
                getOperatorSymbol(),
                list.get(1).toString()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(list, this.getClass());
    }

    @Override
    public boolean equals(Object x) {
        return (this == x) || (
                (x != null) && (this.getClass().equals(x.getClass()))
                        && (getLeftElement().equals(((BinaryOperator<?>) x).getLeftElement()))
                        && (getRightElement().equals(((BinaryOperator<?>) x).getRightElement()))
        );
    }
}