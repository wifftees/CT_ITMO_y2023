package expression.exceptions;

import expression.Element;
import expression.Multiply;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(Element a, Element b) {
        super(a, b);
    }

    private boolean detectOverflow(int a, int b) {
        if (a == 0 || b == 0) {
            return false;
        }

        if (a < 0) {
            if (b < 0) {
                return b < Integer.MAX_VALUE / a;
            }

            return a < Integer.MIN_VALUE / b;
        }

        if (b < 0) {
            return b < Integer.MIN_VALUE / a;
        }

        return a > Integer.MAX_VALUE / b;
    }

    @Override
    public int calc(int a, int b) {
        if (detectOverflow(a, b))  {
            throw new OverflowException("Multiplication result exceeds integer boundaries");
        }

        return a * b;
    }
}
