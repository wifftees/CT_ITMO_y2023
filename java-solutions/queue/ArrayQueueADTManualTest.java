package queue;

public class ArrayQueueADTManualTest {
    public static void main(String[] args) {
        ArrayQueueADT queue1 = new ArrayQueueADT();
        ArrayQueueADT queue2 = new ArrayQueueADT();
        ArrayQueueADT.enqueue(queue1, "value");
        ArrayQueueADT.dequeue(queue1);
        ArrayQueueADT.enqueue(queue2, "value");
        ArrayQueueADT.enqueue(queue2, "value");
        ArrayQueueADT.clear(queue2);
        ArrayQueueADT.enqueue(queue2, "value1");

        while (!ArrayQueueADT.isEmpty(queue2)) {
            System.out.println(ArrayQueueADT.dequeue(queue2));
        }
    }
}
