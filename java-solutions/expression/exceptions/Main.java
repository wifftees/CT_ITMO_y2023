package expression.exceptions;

import expression.Element;

public class Main {
    public static void main(String[] args) {
        String input = "-2147483648 * -1";
        ExpressionParser parser = new ExpressionParser();
        Element x = parser.parse(input);
        System.out.println(x);
        System.out.println(x.evaluate(1));
    }
}
