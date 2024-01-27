package expression.exceptions;

import expression.Divide;
import expression.Element;

public class CheckedDivide extends Divide {
    public CheckedDivide(Element a, Element b) {
        super(a, b);
    }

    @Override
    public int calc(int a, int b) {
        if (b == 0) {
            throw new ZeroDenominator("Denominator of arithmetic expression can't be zero");
        } else if (a == Integer.MIN_VALUE && b == -1) {
            throw new OverflowException("Division result exceeds integer boundaries");
        }

        return a / b;
    }
}
