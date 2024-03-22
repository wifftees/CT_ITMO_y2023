package expression.generic.implementation;

import expression.generic.elements.ConfigGenerator;
import expression.generic.elements.ModeConfig;

import java.math.BigInteger;

public class BigIntegerConfigGenerator implements ConfigGenerator<BigInteger> {
    @Override
    public ModeConfig<BigInteger> getConfig() {
        return new ModeConfig<>(
                BigInteger::add,
                BigInteger::subtract,
                BigInteger::multiply,
                BigInteger::divide,
                BigInteger::negate,
                BigInteger::valueOf,
                BigInteger::intValue,
                a -> BigInteger.valueOf(a.bitCount()),
                BigInteger::min,
                BigInteger::max
        );
    }
}
