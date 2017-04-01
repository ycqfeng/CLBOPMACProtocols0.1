package simulator.logSystem;

import simulator.Simulator;
import simulator.TimeUnit;
import java.text.DecimalFormat;

/**
 * Created by ycqfeng on 2017/3/7.
 */
public class Log {
    private static Log log;
    private LogRecordNode recordNodes;
    private LogControlPanel controlPanel;
    private static DecimalFormat timeFormatilize = new DecimalFormat("0.000000000");
    private Simulator simulator;

    /**
     * 设置时间输出格式
     * @param timeResolution
     */
    public static void setTimeResolution(TimeUnit timeResolution){
        switch (timeResolution){
            case s:
                timeFormatilize.applyPattern("0");
                break;
            case ms:
                timeFormatilize.applyPattern("0.000");
                break;
            case us:
                timeFormatilize.applyPattern("0.000000");
                break;
            case ns:
                timeFormatilize.applyPattern("0.000000000");
                break;
            case ps:
                timeFormatilize.applyPattern("0.000000000000");
                break;
            default:
                break;
        }
    }

    /**
     * 注册接口
     * @param interfaceLog
     */
    public static void enroll(Interface_Log interfaceLog){
        log.register(interfaceLog);
    }

    /**
     * 获取接口节点
     * @param interfaceLog
     * @return
     */
    private LogRecordNode getLogNode(Interface_Log interfaceLog){
        LogRecordNode tNode = this.recordNodes;
        while (tNode != null){
            if (tNode.interfaceLog == interfaceLog){
                return tNode;
            }
            tNode = tNode.next;
        }
        return null;
    }

    /**
     * 注册接口
     * @param interfaceLog
     */
    public void register(Interface_Log interfaceLog){
        if (this.recordNodes == null){
            this.recordNodes = new LogRecordNode(interfaceLog);
            this.controlPanel.addButton(this.recordNodes.getControlButton());
        }
        else {
            if (getLogNode(interfaceLog) != null){
                return;
            }
            LogRecordNode tNode = new LogRecordNode(interfaceLog);
            tNode.next = this.recordNodes;
            this.recordNodes = tNode;
            this.controlPanel.addButton(tNode.getControlButton());
        }
    }

    /**
     * 初始化
     */
    public static void init(Simulator simulator){
        log = new Log();
        log.controlPanel = new LogControlPanel();
        log.simulator = simulator;
    }

    /**
     * 获取当前时间String，并标准化输出
     * @return
     */
    public static String getCurTime(){
        String str = "";
        str += log.timeFormatilize.format(Simulator.getCurTime())+"s, ";
        return str;
    }

    /**
     * 输出错误信息
     * @param interfaceLog
     * @param str
     * @return
     */
    public static boolean printlnErrorInfo(Interface_Log interfaceLog, String str){
        LogRecordNode tNode = log.getLogNode(interfaceLog);
        if (tNode == null){
            return false;
        }
        tNode.printlnErrorInfo(getCurTime()+str);
        if (interfaceLog != Log.log.simulator){
            printlnErrorInfo(Log.log.simulator, str);
        }
        return true;
    }

    /**
     * 输出逻辑信息
     * @param interfaceLog
     * @param str
     * @return
     */
    public static boolean printlnLogicInfo(Interface_Log interfaceLog, String str){
        LogRecordNode tNode = log.getLogNode(interfaceLog);
        if (tNode == null){
            return false;
        }
        tNode.printlnLogicInfo(getCurTime()+str);
        if (interfaceLog != Log.log.simulator){
            printlnLogicInfo(Log.log.simulator, str);
        }
        return true;
    }

    /**
     * 输出调试信息
     * @param interfaceLog
     * @param str
     * @return
     */
    public static boolean printlnDebugInfo(Interface_Log interfaceLog, String str) {
        LogRecordNode tNode = log.getLogNode(interfaceLog);
        if (tNode == null){
            return false;
        }
        tNode.printlnDebugInfo(getCurTime()+str);
        if (interfaceLog != Log.log.simulator){
            printlnDebugInfo(Log.log.simulator, str);
        }
        return true;
    }
}
