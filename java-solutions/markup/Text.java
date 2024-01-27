package markup;

public class Text implements Marking {
    protected final String value;

    public Text(String v) {
        super();
        value = v;
    }
    @Override
    public void toMarkdown(StringBuilder sb) {
        sb.append(value);
    }

    @Override
    public void toBBCode(StringBuilder sb) { sb.append(value); }
}
