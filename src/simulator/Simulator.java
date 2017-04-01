package simulator;

import simulator.logSystem.Interface_Log;
import simulator.logSystem.Log;
import simulator.statistics.Statistics;

/**
 * Created by ycqfeng on 2017/3/7.
 */
public class Simulator implements Interface_Log {
    private static Simulator simulator;
    private Event queueHead;
    private Event curEvent;
    private double curTime;
    private double stopTime;
    private boolean FinishEvent;

    /**
     * 仿真器初始化，所有初始化操作全在这里
     */
    public static void init(){
        simulator = new Simulator();
        simulator.curTime = 0;
        simulator.stopTime = 0;
        simulator.FinishEvent = false;

        Log.init(Simulator.simulator);

        Log.enroll(simulator);
        Statistics.init();
    }

    /**
     * 获取仿真器当前时间
     * @return
     */
    public static double getCurTime(){
        return simulator.curTime;
    }

    /**
     * 设置仿真器停止仿真时间
     * @param stopTime
     */
    public static void setStopTime(double stopTime) {
        simulator.stopTime = stopTime;
    }

    /**
     * 根据uid查找队列中的事件
     * @param uid
     * @return
     */
    public static Event getEvent(int uid){
        Event event = simulator.queueHead;
        while (event != null){
            if (event.getUid() == uid){
                return event;
            }
            event = event.getNext();
        }
        return event;
    }

    /**
     * 取消事件
     * @param event 已经在队列中的事件
     * @return
     */
    private static boolean cancel(Event event){
        if (simulator.queueHead == event){
            simulator.queueHead = event.getNext();
        }
        event.leaveAlone();
        return true;

    }

    /**
     * 取消事件
     * @param uid
     * @return
     */
    public static boolean cancel(int uid){
        Event event = getEvent(uid);
        if (event == null){
            return false;
        }
        else {
            return cancel(event);
        }

    }

    /**
     * 添加一个事件到队列，并正确排序
     * @param root 根
     * @param node 新事件
     * @return
     */
    private boolean addEvent(Event root, Event node){
        if (root.isEarlier(node)){
            root.addToLast(node);
            this.queueHead = root.getHead();
            return true;
        }
        else {
            if (root.getNext() == null){
                root.addToNext(node);
                return true;
            }
            else {
                return addEvent(root.getNext(), node);
            }
        }

    }

    /**
     * 添加一个事件
     * @param event 事件
     * @return 是否添加成功
     */
    public static boolean addEvent(Event event){
        if (event == null){
            return false;
        }
        if (simulator.queueHead == null){
            simulator.queueHead = event;
            return true;
        }
        else {
            return simulator.addEvent(simulator.queueHead, event);
        }
    }

    /**
     * 根据间隔时间，接口添加事件
     * @param interTime 事件执行间隔时间
     * @param interfaceEvent 事件接口
     * @return
     */
    public static int addEvent(double interTime, Interface_Event interfaceEvent){
        Event event = new Event();
        event.setTimeExecute(getCurTime()+interTime);
        event.setInterface(interfaceEvent);
        if (addEvent(event)){
            return event.getUid();
        }
        else {
            return -1;
        }
    }

    /**
     * 仿真器开始运行
     */
    public static void start(){
        class EventEnd implements Interface_Event{
            @Override
            public void run(){
                simulator.FinishEvent = true;
                Log.printlnLogicInfo(simulator, "Simulator is ending.");
            }
        }
        if (simulator.stopTime != 0){
            EventEnd eventEnd = new EventEnd();
            addEvent(simulator.stopTime, eventEnd);
        }
        simulator.execute();
    }

    /**
     * 判断仿真器是否应该停止
     * @return
     */
    private boolean isFinish(){
        if (FinishEvent){
            return true;
        }
        if (queueHead == null){
            return true;
        }
        return false;
    }

    /**
     * 仿真器开始执行事件
     */
    public void execute(){
        while (!isFinish()){
            curEvent = queueHead;
            simulator.curTime = curEvent.getTimeExecute();
            queueHead = queueHead.getNext();
            curEvent.leaveAlone();
            curEvent.run();
        }
    }

    @Override
    public String getDirectory() {
        return "Simulator";
    }
}