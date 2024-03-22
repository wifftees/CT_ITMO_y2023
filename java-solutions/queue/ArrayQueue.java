package queue;

import java.util.function.Predicate;


public class ArrayQueue extends AbstractQueue {
    private int head = 0;
    private Object[] arr = new Object[2];

    public void enqueueImpl(final Object obj) {
        ensureCapacity();
        arr[tail()] = obj;
    }


    @Override
    public Object elementImpl() {
        return arr[head];
    }


    @Override
    public Object dequeueImpl() {
        final Object result = arr[head];
        arr[head] = null;
        head = (head + 1) % arr.length;
        return result;
    }


    public void clear() {
        size = 0;
        head = 0;
        arr = new Object[2];
    }


    // P: true
    public int indexIf(final Predicate<Object> predicate) {
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
    public int lastIndexIf(final Predicate<Object> predicate) {
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

    @Override
    protected Queue createQueue() {
        return new ArrayQueue();
    }

    private int findIndex(Predicate<Object> predicate, boolean isFirstEntry) {
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

    private void ensureCapacity() {
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


    private int tail() {
        return (head + size) % arr.length;
    }


}
