package expression.exceptions;

import expression.Element;
import expression.Multiply;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(Element a, Element b) {
        super(a, b);
    }

    @Override
    public int calc(int a, int b) {
        if (a != 0 && b != 0 && ((a * b) / b != a || (a * b) / a != b))  {
            throw new OverflowException("Multiplication result exceeds integer boundaries");
        }

        return a * b;
    }
}
