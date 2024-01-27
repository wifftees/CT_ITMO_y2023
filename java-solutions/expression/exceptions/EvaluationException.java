package expression.exceptions;

public abstract class EvaluationException extends ExpressionException {
    protected EvaluationException(String message) {
        super(message);
    }
}
