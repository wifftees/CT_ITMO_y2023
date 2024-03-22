package expression.parser;

import expression.exceptions.ExpectationMismatch;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.StringJoiner;

public abstract class BaseParser {

    protected static final char END = '\0';
    protected CharSource source;
    private char ch;
    private static final char[] numbers = "0123456789".toCharArray();

    protected Queue<Character> queue = new ArrayDeque<>();

    protected char take() {
        if (!queue.isEmpty()) {
            return queue.remove();
        }

        return takeNext();
    }

    protected char takeNext() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected boolean lookForward(final char[] word) {
        while (word.length > queue.size()) {
            queue.add(takeNext());
        }

        List<Character> chars = createListFromQueue(queue);

        boolean success = true;

        for (int i = 0; i < chars.size(); i++) {
            if (chars.get(i) != word[i]) {
                success = false;
                break;
            }
        }

        queue = new ArrayDeque<>(chars);

        return success;
    }

    private List<Character> createListFromQueue(final Queue<Character> q) {
        List<Character> chars = new ArrayList<>();

        while (!q.isEmpty()) {
            chars.add(q.remove());
        }

        return chars;
    }

    protected boolean test(final char expected) {
        return ch == expected;
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

    protected boolean take(final char[] list) { // :NOTE: String?
        for (var item: list) {
            if (!test(item)) {
                return false;
            }
            take();
        }

        return true;
    }

    protected boolean test(final char[] expected) {
        for (var item: expected) {
            if (!test(item)) {
                return false;
            }
        }
        return true;
    }

    protected boolean testAny(final char... expected) {
        for (char ch: expected) {
            if (test(ch)) {
                return true;
            }
        }

        return false;
    }

    protected void expect(final char expected) throws ExpectationMismatch {
        if (!take(expected)) {
            throw error("Expected '" + expected + "', found '" + ch + "'");
        }
    }

    protected void expectAny(final char[] expect) throws ExpectationMismatch {
        for (char ch: expect) {
            if (test(ch)) {
                return;
            }
        }

        StringJoiner expectedValues = new StringJoiner(" or ");

        for (char ch: expect) {
            expectedValues.add(Character.toString(ch));
        }

        throw new ExpectationMismatch("Expected '" + expectedValues + "', found '" + getCurrentChar() + "'");
    }

    protected char getCurrentChar() {
        if (!queue.isEmpty()) {
            return queue.remove();
        }

        return ch;
    }

    protected void skipWhitespace() {
        while (Character.isWhitespace(ch)) {
            take();
        }
    }

    protected ExpectationMismatch error(final String message) {
        return source.error(message);
    }
}
