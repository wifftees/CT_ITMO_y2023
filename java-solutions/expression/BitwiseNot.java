package expression;

public class BitwiseNot extends UnaryOperator {
    public BitwiseNot(Element child) {
        super(child);
    }

    @Override
    public String getOperatorSymbol() {
        return "~";
    }

    @Override
    public int calc(int element) {
        return ~element;
    }
}
