import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class WsppSortedPosition {
    


    public static void main(String[] args) {
        String inputFilename = args[0];
        String outputFilename = args[1];
        final Map<String, List<Pair>> wordsFrequency = new LinkedHashMap<>();

        try {
            MyScanner scanner = new MyScanner(
                new FileInputStream(inputFilename)
            );

            try {

                int numberOfLine = 0;
                while (scanner.hasNextLine()) {
                    numberOfLine++;
                    scanner.moveToNextLine();
                    List<String> wordsInLine = new ArrayList<String>();

                    while (scanner.hasNextWord()) {
                        String word = scanner.nextWord();
                        wordsInLine.add(word.toLowerCase());
                    }

                    Collections.reverse(wordsInLine);

                    int cntOfWords = 0;
                    Map<String, List<Pair>> mapForLine = new HashMap<String, List<Pair>>();
                    for(int i = 0; i < wordsInLine.size(); i++) {
                        String word = wordsInLine.get(i);
                        cntOfWords++;
                        Pair pair = new Pair(numberOfLine, cntOfWords);
                        mapForLine.putIfAbsent(word, new ArrayList<>());
                        mapForLine
                                .get(word)
                                .add(pair);   
                    }

                    Set<String> uniqueWordsInLine = new HashSet<String>(wordsInLine); 

                    for (String word: uniqueWordsInLine) {
                        List<Pair> pairsOfWord = mapForLine.get(word);
                        Collections.reverse(pairsOfWord);

                        wordsFrequency.putIfAbsent(word, new ArrayList<>());
                        for(Pair pair: pairsOfWord) {
                            wordsFrequency
                                .get(word)
                                .add(pair);  
                        }

                    }

                }
            } finally {
                try {
                    scanner.close();
                } catch (IOException e) {
                    System.err.println("Exception in action thread: " + e.getMessage());
                }
                
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unsupported encoding exception" + e.getMessage());
        } catch(IOException e) {
            System.err.println("Exception while working with file: " + e.getMessage());
        }

        try (BufferedWriter output = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outputFilename),
                StandardCharsets.UTF_8
        ))) {

            try {
                Set<String> keys = wordsFrequency.keySet();
                List<String> keysArray = new ArrayList<>(keys);
                Collections.sort(keysArray);
                

                for (int i = 0; i < keysArray.size(); i++) {
                    List<Pair> pairsWordIndex = wordsFrequency.get(keysArray.get(i));
                    int numberOfWordEntries = pairsWordIndex.size();
                    StringBuilder outputForWord = new StringBuilder(
                            String.format("%s %d", keysArray.get(i), numberOfWordEntries)
                    );

                    for (int j = 0; j < numberOfWordEntries; j++) {
                        outputForWord.append(
                                String.format(" %s", pairsWordIndex.get(j).toString())
                        );
                    }
                    outputForWord.append("\n");

                    output.write(
                            outputForWord.toString()
                    );
                }
            } finally {
                try {
                    output.close();
                } catch(IOException e) {
                    System.err.println("Can't close file: " + e.getMessage());
                }
                
            } 
        } catch(IOException e) {
            System.err.println("Exception while opening file" + e.getMessage());
        }

    }
}
