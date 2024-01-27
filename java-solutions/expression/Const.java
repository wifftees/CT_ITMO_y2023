package expression;

import java.util.Objects;

public class Const implements Element {
    private final int value;

    public Const(int v) {
        value = v;
    }


    @Override
    public int evaluate(int x, int y, int z) {
        return value;
    }

    @Override
    public int evaluate(int x) {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public boolean equals(Object x) {
        return (this == x) || ((x instanceof Const) && (value == ((Const) x).evaluate(0)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
