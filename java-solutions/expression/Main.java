package expression;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static final Set<Character> digits = new HashSet<>(List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));

    public static boolean isValidInput(String inputLine) {
        for (int i = 0; i < inputLine.length(); i++) {
            char ch = inputLine.charAt(i);

            if (!Character.isSpaceChar(ch) && !digits.contains(ch)) {
                return false;
            }
        }
        return true;
    }

    // using this function because it can be more than one number in input
    public static int parseInt(String inputLine) {
        try (Scanner scanner = new Scanner(inputLine)) {
            return scanner.nextInt();
        }
    }

    public static void main(String[] args) {
//        Expression polynom = new Add(
//                new Subtract(
//                        new Multiply(new Variable("x"), new Variable("x")),
//                        new Multiply(new Const(2), new Variable("x"))
//                ),
//                new Const(1)
//        );


        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Please type a number: ");
                // handling Ctrl+D
                if (!scanner.hasNextLine()) {
                    System.out.println("End of input.");
                    return;
                }

                String inputLine = scanner.nextLine();

                if (!isValidInput(inputLine)) {
                    System.out.println("Incorrect input data, please try again");
                    continue;
                }

                int value = parseInt(inputLine);

            }
        }
    }
}