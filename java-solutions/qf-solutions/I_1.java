import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class ProblemI {
    public static void main(String[] args) {
        final PrintWriter pw = new PrintWriter(System.out);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    System.in,
                    StandardCharsets.UTF_8
            ));

            try {
                final int n = Integer.parseInt(reader.readLine().trim());
                long[] allX = new long[n];
                long[] allY = new long[n];
                long[] allH = new long[n];

                for (int i = 0; i < n; i++) {
                    long[] pillar = Arrays.stream(reader.readLine().split("\\s+"))
                               .mapToLong(Long::parseLong)
                               .toArray();
                    allX[i] = pillar[0];
                    allY[i] = pillar[1];
                    allH[i] = pillar[2];
                }
                long xLeft = Long.MAX_VALUE;
                long xRight = Long.MIN_VALUE;
                long yLeft = Long.MAX_VALUE;
                long yRight = Long.MIN_VALUE;

                for (int i = 0; i < n; i++) {
                    long x = allX[i];
                    long y = allY[i];
                    long h = allH[i];

                    xLeft = Math.min(xLeft, x - h);
                    xRight = Math.max(xRight, x + h);
                    yLeft = Math.min(yLeft, y - h);
                    yRight = Math.max(yRight, y + h);
                }

                StringJoiner joiner = new StringJoiner(" ");
                joiner.add(Long.toString((xLeft + xRight) / 2));
                joiner.add(Long.toString((yLeft + yRight) / 2));
                joiner.add(
                        Long.toString((long) Math.ceil(
                                (double) Math.max(xRight - xLeft, yRight - yLeft) / 2
                        ))
                );
                pw.println(joiner.toString());
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

