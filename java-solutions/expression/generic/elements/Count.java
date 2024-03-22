package expression.generic.elements;

import java.util.function.Function;

public class Count<T> extends UnaryOperator<T> {

    public Count(Element<T> element, Function<T, T> calc) throws IllegalArgumentException {
        super(element, calc);
    }

    @Override
    public String getOperatorSymbol() {
        return "count";
    }
}
