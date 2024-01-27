package expression.exceptions;

import expression.Add;
import expression.Element;

public class CheckedAdd extends Add {
    public CheckedAdd(Element a, Element b) {
        super(a, b);
    }

    @Override
    public int calc(int a, int b) {
        int result =  a + b;

        if (
                (b > 0 && Integer.MAX_VALUE - b < a) || (b <= 0 && Integer.MIN_VALUE - b > a)
        ) {
            throw new OverflowException("Addition result exceeds integer boundaries");
        }

        return result;
    }
}
