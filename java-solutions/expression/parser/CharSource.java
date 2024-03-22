package expression.parser;

import expression.exceptions.ExpectationMismatch;

public interface CharSource {
    boolean hasNext();
    char next();
    ExpectationMismatch error(String message);
}
