package expression.generic;

import expression.exceptions.EvaluationException;
import expression.exceptions.ExpressionException;
import expression.generic.elements.Element;
import expression.exceptions.ArgumentForbiddenValue;
import expression.generic.elements.ModeConfig;
import expression.generic.implementation.BigIntegerConfigGenerator;
import expression.generic.elements.ConfigGenerator;
import expression.generic.implementation.BooleanConfigGenerator;
import expression.generic.implementation.ByteConfigGenerator;
import expression.generic.implementation.DoubleConfigGenerator;
import expression.generic.implementation.CheckedIntegerConfigGenerator;
import expression.generic.implementation.IntegerConfigGenerator;

import java.util.Map;
import static java.util.Map.entry;
public class GenericTabulator implements Tabulator {
    private int x1, x2, y1, y2, z1, z2;
    public enum ModeType {
        CHECKED_INTEGER, DOUBLE, BIG_INTEGER, INTEGER, BYTE, BOOLEAN
    }
    public static Map<String, ModeType> keyToMode = Map.ofEntries(
            entry("i", ModeType.CHECKED_INTEGER),
            entry("d", ModeType.DOUBLE),
            entry("bi", ModeType.BIG_INTEGER),
            entry("u", ModeType.INTEGER),
            entry("b", ModeType.BYTE),
            entry("bool", ModeType.BOOLEAN)
    );

    private ModeType extractModeType(final String input) {
        if (!keyToMode.containsKey(input)) {
            throw new ArgumentForbiddenValue("Unknown key for data type");
        }

        return keyToMode.get(input);
    }

    private <T> ExpressionParser<T> createParser(final ConfigGenerator<T> generator) {
        return new ExpressionParser<>(generator);
    }

    private <T> void fillTable(
            final Object[][][] table, final ConfigGenerator<T> generator, final String expr
    ) throws ExpressionException {
        Element<T> parsed = createParser(generator).parse(expr);
        for (int i = 0; i < x2 - x1 + 1; i++) {
            for (int j = 0; j < y2 - y1 + 1; j++) {
                for (int k = 0; k < z2 - z1 + 1; k++) {
                    try {
                        table[i][j][k] = parsed.evaluate(i + x1, j + y1, k + z1);
                    } catch (EvaluationException | ArithmeticException e) {
                        table[i][j][k] = null;
                    }
                }
            }
        }
    }

    private void updateBoundaries(
            final int x1, final int x2, final int y1, final int y2, final int z1, final int z2
    ) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.z1 = z1;
        this.z2 = z2;
    }

    @Override
    public Object[][][] tabulate(
            final String mode, final String expression, final int x1, final int x2, final int y1, final int y2, final int z1, final int z2
    ) throws ExpressionException {
        Object[][][] table = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        updateBoundaries(x1, x2, y1, y2, z1, z2);
        ModeType modeType = extractModeType(mode);
        switch (modeType) {
            case DOUBLE ->
                fillTable(table, new DoubleConfigGenerator(), expression);
            case CHECKED_INTEGER ->
                fillTable(table, new CheckedIntegerConfigGenerator(), expression);
            case BIG_INTEGER ->
                fillTable(table, new BigIntegerConfigGenerator(), expression);
            case INTEGER ->
                fillTable(table, new IntegerConfigGenerator(), expression);
            case BYTE ->
                fillTable(table, new ByteConfigGenerator(), expression);
            case BOOLEAN ->
                fillTable(table, new BooleanConfigGenerator(), expression);
        }

        return table;
    }
}
