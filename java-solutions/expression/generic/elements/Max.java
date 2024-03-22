package expression.generic.elements;

import java.util.function.BiFunction;

public class Max<T> extends BinaryOperator<T> {
    public Max(Element<T> element1, Element<T> element2, BiFunction<T, T, T> calc) throws IllegalArgumentException {
        super(element1, element2, calc);
    }

    @Override
    public String getOperatorSymbol() {
        return "max";
    }
}