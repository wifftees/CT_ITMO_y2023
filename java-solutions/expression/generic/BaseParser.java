package expression.generic;

import expression.exceptions.ExpectationMismatch;
import expression.parser.CharSource;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.StringJoiner;

public abstract class BaseParser {

    protected static final char END = '\0';
    protected CharSource source;
    private char ch;

    protected Queue<Character> queue = new ArrayDeque<>();

    protected char take() {
        if (!queue.isEmpty()) {
            ch = queue.remove();
            return ch;
        }

        return takeNext();
    }

    protected char takeNext() {
        ch = source.hasNext() ? source.next() : END;
        return ch;
    }


    protected Character lookForward() {
        if (queue.isEmpty()) {
            queue.add(takeNext());
        }

        return queue.peek();
    }
    protected boolean lookForward(final String word) {
        while (word.length() > queue.size()) {
            queue.add(takeNext());
        }

        List<Character> chars = createListFromQueue(queue);

        boolean success = true;

        for (int i = 0; i < word.length(); i++) {
            if (chars.get(i) != word.charAt(i)) {
                success = false;
                break;
            }
        }

        return success;
    }


    protected boolean lookForwardAny(String[] words) {
        for (String word : words) {
            if (lookForward(word)) {
                return true;
            }
        }

        return false;
    }

    private List<Character> createListFromQueue(final Queue<Character> q) {
        List<Character> chars = new ArrayList<>();

        for (int i = 0; i < q.size(); i++) {
            chars.add(q.remove());
            q.add(chars.getLast());
        }

        return chars;
    }

    protected boolean take(final char expected) {
        if (lookForward(Character.toString(expected))) {
            take();
            return true;
        }
        return false;
    }

    protected boolean take(final String list) { // :NOTE: String?
        if (lookForward(list)) {
            for (int i = 0; i < list.length(); i++) {
                take();
            }

            return true;
        }
        return false;
    }

    protected void expect(final char expected) throws ExpectationMismatch {
        if (!take(expected)) {
            throw error("Expected '" + expected + "', found '" + ch + "'");
        }
    }

    protected char getCurrentChar() {
        if (!queue.isEmpty()) {
            return queue.peek();
        }

        return ch;
    }

    protected void skipWhitespace() {
        while (Character.isWhitespace(lookForward())) {
            take();
        }
    }

    protected ExpectationMismatch error(final String message) {
        return source.error(message);
    }
}
