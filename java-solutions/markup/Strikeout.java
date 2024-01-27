package markup;

import java.util.List;

public class Strikeout extends TextDecor {
    public Strikeout(List<Marking> l) {
        super(l);
    }

    public Strikeout(Marking element) {
        super(element);
    }

    @Override
    public String getBBCodeTag() {
        return "s";
    }

    @Override
    public String getMarkupTag() {
        return "~";
    }

}
