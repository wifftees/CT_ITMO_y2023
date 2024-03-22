package queue;

public class LinkedQueueManualTest {
    public static void main(String[] args) {
        Queue queue = new LinkedQueue();
        queue.enqueue("Hello");
        queue.enqueue("World");
        queue.enqueue("!");
        queue.dequeue();
        queue.clear();
        queue.enqueue("Hello");
        queue.enqueue("World");
        queue.enqueue("!");
        dump(queue);
    }

    public static void dump(Queue queue) {
        while (!queue.isEmpty()) {
            System.out.println(queue.dequeue());
        }
    }
}
