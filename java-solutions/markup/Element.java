package markup;

public abstract class Element implements Marking {
    public abstract void toMarkdown(StringBuilder sb);
    public abstract void toBBCode(StringBuilder sb);
}
