package expression.exceptions;

import expression.Element;
import expression.Sign;

public class CheckedNegate extends Sign {
    public CheckedNegate(Element a) {
        super(a);
    }

    @Override
    public int calc(int a) {
        if (a == Integer.MIN_VALUE) {
            throw new OverflowException("Negative value of the variable exceeds integer boundaries");
        }
        return -a;
    }
}
