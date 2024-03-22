package expression.generic;

import expression.exceptions.ExpressionException;
import expression.generic.elements.Element;
import expression.generic.implementation.DoubleConfigGenerator;


public class Main {
    private static class ArgsExtractor {
        public final String expression;

        public ArgsExtractor(String[] args) {
            this.expression = args[1];
        }
    }

    public static void main(String[] args) throws ExpressionException {
        ExpressionParser<Double> parser = new ExpressionParser<>(new DoubleConfigGenerator());
        Element<Double> exp = parser.parse("0 + -1");

        System.out.println(exp);
        System.out.println(exp.evaluate(0, -3, 3));





    }
}
