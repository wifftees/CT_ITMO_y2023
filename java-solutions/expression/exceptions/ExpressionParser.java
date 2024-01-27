package expression.exceptions;
import expression.*;
import expression.parser.BaseParser;
import expression.parser.StringSource;
import expression.parser.TripleParser;

import java.util.StringJoiner;

public class ExpressionParser extends BaseParser implements TripleParser {
    @Override
    public Element parse(String expression) {
        source = new StringSource(expression);

        take();

        // System.out.println(expression);

        Element wholeExpression = parseExpression(true);

        if (getCurrentChar() != BaseParser.END) {
            throw new ExpectionMismatch("End of file expected");
        }

        return wholeExpression;
    }

    private enum BitwiseOperatorType {
        OR, XOR, AND
    }

    private enum BinaryOperatorType {
        ADD, PRODUCT;
    }


    private Element parseBitwiseOperator(boolean wholeExp, BitwiseOperatorType operator) {
        Element leftPart = switch (operator) {
            case OR -> parseBitwiseOperator(wholeExp, BitwiseOperatorType.XOR);
            case XOR -> parseBitwiseOperator(wholeExp, BitwiseOperatorType.AND);
            case AND -> parseBinaryOperator(wholeExp, BinaryOperatorType.ADD);
        };

        if (!wholeExp) {
            return leftPart;
        }

        char takeValue = switch (operator) {
            case OR -> '|';
            case XOR -> '^';
            case AND -> '&';
        };

        skipWhitespace();
        while (take(takeValue)) {
            skipWhitespace();
            leftPart = switch (operator) {
                case OR -> new BitwiseOr(leftPart, parseBitwiseOperator(true, BitwiseOperatorType.XOR));
                case XOR -> new BitwiseXOR(leftPart, parseBitwiseOperator(true, BitwiseOperatorType.AND));
                case AND -> new BitwiseAnd(leftPart, parseBinaryOperator(true, BinaryOperatorType.ADD));
            };
            skipWhitespace();
        }

        return leftPart;

    }

    private Element parseExpression(boolean wholeExp) {
        skipWhitespace();
        return parseBitwiseOperator(wholeExp, BitwiseOperatorType.OR);
    }

    private Element parseBinaryOperator(boolean wholeExp, BinaryOperatorType operator) {
        Element leftPart = switch (operator) {
            case ADD -> parseBinaryOperator(wholeExp, BinaryOperatorType.PRODUCT);
            case PRODUCT -> parseLowestLevel();
        };

        if (!wholeExp) {
            return leftPart;
        }

        char[] takeValues = switch (operator) {
            case ADD -> new char[]{'+', '-'};
            case PRODUCT -> new char[]{'*', '/'};
        };

        skipWhitespace();
        while (test(takeValues[0]) || test(takeValues[1])) {
            if (take(takeValues[0])) {
                leftPart = switch (operator) {
                    case ADD -> new CheckedAdd(leftPart, parseBinaryOperator(true, BinaryOperatorType.PRODUCT));
                    case PRODUCT -> new CheckedMultiply(leftPart, parseLowestLevel());
                };
            } else if (take(takeValues[1])) {
                leftPart = switch (operator) {
                    case ADD -> new CheckedSubtract(leftPart, parseBinaryOperator(true, BinaryOperatorType.PRODUCT));
                    case PRODUCT -> new CheckedDivide(leftPart, parseLowestLevel());
                };
            }
            skipWhitespace();
        }

        return leftPart;
    }

    private Element parseLowestLevel() {
        skipWhitespace();
        if (take('(')) {
            return parseBrackets();
        } else if (take("log2".toCharArray())) {
            return parseLog();
        } else if (take("pow2".toCharArray())) {
            return parsePow();
        } else if (take('-')) {
            return parseCheckedNegate();
        } else if (testAny("xyz".toCharArray())) {
            return parseVariable();
        } else if (testNumber()) {
            return parseConst(1);
        } else if (take('~')) {
            return parseBitwiseNot();
        } else {
            throw new ExpectionMismatch("Unexpected character: " + take());
        }
    }

    private Element parseVariable() {
        return new Variable(Character.toString(take()));
    }

    private Element parseLog() {
        expectAny(" (".toCharArray());
        skipWhitespace();
        return new Log(parseExpression(false));
    }

    private Element parsePow() {
        expectAny(" (".toCharArray());
        skipWhitespace();
        return new Pow(parseExpression(false));
    }

    private Element parseConst(int sign) {
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

    private Element parseBrackets() {
        Element expression = parseExpression(true);
        skipWhitespace();
        expect(')');
        return expression;
    }


    private Element parseBitwiseNot() {
        return new BitwiseNot(parseExpression(false));
    }

    private Element parseCheckedNegate() {
        if (testNumber()) {
            return parseConst(-1);
        } else {
            return new CheckedNegate(parseExpression(false));
        }
    }



    private void expectAny(char[] expect) throws ExpectionMismatch {
        for (char ch: expect) {
            if (test(ch)) {
                return;
            }
        }

        StringJoiner expectedValues = new StringJoiner(" or ");

        for (char ch: expect) {
            expectedValues.add(Character.toString(ch));
        }

        throw new ExpectionMismatch("Expected '" + expectedValues + "', found '" + getCurrentChar() + "'");
    }
}

