package expression;

public class Sign extends UnaryOperator {
    public Sign(Element inputElement) {
        super(inputElement);
    }

    @Override
    public String getOperatorSymbol() {
        return "-";
    }

    @Override
    public int calc(int element) {
        return -element;
    }
}
