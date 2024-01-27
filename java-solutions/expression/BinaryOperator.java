package expression;

import java.util.List;
import java.util.Objects;

public abstract class BinaryOperator implements Element {
    protected final List<Element> list;

    protected BinaryOperator(Element element1, Element element2) throws IllegalArgumentException {
        if (element1 == null || element2 == null) {
            throw new IllegalArgumentException("Null is not supported");
        }
        list = List.of(element1, element2);
    }

    public Element getLeftElement() {
        return list.get(0);
    }

    public Element getRightElement() {
        return list.get(1);
    }

    public abstract String getOperatorSymbol();

    public abstract int calc(int first, int second);

    @Override
    public int evaluate(int x) {
        return calc(list.get(0).evaluate(x), list.get(1).evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return calc(list.get(0).evaluate(x, y, z), list.get(1).evaluate(x, y, z));
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
    public boolean equals(Object x) {
        return (this == x) || (
                (x != null) && (this.getClass().equals(x.getClass()))
                        && (getLeftElement().equals(((BinaryOperator) x).getLeftElement()))
                        && (getRightElement().equals(((BinaryOperator) x).getRightElement()))
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(list, this.getClass());
    }
}
