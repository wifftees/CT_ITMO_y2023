package expression.generic.elements;

import java.util.function.BiFunction;

public class Subtract<T> extends BinaryOperator<T> {
    public Subtract(
            final Element<T> element1, final Element<T> element2, final BiFunction<T, T, T> calc
    ) throws IllegalArgumentException {
        super(element1, element2, calc);
    }

    @Override
    public String getOperatorSymbol() {
        return "-";
    }
}
