package expression.parser;
import expression.*;

import java.util.HashMap;
import java.util.Map;

public class ExpressionParser extends BaseParser implements TripleParser {
    @Override
    public TripleExpression parse(String expression) {
        source = new StringSource(expression);

        take();

        return parseExpression(true);
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
                    case ADD -> new Add(leftPart, parseBinaryOperator(true, BinaryOperatorType.PRODUCT));
                    case PRODUCT -> new Multiply(leftPart, parseLowestLevel());
                };
            } else if (take(takeValues[1])) {
                leftPart = switch (operator) {
                    case ADD -> new Subtract(leftPart, parseBinaryOperator(true, BinaryOperatorType.PRODUCT));
                    case PRODUCT -> new Divide(leftPart, parseLowestLevel());
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
        } else if (take('-')) {
            return parseSign();
        } else if (testAny("xyz".toCharArray())) {
            return parseVariable();
        } else if (testNumber()) {
            return parseConst(1);
        } else if (take('~')) {
            return parseBitwiseNot();
        } else {
            throw error("Unexpected character");
        }
    }

    private Element parseVariable() {
        return new Variable(Character.toString(take()));
    }

    private Element parseConst(int sign) {
        StringBuilder number = new StringBuilder((sign > 0) ? "" : "-");

        while (testNumber()) {
            number.append(take());
        }

        return new Const(Integer.parseInt(number.toString()));
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

    private Element parseSign() {
        if (testNumber()) {
            return parseConst(-1);
        } else {
            return new Sign(parseExpression(false));
        }
    }
}

