package ds;

public class CustomQueue<T> {
    private CustomLinkedList<T> queue;

    public CustomQueue() {
        queue = new CustomLinkedList<>();
    }

    public void enqueue(T element) {
        queue.add(element);
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        T element = queue.get(0);
        queue.remove(0);
        return element;
    }

    public boolean isEmpty() {
        return queue.size() == 0;
    }

    public int size() {
        return queue.size();
    }
}