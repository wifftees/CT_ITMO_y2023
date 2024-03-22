package expression.exceptions;

import expression.BitwiseNot;
import expression.Const;
import expression.Element;
import expression.Variable;
import expression.parser.BaseParser;
import expression.parser.StringSource;

import java.util.List;

public class ExpressionParser extends BaseParser implements TripleParser, ListParser {
    private enum OperatorType {
        ADD, PRODUCT
    }

    private enum BracketsType {
        SQUARE, FIGURE, CURLY
    }

    private List<String> givenVariables = List.of();
    private boolean hasGivenVariables;


    @Override
    public Element parse(String expression) throws ExpressionException {
        initSource(expression);

        hasGivenVariables = false;

        return getParsedExpression();
    }

    @Override
    public Element parse(final String expression, final List<String> variables) throws ExpressionException {
        initSource(expression);

        for (var variable: variables) {
            if (!isValidVariable(variable)) {
                throw new ArgumentForbiddenValue(
                        String.format("Variable: %s is not valid", variable)
                );
            }
        }

        hasGivenVariables = true;
        givenVariables = variables;

        return getParsedExpression();
    }

    private void initSource(String expression) {
        source = new StringSource(expression);

        queue.clear();
        take();
    }

    private Element getParsedExpression() throws ExpressionException {
        Element wholeExpression = parseExpression(true);

        if (getCurrentChar() != BaseParser.END) {
            throw new ExpectationMismatch("End of file expected");
        }

        return wholeExpression;
    }

    private Element parseExpression(boolean wholeExp) throws ExpressionException {
        skipWhitespace();
        return parseOperator(wholeExp, OperatorType.ADD);
    }

    private boolean isValidVariable(String variable) {
        if (variable.isEmpty() || !Character.isJavaIdentifierStart(variable.charAt(0))) {
            return false;
        }
        for (int i = 1; i < variable.length(); i++) {
            if (!Character.isJavaIdentifierPart(variable.charAt(i))) {
                return false;
            }
        }

        return Character.isJavaIdentifierStart(variable.charAt(0));
    }

    private char[] createArrayFromChars(char... chars) {
        return chars;
    }

    private char[] getTakeValuesByOperationType(OperatorType operator) {
        return switch (operator) {
            case ADD -> createArrayFromChars('+', '-');
            case PRODUCT -> createArrayFromChars('*', '/');
        };
    }

    private Element parseOperator(boolean wholeExp, OperatorType operator) throws ExpressionException {
        Element leftPart = switch (operator) {
            case ADD -> parseOperator(wholeExp, OperatorType.PRODUCT);
            case PRODUCT -> parseLowestLevel();
        };

        if (!wholeExp) {
            return leftPart;
        }

        char[] takeValues = getTakeValuesByOperationType(operator);

        skipWhitespace();
        while (testAny(takeValues)) {
            char currentSymbol = take();
            leftPart = switch (operator) {
                case ADD -> {
                    if (currentSymbol == '+') {
                        yield new CheckedAdd(leftPart, parseOperator(true, OperatorType.PRODUCT));
                    } else {
                        yield new CheckedSubtract(leftPart, parseOperator(true, OperatorType.PRODUCT));
                    }
                }
                case PRODUCT -> {
                    if (currentSymbol == '*') {
                        yield new CheckedMultiply(leftPart, parseLowestLevel());
                    } else {
                        yield new CheckedDivide(leftPart, parseLowestLevel());
                    }
                }
            };
            skipWhitespace();
        }

        return leftPart;
    }

    private Element parseLowestLevel() throws ExpressionException {
        skipWhitespace();
        if (take('(')) {
            return parseBrackets(BracketsType.CURLY);
        }
        if (take('{')) {
            return parseBrackets(BracketsType.FIGURE);
        }
        if (take('[')) {
            return parseBrackets(BracketsType.SQUARE);
        }

        if (take("log2".toCharArray())) {
            return parseLog();
        }
        if (take("pow2".toCharArray())) {
            return parsePow();
        }

        if (take('-')) {
            return parseCheckedNegate();
        }
        if (take('~')) {
            return parseBitwiseNot();
        }

        if (testNumber()) {
            return parseConst(1);
        }

        Element variable = parseVariable();
        if (variable != null) {
            queue.clear();
            return variable;
        } else {
            throw new ExpectationMismatch("Unexpected character: " + take()); // :NOTE: more info
        }
    }

    private Element parseVariable() {
        if (hasGivenVariables) {
            for (int i = 0; i < givenVariables.size(); i++) {
                if (lookForward(givenVariables.get(i).toCharArray())) {
                    return new Variable(i, givenVariables.get(i));
                }
            }
        }

        if (testAny('x', 'y', 'z')) {
            return new Variable(Character.toString(take()));
        }

        return null;
    }

    private Element parseLog() throws ExpressionException {
        expectAny(" (".toCharArray());
        skipWhitespace();
        return new Log(parseExpression(false));
    }

    private Element parsePow() throws ExpressionException {
        expectAny(" (".toCharArray());
        skipWhitespace();
        return new Pow(parseExpression(false));
    }

    private Element parseConst(final int sign) throws ParsingOverflow {
        StringBuilder number = new StringBuilder((sign > 0) ? "" : "-");

        while (testNumber()) {
            number.append(take());
        }

        try {
            return new Const(Integer.parseInt(number.toString()));
        } catch (NumberFormatException e) {
            throw new ParsingOverflow("Parsed number exceed integer boundaries", e);
        }
    }

    private Element parseBrackets(final BracketsType type) throws ExpressionException {
        Element expression = parseExpression(true);
        skipWhitespace();

        char expectedBracket = switch (type) {
            case CURLY -> ')';
            case SQUARE -> ']';
            case FIGURE -> '}';
        };

        expect(expectedBracket);
        return expression;
    }


    private Element parseBitwiseNot() throws ExpressionException {
        return new BitwiseNot(parseExpression(false));
    }

    private Element parseCheckedNegate() throws ExpressionException {
        if (testNumber()) {
            return parseConst(-1);
        } else {
            return new CheckedNegate(parseExpression(false));
        }
    }
}
