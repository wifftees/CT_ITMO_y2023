
package expression;

import java.util.List;
import java.util.Objects;

public abstract class UnaryOperator implements Element {
    protected final Element child;

    protected UnaryOperator(Element element) throws IllegalArgumentException {
        if (element == null) {
            throw new IllegalArgumentException("Null is not supported");
        }
        child = element;
    }

    public Element getElement() {
        return child;
    }

    public abstract String getOperatorSymbol();

    public abstract int calc(int element);

    @Override
    public int evaluate(int x) {
        return calc(child.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return calc(child.evaluate(x, y, z));
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return calc(child.evaluate(variables));
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
    public boolean equals(Object x) {
        return (x == this) || (
                (x != null) && (this.getClass().equals(x.getClass()))
                        && child.equals(((UnaryOperator) x).getElement())
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(child, this.getClass());
    }
}
