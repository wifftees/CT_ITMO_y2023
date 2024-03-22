package queue;

public class ArrayQueueManualTest {
    public static void main(String[] args) {
        ArrayQueue queue1 = new ArrayQueue();
        ArrayQueue queue2 = new ArrayQueue();
        queue2.enqueue("value_1");
        queue2.enqueue("value_2");
        queue1.enqueue("value_1");
        queue1.enqueue("value_2");
        queue1.enqueue("value_back_1");
        System.out.println(queue1.dequeue());
        System.out.println(queue2.dequeue());
    }
}
