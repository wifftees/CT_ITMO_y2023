package expression;

public class Multiply extends BinaryOperator {
    public Multiply(Element element1, Element element2) {
        super(element1, element2);
    }

    @Override
    public String getOperatorSymbol() {
        return "*";
    }

    public int calc(int a, int b) {
        return a * b;
    }
}
