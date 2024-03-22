package expression.exceptions;

public abstract class EvaluationException extends RuntimeException {
    protected EvaluationException(String message) {
        super(message);
    }
}
