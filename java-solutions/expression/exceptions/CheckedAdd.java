package expression.exceptions;

import expression.Add;
import expression.Element;

public class CheckedAdd extends Add {
    public CheckedAdd(Element a, Element b) {
        super(a, b);
    }

    @Override
    public int calc(int a, int b) throws EvaluationException {
        if (
                (b > 0 && a > Integer.MAX_VALUE - b) || (b < 0 && a < Integer.MIN_VALUE - b)
        ) {
            throw new OverflowException("Addition result exceeds integer boundaries");
        }

        return a + b;
    }
}
