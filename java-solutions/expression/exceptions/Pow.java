package expression.exceptions;

import expression.Element;
import expression.UnaryOperator;

public class Pow extends UnaryOperator {
    public Pow(Element element) {
        super(element);
    }

    @Override
    public int calc(int a) {
        if (a < 0) {
            throw new ArgumentForbiddenValue("Only integer calculations are allowed");
        }

        if (a >= 31) {
            throw new OverflowException("Exponentiation result exceeds integer boundaries");
        }

        int res = 1;
        for (int i = 0; i < a; i++) {
            res *= 2;
        }
        return res;
    }

    @Override
    public String getOperatorSymbol() {
        return "pow2";
    }
}
