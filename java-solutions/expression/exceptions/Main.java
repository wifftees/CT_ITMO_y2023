package expression.exceptions;

import expression.Element;

import java.util.List;

public class Main {
    public static void main(String[] args) throws ExpressionException {
        String[] s =  new String[]{
        "(($1) + 30))"};
        ExpressionParser parser = new ExpressionParser();
        for (String a: s) {
                Element x = parser.parse(a, List.of(""));
                System.out.println(x);
                System.out.println(x.evaluate(1));

        }
    }
}
