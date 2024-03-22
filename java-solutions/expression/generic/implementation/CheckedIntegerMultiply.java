package expression.generic.implementation;

import expression.exceptions.OverflowException;

import java.util.function.BiFunction;

public class CheckedIntegerMultiply implements BiFunction<Integer, Integer, Integer> {
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
    public Integer apply(Integer a, Integer b) {
        if (detectOverflow(a, b))  {
            throw new OverflowException("Multiplication result exceeds integer boundaries");
        }

        return a * b;
    }
}
