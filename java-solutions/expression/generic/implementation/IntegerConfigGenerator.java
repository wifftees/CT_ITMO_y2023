package expression.generic.implementation;

import expression.generic.elements.ConfigGenerator;
import expression.generic.elements.ModeConfig;

public class IntegerConfigGenerator implements ConfigGenerator<Integer> {
    @Override
    public ModeConfig<Integer> getConfig() {
        return new ModeConfig<>(
                Integer::sum,
                (a, b) -> a - b,
                (a, b) -> a * b,
                (a, b) -> a / b,
                a -> -a,
                Integer::valueOf,
                Integer::valueOf,
                Integer::bitCount,
                Integer::min,
                Integer::max
        );
    }
}
