import java.io.Reader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

public class MyScanner {
    private final boolean isInputString;
    private boolean nextIntIsUsed = true;
    private boolean nextWordIsUsed = true;
    private boolean isSourceOpen = false;

    private final int bufferSize = 1024;
    private int numberOfSymbols = 0;
    private int currentIndexOfCharacter = -1;
    private int lenOfCurrentLine;
    private int currentIndexOfCharacterInLine = 0;
    
    
    private StringBuilder currentLine;
    private StringBuilder nextIntValue;
    private StringBuilder nextAbcValueInt;
    private StringBuilder nextWordValue;


    private final String encoding = "UTF-8";
    private final String lineSeparator = System.lineSeparator();
    private final int lenOfLineSeparator = lineSeparator.length(); 

    private Reader reader;    
    private final char[] abcValues = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
    private final char[] buffer = new char[bufferSize];

    

    public MyScanner(String in) {
        currentLine =  new StringBuilder(in);
        nextIntValue = new StringBuilder();
        nextAbcValueInt = new StringBuilder();
        nextWordValue = new StringBuilder();
        lenOfCurrentLine = currentLine.length();
        isInputString = true;
    }

    public MyScanner(InputStream stream) throws FileNotFoundException, IOException {
        isInputString = false;
        reader = new InputStreamReader(stream, encoding);
        currentLine = new StringBuilder();
        nextWordValue = new StringBuilder();
        lenOfCurrentLine = 0;
        isSourceOpen = true;
    }

    private static boolean checkChar(char ch) {
        return Character.DASH_PUNCTUATION == Character.getType(ch) |
                Character.isLetter(ch) |  '\'' == ch;
    }

    public void close() throws IOException {
        if (isSourceOpen) {
            reader.close();
        }
    }

    private void fillBuffer() throws IOException {
        numberOfSymbols = reader.read(buffer);
        currentIndexOfCharacter = -1;
    }

    private void skipLineSeparator() {
        currentIndexOfCharacter += lenOfLineSeparator;
    }

    public boolean hasNextLine() throws IOException {
        if (isInputString) {
            return false;
        }
        
        if (
            (currentIndexOfCharacter + 1 == bufferSize) | 
            (numberOfSymbols == 0)
        ) {
            fillBuffer();
        }

        if (numberOfSymbols == -1) {
            return false;
        }
        return currentIndexOfCharacter + 1 < numberOfSymbols;
    }

    public String nextLine() throws IOException, NoSuchElementException {
        if (!hasNextLine()) {
            throw new NoSuchElementException("No line found");
        }
        currentLine.setLength(0);
        if (currentIndexOfCharacter == -1) {
            currentIndexOfCharacter++;
        } else {
            skipLineSeparator();
        }
    
        if (currentIndexOfCharacter >= bufferSize) {
            fillBuffer();
            currentIndexOfCharacter++;
        }

        char ch = buffer[currentIndexOfCharacter];
        while ((ch != lineSeparator.charAt(0)) & (currentIndexOfCharacter < numberOfSymbols)) {
            currentLine.append(ch);

            currentIndexOfCharacter++;
            if (currentIndexOfCharacter == bufferSize) {
                fillBuffer();
                currentIndexOfCharacter++;
            }

            ch = buffer[currentIndexOfCharacter];
        }

        lenOfCurrentLine = currentLine.length();
        return currentLine.toString();
    }

    

    private void findNextInt() {
        nextIntValue.setLength(0);

        int i = currentIndexOfCharacterInLine;
        while (i < lenOfCurrentLine) {
            char ch = currentLine.charAt(i);

            if (Character.isDigit(ch) | ch == '-') {
                nextIntValue.append(ch);
            } else {
                if (!nextIntValue.isEmpty()) {
                    break;
                }
            }
            i++;
        }

        currentIndexOfCharacterInLine = i;
        nextIntIsUsed = nextIntValue.isEmpty();
    }

    public boolean hasNextInt() {
        if (!nextIntIsUsed) {
            return true;
        } else {
            findNextInt();
        }     
        return !nextIntValue.isEmpty();  
    }

    public int nextInt() throws InputMismatchException {
        if (hasNextInt()) {
            nextIntIsUsed = true;

            return Integer.parseInt(nextIntValue.toString());
        } else {
            throw new InputMismatchException();
        }
        
    }

    private boolean isAbcValues(char ch) {      
        for (int i = 0; i < 10; i++) {
            if (ch == abcValues[i]) {
                return true;
            }    
        }
        return false;
    }

    private void findNextAbc() {
        nextAbcValueInt.setLength(0);

        int i = currentIndexOfCharacterInLine;
        while (i < lenOfCurrentLine) {
            char ch = currentLine.charAt(i);
            if (isAbcValues(ch) | ch == '-') {
                nextAbcValueInt.append(ch);
            } else if (!nextAbcValueInt.isEmpty()) {
                break;
            }
            i++;
        }

        currentIndexOfCharacterInLine = i;
        nextIntIsUsed = nextAbcValueInt.isEmpty();
    }

    public boolean hasNextAbc() {
        if (!nextIntIsUsed) {
            return true;
        } else {
            findNextAbc();
        }     
        return !nextAbcValueInt.isEmpty();  
    }

    public String nextAbc() {
        if (hasNextAbc()) {
            nextIntIsUsed = true;
            return nextAbcValueInt.toString();
        } else {
            throw new InputMismatchException();
        }
    }    

    public void moveToNextLine() {
        if (currentIndexOfCharacter == -1) {
            currentIndexOfCharacter++;
        } else {
            skipLineSeparator();
        }
    }
    private void findNextWord() throws IOException {
        nextWordValue.setLength(0);

        if (currentIndexOfCharacter >= bufferSize) {
            fillBuffer();
            currentIndexOfCharacter++;
        }



        char ch = buffer[currentIndexOfCharacter];
        while ((ch != lineSeparator.charAt(0)) & (currentIndexOfCharacter < numberOfSymbols)) {
            if (checkChar(ch)) {
                nextWordValue.append(ch);
            } else {
                if (!nextWordValue.isEmpty()) {
                    break;
                }
            }

            currentIndexOfCharacter++;
            if (currentIndexOfCharacter == bufferSize) {
                fillBuffer();
                currentIndexOfCharacter++;
            }

            ch = buffer[currentIndexOfCharacter];
        }

        nextWordIsUsed = nextWordValue.isEmpty();
    }

    public boolean hasNextWord() throws IOException {
        if (!nextWordIsUsed) {
            return true;
        } else {
            findNextWord();
        }     
        return !nextWordValue.isEmpty();  
    }

    public String nextWord() throws IOException, InputMismatchException {
        if (hasNextWord()) {
            nextWordIsUsed = true;

            return nextWordValue.toString();
        } else {
            throw new InputMismatchException();
        }
    }
}
