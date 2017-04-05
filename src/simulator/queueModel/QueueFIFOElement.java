package simulator.queueModel;

/**
 * Created by ycqfeng on 2017/4/1.
 */
public class QueueFIFOElement<T> {
    protected T element;
    protected QueueFIFOElement<T> next;
    protected QueueFIFOElement(T element){
        this.element = element;
        this.next = null;
    }
}
