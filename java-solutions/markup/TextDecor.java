package markup;

import java.util.List;

public abstract class TextDecor implements Marking {
    protected final List<Marking> listOfElements;
    public TextDecor(List<Marking> l) {
        listOfElements = l;
    }

    public TextDecor(Marking element) {
        listOfElements = List.of(element);
    }

    protected void makeMarkupStringFromList(StringBuilder sb) {
        sb.append(getMarkupTag());
        for (Markdown element: listOfElements) {
            element.toMarkdown(sb);
        }
        sb.append(getMarkupTag());
    }

    protected void makeBBCodeStringFromList(StringBuilder sb) {
        sb.append("[").append(getBBCodeTag()).append("]");
        for (BBCode element: listOfElements) {
            element.toBBCode(sb);
        }
        sb.append("[/").append(getBBCodeTag()).append("]");
    }

    public abstract String getBBCodeTag();
    public abstract String getMarkupTag();

    @Override
    public void toMarkdown(StringBuilder sb) {
        makeMarkupStringFromList(sb);
    }

    @Override
    public void toBBCode(StringBuilder sb) {
        makeBBCodeStringFromList(sb);
    }
}
