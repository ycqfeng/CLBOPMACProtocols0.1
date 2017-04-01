package simulator;

/**
 * Created by ycqfeng on 2017/3/7.
 */
public class Event {
    //序号
    private static int index = 0;
    private int uid;
    //时间
    private double timeExecute;
    //相连节点
    private Event next;
    private Event last;
    //接口
    private Interface_Event interfaceEvent;

    public Event(){
        this.uid = index++;
    }

    public void run(){
        interfaceEvent.run();
    }

    public void setInterface(Interface_Event interfaceEvent){
        this.interfaceEvent = interfaceEvent;
    }

    public Event getNext(){
        return this.next;
    }

    public Event getHead(){
        Event event = this;
        while (event.last != null) {
            event = event.last;
        }
        return event;
    }

    public boolean isEarlier(Event event){
        if (event.timeExecute < this.timeExecute){
            return true;
        }
        else {
            return false;
        }
    }

    public void setTimeExecute(double timeExecute){
        this.timeExecute = timeExecute;
    }

    public double getTimeExecute(){
        return this.timeExecute;
    }
    public int getUid(){
        return this.uid;
    }

    public void addToLast(Event event){
        if (this.last == null){
            this.last = event;
            this.last.next = this;
            return;
        }
        else {
            event.last = this.last;
            this.last.next = event;
            event.next = this;
            this.last = event;
            return;
        }
    }

    public void addToNext(Event event){
        if (this.next == null){
            this.next = event;
            this.next.last = this;
            return;
        }
        else{
            event.next = this.next;
            this.next.last = event;
            this.next = event;
            event.last = this;
            return;
        }
    }
    public void leaveAlone(){
        if (this.next != null) {
            this.next.last = this.last;
        }
        if (this.last != null) {
            this.last.next = this.next;
        }
        this.last = null;
        this.next = null;
    }
}