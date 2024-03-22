import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class ReverseMinCAbc {
    private static char[] abcValues = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};

    public static int convertAbcToInt(String abc) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < abc.length(); i++) {
            char ch = abc.charAt(i);
            for (int j = 0; j < 10; j++) {  
                if (ch == abcValues[j]) {
                    sb.append(j);
                    break;
                } else if (ch == '-') {
                    sb.append('-');
                    break;
                }
            }
        }
        return Integer.parseInt(sb.toString());   
    }

    public static String convertIntToAbc(int value) {
        StringBuilder sbAbcValue = new StringBuilder();

        int absValue = Math.abs(value);

        while (absValue > 0) {
            int lastDigit = absValue % 10;
            sbAbcValue.append(abcValues[lastDigit]);
            absValue /= 10;
        }
        
        if (value == 0) {
            sbAbcValue.append("a");
        }

        if (value < 0) {
            sbAbcValue.append("-");
        }

        sbAbcValue.reverse();

        return sbAbcValue.toString();
    }
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
                    String s = scanForLines.nextLine();
                    MyScanner scanForLine = new MyScanner(s);
                    while (scanForLine.hasNextAbc()) {
                        if (startOfTemp == endOfTemp) {
                            endOfTemp = temp.length * 2;
                            temp = Arrays.copyOf(temp, temp.length * 2);
                        }
                        temp[startOfTemp] = convertAbcToInt(scanForLine.nextAbc());
                        startOfTemp++;
                    }

                    scanForLine.close();

                    lines[numberOfLines] = temp;
                    lenOfLines[numberOfLines] = startOfTemp;
                    maxLenOfLine = Math.max(maxLenOfLine, startOfTemp);
                    numberOfLines++;
                }

                int[] columnMin = new int[maxLenOfLine];
                for (int i = 0; i < maxLenOfLine; i++) {
                    columnMin[i] = Integer.MAX_VALUE;
                }
                for (int i = 0; i < numberOfLines; i++) {
                    for (int j = 0; j < lenOfLines[i]; j++) {
                            columnMin[j] = Math.min(columnMin[j], lines[i][j]);
                            System.out.print(
                                convertIntToAbc(columnMin[j]) + " "
                            );
                    }
                    System.out.println("");
                }

                scanForLines.close();
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
