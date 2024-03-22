package expression.generic;

import expression.exceptions.ExpectationMismatch;
import expression.exceptions.ExpressionException;
import expression.exceptions.ParsingOverflow;
import expression.generic.elements.Add;
import expression.generic.elements.ConfigGenerator;
import expression.generic.elements.Const;
import expression.generic.elements.Count;
import expression.generic.elements.Divide;
import expression.generic.elements.Element;
import expression.generic.elements.Max;
import expression.generic.elements.Min;
import expression.generic.elements.ModeConfig;
import expression.generic.elements.Multiply;
import expression.generic.elements.Negate;
import expression.generic.elements.Subtract;
import expression.generic.elements.Variable;
import expression.parser.StringSource;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class ExpressionParser<T> extends BaseParser {
    private enum OperatorType {
        ADD, PRODUCT, COMPARE
    }
    private static final List<String> BRACKETS = List.of("[]", "{}", "()");
    private static final String[] DEFAULT_VARIABLES = {"x", "y", "z"};
    private static final Map<OperatorType, List<String>> takeValues = Map.ofEntries(
            entry(OperatorType.ADD, List.of("+", "-")),
            entry(OperatorType.PRODUCT, List.of("*", "/")),
            entry(OperatorType.COMPARE, List.of("min", "max"))
    );
    private final ModeConfig<T> config;

    public ExpressionParser(final ConfigGenerator<T> generator) {
        this.config = generator.getConfig();
    }

    public Element<T> parse(final String expression) throws ExpressionException {
        initSource(expression);

        return getParsedExpression();
    }

    private void initSource(final String expression) {
        source = new StringSource(expression);

        queue.clear();
    }

    private Element<T> getParsedExpression() throws ExpressionException {
        Element<T> wholeExpression = parseExpression(true);

        if (getCurrentChar() != BaseParser.END) {
            throw new ExpectationMismatch("End of file expected");
        }

        return wholeExpression;
    }

    private Element<T> parseExpression(final boolean wholeExp) throws ExpectationMismatch, ParsingOverflow {
        skipWhitespace();
        return parseOperator(wholeExp, OperatorType.ADD);
    }

    private int findNextTakeValue(final List<String> takeValues) {
        for (int i = 0; i < takeValues.size(); i++) {
            if (lookForward(takeValues.get(i))) {
                return i;
            }
        }

        return -1;
    }

    private List<String> getTakeValuesByOperationType(final OperatorType operator) {
        return takeValues.get(operator);
    }

    private Element<T> getElementBySwitch(final Element<T> leftPart, final String currentOperatorValue, final OperatorType operator) throws ExpectationMismatch, ParsingOverflow {
        return switch (operator) {
            case ADD -> {
                if (currentOperatorValue.equals("+")) {
                    yield new Add<>(
                            leftPart, parseOperator(true, OperatorType.PRODUCT), config.add()
                    );
                } else {
                    yield new Subtract<>(
                            leftPart, parseOperator(true, OperatorType.PRODUCT), config.subtract()
                    );
                }
            }
            case PRODUCT -> {
                if (currentOperatorValue.equals("*")) {
                    yield new Multiply<>(leftPart, parseOperator(true, OperatorType.COMPARE), config.multiply());
                } else {
                    yield new Divide<>(leftPart, parseOperator(true, OperatorType.COMPARE), config.divide());
                }
            }
            case COMPARE -> {
                if (currentOperatorValue.equals("min")) {
                    yield new Min<>(leftPart, parseLowestLevel(), config.min());
                } else {
                    yield new Max<>(leftPart, parseLowestLevel(), config.max());
                }
            }
        };
    }

    private Element<T> parseOperator(final boolean wholeExp, final OperatorType operator) throws ExpectationMismatch, ParsingOverflow {
        Element<T> leftPart = switch (operator) {
            case ADD -> parseOperator(wholeExp, OperatorType.PRODUCT);
            case PRODUCT -> parseOperator(wholeExp, OperatorType.COMPARE);
            case COMPARE -> parseLowestLevel();
        };

        if (!wholeExp) {
            return leftPart;
        }

        List<String> takeValues = getTakeValuesByOperationType(operator);

        skipWhitespace();
        int indexOfTakeValue = findNextTakeValue(takeValues);
        while (indexOfTakeValue != -1) {
            take(takeValues.get(indexOfTakeValue));
            String currentOperatorValue = takeValues.get(indexOfTakeValue);
            leftPart = getElementBySwitch(leftPart, currentOperatorValue, operator);
            skipWhitespace();
            indexOfTakeValue = findNextTakeValue(takeValues);
        }

        return leftPart;
    }

    private Element<T> parseLowestLevel() throws ExpectationMismatch, ParsingOverflow {
        skipWhitespace();

        for (String br: BRACKETS) {
            if (take(br.charAt(0))) {
                return parseBrackets(br);
            }
        }

        if (take("-")) {
            return parseNegate();
        }

        if (take("count")) {
            return parseCount();
        }

        if (checkDigit()) {
            return parseConst(1);
        }

        Element<T> variable = parseVariable();
        if (variable != null) {
            return variable;
        } else {
            throw error("Unexpected character: " + take());
        }
    }

    private Element<T> parseCount() throws ExpectationMismatch, ParsingOverflow {
        return new Count<>(parseExpression(false), config.count());
    }

    private Element<T> parseNegate() throws ParsingOverflow, ExpectationMismatch {
        if (checkDigit()) {
            return parseConst(-1);
        } else {
            return new Negate<>(parseExpression(false), config.negate());
        }
    }

    private Element<T> parseVariable() {
        if (lookForwardAny(DEFAULT_VARIABLES)) {
            return new Variable<>(Character.toString(take()), config.converter());
        }

        return null;
    }

    private Element<T> parseConst(final int sign) throws ParsingOverflow {
        StringBuilder number = new StringBuilder((sign > 0) ? "" : "-");

        while (checkDigit()) {
            number.append(take());
        }

        try {
            return new Const<>(Integer.parseInt(number.toString()), config.converter(), config.backConverter());
        } catch (NumberFormatException e) {
            throw new ParsingOverflow("Parsed number exceed integer boundaries", e);
        }
    }

    private Element<T> parseBrackets(final String brackets) throws ExpectationMismatch, ParsingOverflow {
        Element<T> expression = parseExpression(true);
        skipWhitespace();

        char expectedBracket = brackets.charAt(1);

        expect(expectedBracket);
        return expression;
    }

    private boolean checkDigit() {
        return (Character.isDigit(lookForward()));
    }
}