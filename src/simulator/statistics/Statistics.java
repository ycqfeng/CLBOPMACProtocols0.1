package simulator.statistics;

import communicationModel.packetModel.Packet;
import simulator.logSystem.Interface_Log;
import simulator.logSystem.Log;

/**
 * Created by ycqfeng on 2017/3/23.
 */
public class Statistics implements Interface_Log{
    private static Statistics statistics;
    private StatisticsRecordNode recordNodes;

    private Statistics(){}

    public static void init(){
        statistics = new Statistics();
        Log.enroll(statistics);
    }

    /**
     * 注册一个接口
     * @param interface_statistics 接口
     */
    public static void enroll(Interface_Statistics interface_statistics){
        if (statistics.isExist(interface_statistics)){
            return;
        }
        if (statistics.recordNodes == null){
            statistics.recordNodes = new StatisticsRecordNode(interface_statistics);
        }
        else {
            StatisticsRecordNode tNode = new StatisticsRecordNode(interface_statistics);
            tNode.next = statistics.recordNodes;
            statistics.recordNodes = tNode;
        }
    }

    public static void addRecordReceiveFromHighLayer(Interface_Statistics interface_statistics, Packet packet){
        if (!statistics.isExist(interface_statistics)){
            Log.printlnErrorInfo(statistics, statistics.getDirectory()+"-> 无法对未注册节点进行统计");
            return;
        }
        StatisticsRecordNode tNode = statistics.getRecordNodes(interface_statistics);
        tNode.numReceiveFromHighLayer += 1;
        tNode.sizeReceiveFromHighLayer += packet.getSizeInByte();
    }

    public static void addRecordSendToHighLayer(Interface_Statistics interface_statistics, Packet packet){
        if (!statistics.isExist(interface_statistics)){
            Log.printlnErrorInfo(statistics, statistics.getDirectory()+"-> 无法对未注册节点进行统计");
            return;
        }
        StatisticsRecordNode tNode = statistics.getRecordNodes(interface_statistics);
        tNode.numSendToHighLayer += 1;
        tNode.sizeSendToHighLayer += packet.getSizeInByte();
    }

    public static void addRecordReceiveFromLowLayer(Interface_Statistics interface_statistics, Packet packet){
        if (!statistics.isExist(interface_statistics)){
            Log.printlnErrorInfo(statistics, statistics.getDirectory()+"-> 无法对未注册节点进行统计");
            return;
        }
        StatisticsRecordNode tNode = statistics.getRecordNodes(interface_statistics);
        tNode.numReceiveFromLowLayer += 1;
        tNode.sizeReceiveFromLowLayer += packet.getSizeInByte();
    }

    public static void addRecordSendToLowLayer(Interface_Statistics interface_statistics, Packet packet){
        if (!statistics.isExist(interface_statistics)){
            Log.printlnErrorInfo(statistics, statistics.getDirectory()+"-> 无法对未注册节点进行统计");
            return;
        }
        StatisticsRecordNode tNode = statistics.getRecordNodes(interface_statistics);
        tNode.numSendToLowLayer += 1;
        tNode.sizeSendToLowLayer += packet.getSizeInByte();
    }

    public static void addRecordDropPacket(Interface_Statistics interface_statistics, Packet packet){
        if (!statistics.isExist(interface_statistics)){
            Log.printlnErrorInfo(statistics, statistics.getDirectory()+"-> 无法对未注册节点进行统计");
            return;
        }
        StatisticsRecordNode tNode = statistics.getRecordNodes(interface_statistics);
        tNode.numDrop += 1;
        tNode.sizeDrop += packet.getSizeInByte();
    }

    public static void print(){
        StatisticsRecordNode tNode = statistics.recordNodes;
        while (tNode != null){
            tNode.printRecord();
            tNode = tNode.next;
        }
    }

    /**
     * 获取接口记录节点
     * @param interface_statistics 接口
     * @return 记录节点
     */
    private StatisticsRecordNode getRecordNodes(Interface_Statistics interface_statistics){
        StatisticsRecordNode tNode = this.recordNodes;
        while (tNode != null){
            if (tNode.interfaceStatistics.getName().equals(interface_statistics.getName())
                    && (tNode.interfaceStatistics.getUid() == interface_statistics.getUid())){
                return tNode;
            }
            tNode = tNode.next;
        }
        return null;
    }

    /**
     * 判断是否存在接口
     * @param interface_statistics 接口
     * @return 是否存在
     */
    private boolean isExist(Interface_Statistics interface_statistics){
        StatisticsRecordNode tNode = this.recordNodes;
        while (tNode != null){
            if (tNode.interfaceStatistics.getName().equals(interface_statistics.getName())
                    && (tNode.interfaceStatistics.getUid() == interface_statistics.getUid())){
                return true;
            }
            tNode = tNode.next;
        }
        return false;
    }

    @Override
    public String getDirectory() {
        return "Statistics";
    }
}
