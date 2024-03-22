package expression.parser;

import expression.Add;
import expression.BitwiseAnd;
import expression.BitwiseNot;
import expression.BitwiseOr;
import expression.BitwiseXOR;
import expression.Const;
import expression.Divide;
import expression.Element;
import expression.Multiply;
import expression.Sign;
import expression.Subtract;
import expression.TripleExpression;
import expression.Variable;
import expression.exceptions.ExpectationMismatch;
import expression.exceptions.TripleParser;

public class ExpressionParser extends BaseParser implements TripleParser {
    private enum OperatorType {
        OR, XOR, AND, ADD, PRODUCT
    }

    @Override
    public TripleExpression parse(String expression) throws ExpectationMismatch {
        source = new StringSource(expression);

        take();

        return parseExpression(true);
    }

    private Element parseExpression(boolean wholeExp) throws ExpectationMismatch {
        skipWhitespace();
        return parseOperator(wholeExp, OperatorType.OR);
    }

    private char[] createArrayFromChars(char... chars) {
        return chars;
    }
    private char[] getTakeValuesByOperationType(OperatorType operator) {
        return switch (operator) {
            case OR -> createArrayFromChars('|');
            case XOR -> createArrayFromChars('^');
            case AND -> createArrayFromChars('&');
            case ADD -> createArrayFromChars('+', '-');
            case PRODUCT -> createArrayFromChars('*', '/');
        };
    }

    private Element parseOperator(boolean wholeExp, OperatorType operator) throws ExpectationMismatch {
        Element leftPart = switch (operator) {
            case OR -> parseOperator(wholeExp, OperatorType.XOR);
            case XOR -> parseOperator(wholeExp, OperatorType.AND);
            case AND -> parseOperator(wholeExp, OperatorType.ADD);
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
                case OR -> new BitwiseOr(leftPart, parseOperator(true, OperatorType.XOR));
                case XOR -> new BitwiseXOR(leftPart, parseOperator(true, OperatorType.AND));
                case AND -> new BitwiseAnd(leftPart, parseOperator(true, OperatorType.ADD));
                case ADD -> {
                    if (currentSymbol == '+') {
                        yield new Add(leftPart, parseOperator(true, OperatorType.PRODUCT));
                    } else {
                        yield new Subtract(leftPart, parseOperator(true, OperatorType.PRODUCT));
                    }
                }
                case PRODUCT -> {
                    if (currentSymbol == '*') {
                        yield new Multiply(leftPart, parseLowestLevel());
                    } else {
                        yield new Divide(leftPart, parseLowestLevel());
                    }
                }
            };
            skipWhitespace();
        }

        return leftPart;
    }

    private Element parseLowestLevel() throws ExpectationMismatch {
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

    private Element parseBrackets() throws ExpectationMismatch {
        Element expression = parseExpression(true);
        skipWhitespace();
        expect(')');
        return expression;
    }


    private Element parseBitwiseNot() throws ExpectationMismatch {
        return new BitwiseNot(parseExpression(false));
    }

    private Element parseSign() throws ExpectationMismatch {
        if (testNumber()) {
            return parseConst(-1);
        } else {
            return new Sign(parseExpression(false));
        }
    }
}

