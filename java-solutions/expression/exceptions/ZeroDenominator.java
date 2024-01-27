package expression.exceptions;

public class ZeroDenominator extends EvaluationException {
    public ZeroDenominator(String message) {
        super(message);
    }
}
