import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ProblemJ {
    public static void main(String[] args) {
        final PrintWriter pw = new PrintWriter(System.out);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    System.in,
                    StandardCharsets.UTF_8
            ));

            try {
                final int n = Integer.parseInt(reader.readLine().trim());
                int[][] inputTable = new int[n][n];
                int[][] outputTable = new int[n][n];

                for (int i = 0; i < n; i++) {
                    char[] lineOfChars = reader.readLine().toCharArray();

                    for (int j = 0; j < n; j++) {
                       inputTable[i][j] = lineOfChars[j] - '0';
                    }
                }

                for (int i = 0; i < n; i++) {
                    for (int j = i; j < n; j++) {
                        if (inputTable[i][j] == 1) {
                            outputTable[i][j] = 1;

                            for (int k = j + 1; k < n; k++) {
                                inputTable[i][k] = (inputTable[i][k] - inputTable[j][k]) % 10;
                            }
                        }
                    }
                }

                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                       pw.print(outputTable[i][j]);
                    }
                    pw.println();
                }


            } catch (IOException e) {
                pw.println(e.getMessage());
            } finally {
                pw.close();
                reader.close();
            }
        } catch (IOException e) {
            pw.println(e.getMessage());
        }
    }
}

