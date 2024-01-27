import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class ProblemB {
    public static void main(String[] args) {
        final PrintWriter pw = new PrintWriter(System.out);

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    System.in,
                    StandardCharsets.UTF_8
            ));

            try {
                final int n = Integer.parseInt(reader.readLine().trim());
                StringBuilder sb = new StringBuilder();
                int startPoint = -710 * 25000;

                for (int i = 0; i < n; i++) {
                    sb.append(startPoint + (710 * (i + 1))).append("\n");
                }

                pw.println(sb.toString());


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

