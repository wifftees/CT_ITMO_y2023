import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputManager {
    public static int[] parseLine(String line, int numOfArgs) throws NoSuchElementException {
        int[] result = new int[numOfArgs];

        try (Scanner scanner = new Scanner(line)) {
            for (int i = 0; i < numOfArgs; i++) {
                result[i] = scanner.nextInt();
            }
        }

        return result;
    }
}
