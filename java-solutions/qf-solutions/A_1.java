import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class ProblemA {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    System.in,
                    StandardCharsets.UTF_8
            ));
            final PrintWriter pw = new PrintWriter(System.out);

            try {
                String[] input = reader.readLine().split("\s+");
                int a = Integer.parseInt(input[0]);
                int b = Integer.parseInt(input[1]);
                int n = Integer.parseInt(input[2]);

                long ans = 2 * ((long) Math.ceil(
                        (double) (n - b) / (b - a)
                )) + 1;
                
                pw.println(ans);
            } catch (IOException e) {
                pw.println(e.getMessage());
            } finally {
                pw.close();
                reader.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

