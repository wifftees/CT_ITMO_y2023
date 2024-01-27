package expression.exceptions;

import expression.Element;
import expression.Subtract;

public class CheckedSubtract extends Subtract {
    public CheckedSubtract(Element a, Element b) {
        super(a, b);
    }

    @Override
    public int calc(int a, int b) {
        if (
                (a >= 0 && b <= 0 && a > Integer.MAX_VALUE + b) || (a <= 0 && b >= 0 && a < Integer.MIN_VALUE + b)
        ) {
            throw new OverflowException("Subtraction result exceeds integer boundaries");
        }

        return a - b;
    }
}
