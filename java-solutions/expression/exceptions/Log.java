package expression.exceptions;

import expression.Element;
import expression.UnaryOperator;

public class Log extends UnaryOperator { // :NOTE: naming
    public Log(Element e) {
        super(e);
    }
    @Override
    public int calc(int a) {
        if (a < 0) {
            throw new ArgumentForbiddenValue("Logarithm argument is negative");
        } else if (a == 0) {
            throw new ArgumentForbiddenValue("Logarithm argument is zero");
        }

        int cnt = 0;
        while (a >= 2) {
            a /= 2;
            cnt++;
        }
        return cnt;
    }

    @Override
    public String getOperatorSymbol() {
        return "log2";
    }

}
