package expression.parser;


public abstract class BaseParser {
    protected static final char END = '\0';
    protected CharSource source;
    private char ch;
    private static final char[] numbers = "0123456789".toCharArray();

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected boolean testAny(final char[] expected) {
        for (char ch: expected) {
            if (test(ch)) {
                return true;
            }
        }

        return false;
    }

    protected boolean testNumber() {
        return testAny(numbers);
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected boolean take(final char[] list) {
        for (var item: list) {
            if (test(item)) {
                take();
                return false;
            }
        }

        return true;
    }

    protected boolean takeAll(final char[] list) {
        for (var item: list) {
            if (test(item)) {
                take();
            } else {
                return false;
            }
        }

        return true;

    }

    protected void expect(final char expected) {
        if (!take(expected)) {
            throw error("Expected '" + expected + "', found '" + ch + "'");
        }
    }

    protected void expect(final String value) {
        for (final char c : value.toCharArray()) {
            expect(c);
        }
    }

    protected boolean eof() {
        return take(END);
    }

    protected char getCurrentChar() {
        return ch;
    }

    protected void skipWhitespace() {
        while (Character.isWhitespace(ch)) {
            take();
        }
    }

    protected IllegalArgumentException error(final String message) {
        return source.error(message);
    }
}
