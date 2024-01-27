package game;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InputChecker {
    private static final Set<Character> digits = new HashSet<>(List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
    public static boolean isValidBoardSize(int m, int n) {
        return (m > 0) && (n > 0);
    }

    public static boolean isValidKValue(int k) {
       return k > 0;
    }
    public static boolean isValidInput(String inputLine) {
        for (int i = 0; i < inputLine.length(); i++) {
             char ch = inputLine.charAt(i);

             if (!Character.isSpaceChar(ch) && !digits.contains(ch)) {
                return false;
             }
        }
        return true;
    }
}
