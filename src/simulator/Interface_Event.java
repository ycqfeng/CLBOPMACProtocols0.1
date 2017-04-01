package simulator;

/**
 * Created by ycqfeng on 2017/3/22.
 */
public interface Interface_Event {
    default void run(){
        System.out.println("function run() is not defined.");
    }
}
