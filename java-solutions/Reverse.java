import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class Reverse {
    public static void main(String[] args) {
        try {
            MyScanner scanForLines = new MyScanner(System.in);

            try {
                int numberOfLines = 0;
                int limit = 1_000_000;
                int[][] lines = new int[limit][];
                int[] lenOfLines = new int[limit];
                int maxLenOfLine = 0;

                while (scanForLines.hasNextLine()) {
                    int[] temp = new int[1];
                    int startOfTemp = 0;
                    int endOfTemp = 1;
                    String s = scanForLines.nextLine();
                    MyScanner scanForLine = new MyScanner(s);

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
                
                for (int i = numberOfLines - 1; i >= 0; i--) {
                    for (int j = lenOfLines[i] - 1; j >= 0; j--) {
                        System.out.print(lines[i][j] + " ");
                        //System.err.print(lines[i][j] + " ");
                    }
                        System.out.println();
                        //System.err.println();
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
