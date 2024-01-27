import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Set;


public class WordStatInput {
    private static boolean checkChar(char ch) {
        return Character.DASH_PUNCTUATION == Character.getType(ch) | 
        Character.isLetter(ch) |  '\'' == ch;
    }

    public static void main(String[] args) {
        LinkedHashMap<String, Integer> wordsFrequency = new LinkedHashMap<String, Integer>(); 
        StringBuilder sb = new StringBuilder();
        String inputFilename = args[0];
        String outputFilename = args[1];

        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(
                new FileInputStream(inputFilename),
                 "UTF-8"
            ));
            MyScanner scanForLines = new MyScanner(new FileInputStream(inputFilename));

            try {
                while (scanForLines.hasNextLine()) {
                    String line = scanForLines.nextLine();
                    int numberOfLettersInLine = line.length();

                    for (int i = 0; i < numberOfLettersInLine; i++) {
                        char ch = line.charAt(i);
                        if (checkChar(ch)) {
                            sb.append(ch);
                        } else {
                            String word = sb.toString().toLowerCase();
                            if (!sb.isEmpty()) {
                                wordsFrequency.put(
                                    word,
                                    wordsFrequency.getOrDefault(word, 0) + 1
                                );
                                sb.setLength(0);
                            }
                            
                        }
                    }

                    if (!sb.isEmpty()) {
                        String word = sb.toString().toLowerCase();
                        wordsFrequency.put(
                            word,
                            wordsFrequency.getOrDefault(word, 0) + 1
                        );
                        sb.setLength(0);
                    }
                }
            } finally {
                input.close();
                scanForLines.close();
            }
                
        } catch (FileNotFoundException e) {
            System.err.println("File not found" + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unsupported encoding exception" + e.getMessage());
        } catch(IOException e) {
            System.err.println("IO exception" + e.getMessage());
        }

        try {
            BufferedWriter output = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputFilename), "UTF-8")
            );

            try {
                Set<String> keys = wordsFrequency.keySet();
                String[] keysArray = keys.toArray(new String[0]);

                for (int i = 0; i < keysArray.length; i++) {
                    output.write(keysArray[i] + " " + wordsFrequency.get(keysArray[i]) + "\n");
                }
            } finally {
                output.close();
            }
        } catch(IOException e) {
            System.err.println("IO exception" + e.getMessage());
        }
    }
}
