package markup;

import java.util.List;

public abstract class TextBlock implements Marking {

    protected final List<Marking> listOfElements;

    public TextBlock(List<Marking> list) {
        listOfElements = list;
    }

    public TextBlock(Marking element) {
        listOfElements = List.of(element);
    }

    @Override

    public void toMarkdown(StringBuilder sb) {
        for (Marking element: listOfElements) {
            element.toMarkdown(sb);
        }
    }

    @Override

    public void toBBCode(StringBuilder sb) {
        for (Marking element: listOfElements) {
            element.toBBCode(sb);
        }
    }
}
