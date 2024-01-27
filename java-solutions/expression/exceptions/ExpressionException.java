package expression.exceptions;

public abstract class ExpressionException extends RuntimeException {
    protected ExpressionException(String message, Throwable cause) {
        super(message, cause);
    }

    protected ExpressionException(String message) {
        super(message);
    }

}
