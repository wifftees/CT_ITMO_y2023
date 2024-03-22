package expression.generic.implementation;

import expression.exceptions.OverflowException;
import expression.exceptions.ZeroDenominator;
import expression.generic.elements.ConfigGenerator;
import expression.generic.elements.ModeConfig;

public class CheckedIntegerConfigGenerator implements ConfigGenerator<Integer> {
    @Override
    public ModeConfig<Integer> getConfig() {
        return new ModeConfig<>(
                (a, b) -> {
                    if (
                            (b > 0 && a > Integer.MAX_VALUE - b) || (b < 0 && a < Integer.MIN_VALUE - b)
                    ) {
                        throw new OverflowException("Addition result exceeds integer boundaries");
                    }

                    return a + b;
                },
                (a, b) -> {
                    if (
                            (a >= 0 && b <= 0 && a > Integer.MAX_VALUE + b) || (a <= 0 && b >= 0 && a < Integer.MIN_VALUE + b)
                    ) {
                        throw new OverflowException("Subtraction result exceeds integer boundaries");
                    }

                    return a - b;
                },
                new CheckedIntegerMultiply(),
                (a, b) -> {
                    if (b == 0) {
                        throw new ZeroDenominator("Denominator of arithmetic expression can't be zero");
                    } else if (a == Integer.MIN_VALUE && b == -1) {
                        throw new OverflowException("Division result exceeds integer boundaries");
                    }

                    return a / b;
                },
                a -> {
                    if (a == Integer.MIN_VALUE) {
                        throw new OverflowException("Negative value of the variable exceeds integer boundaries");
                    }
                    return -a;
                },
                Integer::valueOf,
                Integer::valueOf,
                Integer::bitCount,
                Integer::min,
                Integer::max
        );
    }
}
