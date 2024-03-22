package queue;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

/*
    Model: {
        arr[0] ... arr[n]
    }

    immutable(n, head, tail) {
        ∀ i: i = head % n, (head + 1) % n ... (tail - 1)% n: arr'[i] = arr[i]
    }
 */
public class ArrayQueueModule {
    private static int head = 0;
    private static int size = 0;
    private static Object[] arr = new Object[2];

    // P: obj != null
    public static void enqueue(final Object obj) {
        Objects.requireNonNull(obj);

        ensureCapacity();

        arr[tail()] = obj;
        size++;
    }
    /*
        Q:  && size' = size' + 1
            && arr[(head' + size') % n'] == obj
            && size' = size + 1
            && immutable(arr.length, head', head' + size')
     */

    // P: size' > 0
    public static Object element() {
        requireNonEmpty();

        return arr[head];
    }
    // Q: R != null && R = arr[head'] && immutable(arr.length, head', head' + size')

    /*
        Если в модели нет size, то здесь мы никак не можем
        заставить очередь быть непустой
     */
    // P: size' > 0
    public static Object dequeue() {
        requireNonEmpty();

        size--;
        final Object result = arr[head];
        arr[head] = null;
        head = (head + 1) % arr.length;
        return result;
    }
    /*
        Q: && R != null
           && R = arr[head']
           && head' = (head + 1) % n'
           && size' = size - 1
           && immutable(arr.length, head', head' + size')
           && arr[head'] = null
     */

    // P: true
    public static void clear() {
        size = 0;
        head = 0;
        arr = new Object[2];
    }
    /*
        && head' = 0
        && immutable(arr.length, head', head' + size') (для пустого множество выполняется все)
        && arr == new Object[2]
        && size' = 0
     */

    // P = true
    public static int size() {
        return size;
    }
    // R != null && R = size'

    // P = true
    public static boolean isEmpty() {
        return size() == 0;
    }
    // R = (size' == 0)

    // P: true
    public static int indexIf(final Predicate<Object> predicate) {
        return findIndex(predicate, true);
    }
    /*
        Q:
            temp = [
                arr[head'], arr[(head' + 1) % n'] ... arr[(head' + size' -1) % n'], arr[head' + size' % arr.length]
            ]
            R = i:
                predicate.test(temp[i]) == true
                &&
                ∀ j != i: predicate.test(temp[i]) == true: i < j
     */

    // P: true
    public static int lastIndexIf(final Predicate<Object> predicate) {
        return findIndex(predicate, false);
    }
    /*
        Q:
            temp = [arr[head'], arr[(head' + 1) % n'] ... arr[(tail' -1) % n'], arr[tail']]
            R = i:
                predicate.test(temp[i]) == true
                &&
                ∀ j != i: predicate.test(temp[i]) == true: i > j
     */

    private static int findIndex(Predicate<Object> predicate, boolean isFirstEntry) {
        int i = head;
        int result = -1;

        while (i < head + size) {
            if (predicate.test(arr[i % arr.length])) {
                result = (i - head) % arr.length;

                if (isFirstEntry) {
                    break;
                }
            }

            i++;
        }

        return result;
    }

    private static void ensureCapacity() {
        if (size + 1 <= arr.length) {
            return;
        }

        final Object[] temp = new Object[arr.length * 2];

        if (head >= tail()) {
            System.arraycopy(arr, head, temp, 0, arr.length - head);
            System.arraycopy(arr, 0, temp, arr.length - head, tail());
        } else {
            System.arraycopy(arr, 0, temp, 0, tail());
        }

        head = 0;
        arr = temp;
    }

    private static int tail() {
        return (head + size) % arr.length;
    }

    private static void requireNonEmpty() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
    }
}
