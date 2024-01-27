package md2html;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class TextManager {
    private static final Set<Character> specialSymbols = new HashSet<>(List.of('*', '-', '_', '`', '%'));
    private static class Pair {
        private final boolean flag;
        private final int value;

        public Pair(boolean f, int v) {
            flag = f;
            value = v;
        }

        public boolean getFlag() {
            return flag;
        }

        public int getValue() {
            return value;
        }
    }
    public boolean isHeader(String line) {
       if (line.charAt(0) != '#') {
           return false;
       }
       int i = 0;
       char ch;
       while ((ch = line.charAt(i)) == '#') {
           i++;
       }
       return (ch == ' ');
    }

    public int headerType(String line) {
        int i = 0;
        char ch = line.charAt(i);
        while (ch == '#') {
            i++;
            ch = line.charAt(i);
        }
        return i;
    }

    private int getTagLength(int start, String currentLine) {
        char tagSymbol = currentLine.charAt(start);
        int tagLength = 1;

        if (start + 1 < currentLine.length() && currentLine.charAt(start + 1) == tagSymbol) {
            tagLength++;
        }

        return tagLength;
    }

    private boolean isShielded(int start, String currentLine) {
        return (start - 1) >= 0 && currentLine.charAt(start - 1) == '\\';
    }

    private boolean isOpenTag(int start, int numberOfLine, List<String> paragraph) {
        String currentLine = paragraph.get(numberOfLine);
        char tagSymbol = currentLine.charAt(start);
        int tagLength = getTagLength(start, currentLine);

        if (isShielded(start, currentLine)) {
            return false;
        }

        for (int i = start + tagLength; i < currentLine.length(); i++) {
            char ch = currentLine.charAt(i);

            if (ch == tagSymbol && (i-1) >= 0 && currentLine.charAt(i - 1) != '\\') {
                return true;
            }
        }

        for (int i = numberOfLine + 1; i < paragraph.size(); i++) {
            String line = paragraph.get(i);

            for (int j = 0; j < line.length(); j++) {
                char ch = line.charAt(j);

                if (ch == tagSymbol && (j - 1) >= 0 && line.charAt(j - 1) != '\\') {
                    return true;
                }
            }
        }

        return false;
    }

    private Pair decideTag(char ch, boolean flag, boolean openTag, int tagLength, StringBuilder sb, HtmlManager htmlManager) {
        if (!flag && openTag) {
            sb.append(htmlManager.createTag(ch, tagLength, true));
            return new Pair(true, tagLength - 1);
        } else if (flag) {
            sb.append(htmlManager.createTag(ch, tagLength, false));
            return new Pair(false, tagLength - 1);
        } else {
            sb.append(ch);
            return new Pair(false, tagLength - 1);
        }
    }


    public void processParagraphText(
            int startOfFirstLine,
            List<String> paragraph,
            StringBuilder sb,
            HtmlManager htmlManager
    ) {
        boolean emphasisFlag = false;
        boolean strongFlag = false;
        boolean strikeOutFlag = false;
        boolean codeFlag = false;
        boolean varFlag = false;

        for (int i = 0; i < paragraph.size(); i++) {
            String line = paragraph.get(i);

            for (int j = startOfFirstLine; j < line.length(); j++) {
                char ch = line.charAt(j);
                Pair pair;

                if (specialSymbols.contains(ch) && !isShielded(j, line)) {
                    int tagLength = getTagLength(j, line);
                    boolean openTag = isOpenTag(j, i, paragraph);

                    switch (ch) {
                        case '*', '_':
                            if (tagLength > 1) {
                                pair = decideTag(ch, strongFlag, openTag, tagLength, sb, htmlManager);
                                strongFlag = pair.getFlag();
                                j += pair.getValue();
                            } else {
                                pair = decideTag(ch, emphasisFlag, openTag, tagLength, sb, htmlManager);
                                emphasisFlag = pair.getFlag();
                                j += pair.getValue();
                            }
                            break;
                        case '-':
                            pair = decideTag(ch, strikeOutFlag, openTag, tagLength, sb, htmlManager);
                            strikeOutFlag = pair.getFlag();
                            j += pair.getValue();
                            break;
                        case '%':
                            pair = decideTag(ch, varFlag, openTag, tagLength, sb, htmlManager);
                            varFlag = pair.getFlag();
                            j += pair.getValue();
                            break;
                        default:
                            pair = decideTag(ch, codeFlag, openTag, tagLength, sb, htmlManager);
                            codeFlag = pair.getFlag();
                            j += pair.getValue();
                    }
                } else {
                    switch (ch) {
                        case '<':
                            sb.append("&lt;");
                            break;
                        case '>':
                            sb.append("&gt;");
                            break;
                        case '&':
                            sb.append("&amp;");
                            break;
                        case '\\':
                            if (
                                    j + 1 < line.length() && !specialSymbols.contains(line.charAt(j + 1))
                            ) {
                                sb.append(ch);
                            }
                            break;
                        default:
                            sb.append(ch);
                    }
                }
            }
            startOfFirstLine = 0;
        }
    }
}
