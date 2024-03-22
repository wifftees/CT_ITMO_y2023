package queue;

import java.util.List;
import java.util.function.Function;

/*
    Model: {
        arr[0] ... arr[n]
    }

    immutable(head, tail) {
        ∀ i: i = head, (head + 1) ... tail: arr'[i] = arr[i]
    }
 */
public interface Queue {

    // P: obj != null
    void enqueue(Object obj);
    /*
        Q:  && size' = size' + 1
            && arr[(head' + size') % n'] == obj
            && size' = size + 1
            && immutable(arr.length, head', head' + size')
     */

    // P: size' > 0
    Object element();
    // Q: R != null && R = arr[head'] && immutable(arr.length, head', head' + size')


    /*
        Если в модели нет size, то здесь мы никак не можем
        заставить очередь быть непустой
     */
    // P: size' > 0
    Object dequeue();
    /*
        Q: && R != null
           && R = arr[head']
           && head' = (head + 1) % n'
           && size' = size - 1
           && immutable(arr.length, head', head' + size')
           && arr[head'] = null
     */

    // P = true
    int size();
    // R != null && R = size'

    // P = true
    boolean isEmpty();
    // R = (size' == 0)

    // P: true
    void clear();
    /*
        && head' = 0
        && immutable(arr.length, head', head' + size') (для пустого множество выполняется все)
        && arr == new Object[2]
        && size' = 0
     */

    Queue flatMap(Function<Object, List<Object>> function);
    void distinct();
}


