package expression.parser;

import expression.Element;
import expression.TripleExpression;
public class Main {
    public static void main(String[] args) {
        TripleParser parser = new ExpressionParser();
        TripleExpression equation = parser.parse("(x / y)");
        //TripleExpression equation = parser.parse("-(x)");
        System.out.println(equation);
    }
}
