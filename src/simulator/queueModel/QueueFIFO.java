package simulator.queueModel;

/**
 * Created by ycqfeng on 2017/4/1.
 */
public class QueueFIFO<T> {
    private QueueFIFOElement<T> head;
    private QueueFIFOElement<T> tail;

    public void enqueue(T element){
        if (head == null){
            head = new QueueFIFOElement<T>(element);
            tail = head;
        }
        else {
            tail.next = new QueueFIFOElement<T>(element);
            tail = tail.next;
        }
    }

    public T dequeue(){
        if (head == null){
            return null;
        }
        else {
            QueueFIFOElement<T> t = head;
            head = head.next;
            return t.element;
        }
    }

    public boolean hasNext(){
        return head != null;
    }
}
