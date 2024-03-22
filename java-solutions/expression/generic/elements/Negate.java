package expression.generic.elements;

import java.util.function.Function;

public class Negate<T> extends UnaryOperator<T> {
    public Negate(final Element<T> element, final Function<T, T> calc) throws IllegalArgumentException {
        super(element, calc);
    }

    @Override
    public String getOperatorSymbol() {
        return "-";
    }
}
