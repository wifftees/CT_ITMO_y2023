package expression.generic.implementation;

import expression.generic.elements.ConfigGenerator;
import expression.generic.elements.ModeConfig;

public class DoubleConfigGenerator implements ConfigGenerator<Double> {
    @Override
    public ModeConfig<Double> getConfig() {
        return new ModeConfig<>(
                Double::sum,
                (a, b) -> a - b,
                (a, b) -> a * b,
                (a, b) -> a / b,
                a -> -a,
                Double::valueOf,
                Double::intValue,
                a -> (double) Long.bitCount(Double.doubleToLongBits(a)),
                Double::min,
                Double::max
        );
    }
}
