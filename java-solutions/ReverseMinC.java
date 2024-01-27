import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class ReverseMinC {
    public static void main(String[] args) {
        try {
            MyScanner scanForLines = new MyScanner(System.in);

            try {
                int numberOfLines = 0;
                int limit = 1_000_000;
                int[][] lines = new int[limit][];
                int[] lenOfLines = new int[limit];
                int maxLenOfLine= 0;

                while (scanForLines.hasNextLine()) {
                    int[] temp = new int[1];
                    int startOfTemp = 0;
                    int endOfTemp = 1;
                    MyScanner scanForLine = new MyScanner(scanForLines.nextLine());

                    while (scanForLine.hasNextInt()) {
                        if (startOfTemp == endOfTemp) {
                            endOfTemp = temp.length * 2;
                            temp = Arrays.copyOf(temp, temp.length * 2);
                        }
                        temp[startOfTemp] = scanForLine.nextInt();
                        startOfTemp++;
                    }

                    scanForLine.close();

                    lines[numberOfLines] = temp;
                    lenOfLines[numberOfLines] = startOfTemp;
                    maxLenOfLine = Math.max(maxLenOfLine, startOfTemp);
                    numberOfLines++;
                }

                scanForLines.close();

                int[] columnMin = new int[maxLenOfLine];
                for (int i = 0; i < maxLenOfLine; i++) {
                    columnMin[i] = Integer.MAX_VALUE;
                }
                for (int i = 0; i < numberOfLines; i++) {
                    for (int j = 0; j < lenOfLines[i]; j++) {
                            columnMin[j] = Math.min(columnMin[j], lines[i][j]);
                            System.out.print(columnMin[j] + " ");
                    }
                    System.out.println("");
                }

            } catch(NoSuchElementException e) {
                System.err.println("Do not have next line" + e.getMessage());
            } catch(NumberFormatException e) {
                System.err.println("Do not have next int" + e.getMessage());
            } catch(IOException e) {
                System.err.println("Exception in read method" + e.getMessage());
            } finally {
                scanForLines.close();
            } 
        } catch (FileNotFoundException e) {
            System.err.println("File was not found" + e.getMessage());
        } catch (IOException e) {
            System.err.println("Unexpected IO Exception" + e.getMessage());
        }
    }
}
