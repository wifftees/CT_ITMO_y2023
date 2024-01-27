import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Wspp {
    
    private static boolean checkChar(char ch) {
        return Character.DASH_PUNCTUATION == Character.getType(ch) |
                Character.isLetter(ch) |  '\'' == ch;
    }

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        String inputFilename = args[0];
        String outputFilename = args[1];
        final Map<String, List<Integer>> wordsFrequency = new LinkedHashMap<>();

        try {
            MyScanner scanner = new MyScanner(
                new FileInputStream(inputFilename)
            );

            try {
                int cntOfWords = 0;
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    int numberOfLettersInLine = line.length();

                    for (int i = 0; i < numberOfLettersInLine; i++) {
                        char ch = line.charAt(i);
                        if (checkChar(ch)) {
                            sb.append(ch);
                        } else {
                            if (!sb.isEmpty()) {
                                String word = sb.toString().toLowerCase();
                                cntOfWords++;

                                wordsFrequency.putIfAbsent(word, new ArrayList<>());
                                wordsFrequency
                                        .get(word)
                                        .add(cntOfWords);
                                sb.setLength(0);
                            }

                        }
                    }

                    if (!sb.isEmpty()) {
                        String word = sb.toString().toLowerCase();
                        cntOfWords++;

                        wordsFrequency.putIfAbsent(word, new ArrayList<>());
                        wordsFrequency
                                .get(word)
                                .add(cntOfWords);
                        sb.setLength(0);
                    }
                }


            } finally {
                try {
                    scanner.close();
                } catch (IOException e) {
                    System.err.println("IO exception: " + e.getMessage());
                }
                
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unsupported encoding exception" + e.getMessage());
        } catch(IOException e) {
            System.err.println("IO exception: " + e.getMessage());
        }

        try (BufferedWriter output = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outputFilename),
                StandardCharsets.UTF_8
        ))) {

            try {
                Set<String> keys = wordsFrequency.keySet();
                String[] keysArray = keys.toArray(new String[0]);

                for (int i = 0; i < keysArray.length; i++) {
                    List<Integer> indexesOfWord = wordsFrequency.get(keysArray[i]);
                    int numberOfWordEntries = indexesOfWord.size();
                    StringBuilder outputForWord = new StringBuilder(
                            String.format("%s %d", keysArray[i], numberOfWordEntries)
                    );

                    for (int j = 0; j < numberOfWordEntries; j++) {
                        outputForWord.append(
                                String.format(" %d", indexesOfWord.get(j))
                        );
                    }
                    outputForWord.append("\n");

                    output.write(
                            outputForWord.toString()
                    );
                }
            } finally {
                output.close();
            }
        } catch(IOException e) {
            System.err.println("IO exception" + e.getMessage());
        }

    }
}
