package expression.generic.elements;

import java.util.Objects;
import java.util.function.Function;

public abstract class UnaryOperator<T> extends Operator<T> {
    protected final Element<T> child;
    private final Function<T, T> calc;

    protected UnaryOperator(final Element<T> element, final Function<T, T> calc) throws IllegalArgumentException {
        if (element == null) {
            throw new IllegalArgumentException("Null is not supported");
        }
        this.child = element;
        this.calc = calc;
    }

    public Element<T> getElement() {
        return child;
    }


    @Override
    public T evaluate(int x, int y, int z) {
        return calc.apply(child.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return String.format(
                "%s(%s)",
                getOperatorSymbol(),
                child
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(child, this.getClass());
    }

    @Override
    public boolean equals(Object x) {
        return (x == this) || (
                (x != null) && (this.getClass().equals(x.getClass()))
                        && child.equals(((UnaryOperator<?>) x).getElement())
        );
    }
}
