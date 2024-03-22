package queue;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

/*
    Model: {
        arr[0] ... arr[n]
    }

    immutable(n, head, tail) {
        ∀ i: i = head % n, (head + 1) % n ... tail % n: arr[i] = arr[i]
    }
 */
public class ArrayQueueADT {
    private int head = 0;
    private int size = 0;
    private Object[] arr = new Object[2];

    // P: obj != null
    public static void enqueue(ArrayQueueADT queue, final Object obj) {
        Objects.requireNonNull(obj);

        ensureCapacity(queue);

        queue.arr[tail(queue)] = obj;
        queue.size++;
    }
    /*
        Q:  && size' = size' + 1
            && arr[(head' + size') % n'] == obj
            && size' = size + 1
            && immutable(arr.length, head', head' + size')
     */

    // P: size' > 0
    public static Object element(ArrayQueueADT queue) {
        requireNonEmpty(queue);

        return queue.arr[queue.head];
    }
    // Q: R != null && R = arr[head'] && immutable(arr.length, head', head' + size')

    /*
        Если в модели нет size, то здесь мы никак не можем
        заставить очередь быть непустой
     */
    // P: size' > 0
    public static Object dequeue(ArrayQueueADT queue) {
        requireNonEmpty(queue);

        queue.size--;
        final Object result = queue.arr[queue.head];
        queue.arr[queue.head] = null;
        queue.head = (queue.head + 1) % queue.arr.length;
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
    public static void clear(ArrayQueueADT queue) {
        queue.size = 0;
        queue.head = 0;
        queue.arr = new Object[2];
    }
    /*
        && head' = 0
        && immutable(arr.length, head', head' + size') (для пустого множество выполняется все)
        && arr == new Object[2]
        && size' = 0
     */

    // P = true
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }
    // R != null && R = size'

    // P = true
    public static boolean isEmpty(ArrayQueueADT queue) {
        return size(queue) == 0;
    }
    // R = (size' == 0)

    // P: true
    public static int indexIf(ArrayQueueADT queue, final Predicate<Object> predicate) {
        return findIndex(queue, predicate, true);
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
    public static int lastIndexIf(ArrayQueueADT queue, final Predicate<Object> predicate) {
        return findIndex(queue, predicate, false);
    }
    /*
        Q:
            temp = [arr[head'], arr[(head' + 1) % n'] ... arr[(tail' -1) % n'], arr[tail']]
            R = i:
                predicate.test(temp[i]) == true
                &&
                ∀ j != i: predicate.test(temp[i]) == true: i > j
     */

    private static int findIndex(ArrayQueueADT queue, Predicate<Object> predicate, boolean isFirstEntry) {
        int i = queue.head;
        int result = -1;

        while (i < queue.head + queue.size) {
            if (predicate.test(queue.arr[i % queue.arr.length])) {
                result = (i - queue.head) % queue.arr.length;

                if (isFirstEntry) {
                    break;
                }
            }

            i++;
        }

        return result;
    }

    private static void ensureCapacity(ArrayQueueADT queue) {
        if (queue.size + 1 <= queue.arr.length) {
            return;
        }

        final Object[] temp = new Object[queue.arr.length * 2];

        if (queue.head >= tail(queue)) {
            System.arraycopy(queue.arr, queue.head, temp, 0, queue.arr.length - queue.head);
            System.arraycopy(queue.arr, 0, temp, queue.arr.length - queue.head, tail(queue));
        } else {
            System.arraycopy(queue.arr, 0, temp, 0, tail(queue));
        }

        queue.head = 0;
        queue.arr = temp;
    }

    private static int tail(ArrayQueueADT queue) {
        return (queue.head + queue.size) % queue.arr.length;
    }

    private static void requireNonEmpty(ArrayQueueADT queue) throws NoSuchElementException {
        if (isEmpty(queue)) {
            throw new NoSuchElementException("Queue is empty");
        }
    }

}
