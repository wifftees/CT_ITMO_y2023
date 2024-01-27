package expression.exceptions;


public class OverflowException extends EvaluationException {
    public OverflowException(String message) {
        super(message);
    }

    public static void detectOverflow(long result, String message) throws OverflowException {
        if (result < Integer.MIN_VALUE || result > Integer.MAX_VALUE) {
            throw new OverflowException(message);
        }
    }
}
