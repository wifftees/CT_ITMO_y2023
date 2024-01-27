package markup;

import java.util.List;

public class Strong extends TextDecor {
    public Strong(List<Marking> l) {
        super(l);
    }

    public Strong(Marking element) {
        super(element);
    }

    @Override
    public void toBBCode(StringBuilder sb) {
        super.makeBBCodeStringFromList(sb);
    }

    @Override
    public String getBBCodeTag() {
        return "b";
    }

    @Override
    public String getMarkupTag() {
        return "__";
    }


}