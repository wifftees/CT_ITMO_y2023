package expression.exceptions;

public class ExpectationMismatch extends ParseException {
    public ExpectationMismatch(String message) {
        super(message);
    }
}
