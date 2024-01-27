package markup;

import java.util.List;

public class Paragraph extends TextBlock {
    public Paragraph(List<Marking> list) {
        super(list);
    }

    public Paragraph(Marking element) {
        super(element);
    }
}
