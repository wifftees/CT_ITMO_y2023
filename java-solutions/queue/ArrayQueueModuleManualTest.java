package queue;

import static queue.ArrayQueueModule.*;

public class ArrayQueueModuleManualTest {
    public static void main(String[] args) {
        enqueue("adf");
        enqueue("asd");
        dequeue();
        enqueue("sdffd");
        clear();
        enqueue("sdf");
        enqueue("sdffd");
        System.out.println(dequeue());
        System.out.println(dequeue());
    }


}
