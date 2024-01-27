import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class ProblemM  {
    public static long solve(int n, int[] a) {
        Map<Integer, Integer> map = new HashMap<>();
        long ans = 0;

        for (int i = 0; i < n; i++) {
            map.clear();

            for (int j = n - 1; j >= i + 1; j--) {
                long k = (long) 2 * a[j]  - a[i];

                if (k > Integer.MAX_VALUE) {
                    continue;
                }

                ans += (long) map.getOrDefault((int) k, 0);

                int count = map.getOrDefault(a[j], 0);
                map.put(a[j], count + 1);
            }
        }

        return ans;
    }

    public static void main(String[] args) {
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(
                    System.in,
                    StandardCharsets.UTF_8
            ));
            final PrintWriter pw = new PrintWriter(System.out);

            try {
                int t = Integer.parseInt(reader.readLine());
                int n;
                for (int i = 0; i < t; i++) {
                    n = Integer.parseInt(reader.readLine());
                    int[] a = Arrays.stream(reader.readLine().split("\\s+"))
                            .mapToInt(Integer::parseInt)
                            .toArray();
                    long ans = solve(n, a);
                    pw.println(ans);
                }

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

