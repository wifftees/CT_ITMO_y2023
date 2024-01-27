package expression.exceptions;

public abstract class ParseException extends ExpressionException {
    protected ParseException(String message, Throwable cause) {
        super(message, cause);
    }

    protected ParseException(String message) {
        super(message);
    }
}
