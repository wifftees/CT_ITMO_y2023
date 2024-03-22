package queue;

public class LinkedQueue extends AbstractQueue {
    private Node head = null;
    private Node tail = null;

    @Override
    public void enqueueImpl(Object obj) {
        Node currentNode = tail;
        tail = new Node(obj, null);

        if (head != null) {
            currentNode.next = tail;
        } else {
            head = tail;
        }
    }

    @Override
    public Object elementImpl() {
        return head.value;
    }

    @Override
    public Object dequeueImpl() {
        Object result = head.value;
        head = head.next;

        return result;
    }

    @Override
    public void clear() {
        size = 0;
        head = null;
        tail = null;
    }

    @Override
    protected Queue createQueue() {
        return new LinkedQueue();
    }
    private static class Node {
        private Node next;
        private final Object value;
        public Node(Object value, Node next) {
            this.value = value;
            this.next = next;
        }
    }
}
