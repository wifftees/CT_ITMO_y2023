package expression;

public class BitwiseOr extends BinaryOperator {
    public BitwiseOr(Element element1, Element element2) {
        super(element1, element2);
    }

    @Override
    public String getOperatorSymbol() {
        return "|";
    }

    @Override
    public int calc(int a, int b) {
        return a | b;
    }
}
