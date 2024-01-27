package md2html;

public class HtmlManager {
    public String createTag(char tagSymbol, int tagLength, boolean open) {
        String tagName = switch (tagSymbol) {
            case '*', '_' -> (tagLength == 2) ? "strong" : "em";
            case '`' -> "code";
            case '%' -> "var";
            default -> "s";
        };
        StringBuilder sb = new StringBuilder();
        if (open) {
            sb.append("<");
            sb.append(tagName);
            sb.append(">");
        } else {
            sb.append("</");
            sb.append(tagName);
            sb.append(">");
        }

        return sb.toString();
    }

    public String createHeaderTag(int typeOfHeader, boolean open) {
        StringBuilder sb = new StringBuilder();

        if (open) {
            sb.append("<");
        } else {
            sb.append("</");
        }

        sb.append("h");
        sb.append(typeOfHeader);
        sb.append(">");

        return sb.toString();
    }
}
