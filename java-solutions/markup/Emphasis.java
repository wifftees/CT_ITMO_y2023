package markup;

import java.util.List;

public class Emphasis extends TextDecor {
    public Emphasis(List<Marking> l) {
        super(l);
    }

    public Emphasis(Marking element) {
        super(element);
    }

    public String getBBCodeTag() {
       return "i";
    }

    public String getMarkupTag() {
        return "*";
    }
}
