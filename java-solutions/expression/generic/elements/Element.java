package expression.generic.elements;

public interface Element<T> {
    T evaluate(int x, int y, int z);
}
