package expression;

public class Subtract extends BinaryOperator {
    public Subtract(Element element1, Element element2) {
        super(element1, element2);
    }

    @Override
    public String getOperatorSymbol() {
        return "-";
    }

    @Override
    public int calc(int a, int b) {
        return a - b;
    }
}