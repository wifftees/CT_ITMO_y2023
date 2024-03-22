package expression.generic.implementation;

import expression.generic.elements.ConfigGenerator;
import expression.generic.elements.ModeConfig;

public class BooleanConfigGenerator implements ConfigGenerator<Boolean> {
    public int booleanToInt(final Boolean a) {
        return a ? 1 : 0;
    }

    public boolean intToBoolean(final Integer a) {
        return a != 0;
    }

    @Override
    public ModeConfig<Boolean> getConfig() {
        return new ModeConfig<>(
                Boolean::logicalOr,
                Boolean::logicalXor,
                Boolean::logicalAnd,
                (a, b) -> intToBoolean(booleanToInt(a) / booleanToInt(b)),
                a -> a,
                this::intToBoolean,
                this::booleanToInt,
                a -> a,
                Boolean::logicalAnd,
                Boolean::logicalOr
        );
    }
}
