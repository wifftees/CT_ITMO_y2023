package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TextExtractor implements AutoCloseable {
    private final static String lineSeparator = System.lineSeparator();
    private final BufferedReader reader;
    public static class ParagraphManager {
        private final List<List<String>> paragrahps;
        private int pIndex = 0;
        public ParagraphManager(List<List<String>> list) {
           paragrahps = list;
        }
        public boolean hasNextParagraph() {
            return pIndex + 1 <= paragrahps.size();
        }
        public List<String> nextParagraph() {
            return paragrahps.get(pIndex++);
        }
    }
    public TextExtractor(String inputFileName) throws IOException {
        reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(inputFileName), StandardCharsets.UTF_8
                )
        );
    }

    private void updateLastLine(List<String> paragraph) {
        int pLength = paragraph.size();
        String lastLine = paragraph.get(pLength -1);
        String newLastLine = lastLine.substring(0, lastLine.length() - lineSeparator.length());
        paragraph.set(pLength - 1, newLastLine);
    }

    public ParagraphManager splitByParagraphs() throws IOException {
        List<List<String>> paragraphs = new ArrayList<>();
        List<String> paragraph = new ArrayList<>();
        String line = reader.readLine();

        while (line != null) {
            if (line.isEmpty() && !paragraph.isEmpty()) {
               updateLastLine(paragraph);
               paragraphs.add(paragraph);
               paragraph = new ArrayList<>();
            } else if(!line.isEmpty()) {
                paragraph.add(
                       line + lineSeparator
                );
            }
            line = reader.readLine();
        }

        if (!paragraph.isEmpty()) {
           updateLastLine(paragraph);
           paragraphs.add(paragraph);
        }
        return new ParagraphManager(paragraphs);
    }

    public void close() throws IOException {
        reader.close();
    }
}
