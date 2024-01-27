package expression.exceptions;

public class ParsingOverflow extends ParseException {
    public ParsingOverflow(String message, Throwable cause) {
        super(message, cause);
    }
}
