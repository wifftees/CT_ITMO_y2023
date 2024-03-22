package expression.generic.implementation;

import expression.generic.elements.ConfigGenerator;
import expression.generic.elements.ModeConfig;

public class ByteConfigGenerator implements ConfigGenerator<Byte> {
    @Override
    public ModeConfig<Byte> getConfig() {
        return new ModeConfig<>(
                (a, b) -> (byte) (a + b),
                (a, b) -> (byte) (a - b),
                (a, b) -> (byte) (a * b),
                (a, b) -> (byte) (a / b),
                a -> (byte) -a,
                Integer::byteValue,
                Byte::intValue,
                a -> (byte) Integer.bitCount(Byte.toUnsignedInt(a)),
                (a, b) -> a.compareTo(b) < 0 ? a : b,
                (a, b) -> a.compareTo(b) > 0 ? a : b
        );
    }
}
