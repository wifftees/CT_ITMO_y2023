package queue;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public abstract class AbstractQueue implements Queue {
    protected int size;
    abstract protected Object elementImpl();
    abstract public void clear();
    abstract protected void enqueueImpl(Object obj);
    abstract protected Object dequeueImpl();
    abstract protected Queue createQueue();


    @Override
    public final void enqueue(Object obj) {
        Objects.requireNonNull(obj);

        enqueueImpl(obj);
        size++;
    }

    @Override
    public final Object element() {
        requireNonEmpty();

        return elementImpl();
    }

    @Override
    public final Object dequeue() {
        requireNonEmpty();
        size--;

        return dequeueImpl();
    }


    @Override
    public final int size() {
        return size;
    }

    @Override
    public final boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public final Queue flatMap(Function<Object, List<Object>> function) {
        Queue result = createQueue();

        for (int i = 0; i < size; i++) {
            Object item = dequeue();
            List<Object> temp = function.apply(item);
            for (Object value: temp) {
                result.enqueue(value);
            }
            enqueue(item);
        }

        return result;
    }

    /*
        P: true
     */
    public void distinct() {
        Set<Object> set = new HashSet<>();
        int initialSize = size;

        for (int i = 0; i < initialSize; i++) {
            Object current = dequeue();

            if (!set.contains(current)) {
                enqueue(current);
                set.add(current);
            }
        }
    }
    /*
        Q:  ∀ x ∈ arr': ∄ y: y == x: y ∈ arr'
            &&
            ∀ x ∈ arr: ∃ y ∈ arr': x == y
            &&
            ∀ i: ∀ j: arr'[i] == arr[j] -> i <= j

            Если индексы свапнулись, то индекс элемента вырос хотя бы на один,
            значит, существует j: arr'[i] == arr[j]: i > j


            [hello, hello,  world]
                0      1       2
            [world, hello]
                0      1

     */


    protected final void requireNonEmpty() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
    }
}
