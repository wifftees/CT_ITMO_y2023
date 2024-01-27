public class Pair {
    private final int key;
    private final int value;

    public Pair(int k, int v) {
        key = k;
        value = v;
    }

    @Override
    public String toString() {
        return String.format("%d:%d", key, value);
    }

}
