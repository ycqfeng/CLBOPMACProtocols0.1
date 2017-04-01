package simulator.statistics;

/**
 * Created by ycqfeng on 2017/3/23.
 */
public class StatisticsRecordNode {
    protected StatisticsRecordNode next;
    protected Interface_Statistics interfaceStatistics;
    //Unit In Byte
    protected int numReceiveFromHighLayer;
    protected int numSendToHighLayer;
    protected int numSendToLowLayer;
    protected int numReceiveFromLowLayer;
    protected int numDrop;

    protected int sizeReceiveFromHighLayer;
    protected int sizeSendToHighLayer;
    protected int sizeSendToLowLayer;
    protected int sizeReceiveFromLowLayer;
    protected int sizeDrop;

    protected StatisticsRecordNode(Interface_Statistics interface_statistics){
        this.next = null;
        this.interfaceStatistics = interface_statistics;
        this.numReceiveFromHighLayer = 0;
        this.numSendToHighLayer = 0;
        this.numSendToLowLayer = 0;
        this.numReceiveFromLowLayer = 0;
        this.numDrop = 0;
        this.sizeReceiveFromHighLayer = 0;
        this.sizeSendToHighLayer = 0;
        this.sizeSendToLowLayer = 0;
        this.sizeReceiveFromLowLayer = 0;
        this.sizeDrop = 0;
    }

    protected void printRecord(){
        String name = interfaceStatistics.getName();
        int uid = interfaceStatistics.getUid();
        name = name + "(" + uid + ")";
        System.out.println("\n========================="+name+"=========================");
        System.out.println(name+" receive from high layer : "+numReceiveFromHighLayer+" Packets");
        System.out.println(name+" send to high layer : "+numSendToHighLayer+" Packets");
        System.out.println(name+" receive from low layer : "+numReceiveFromLowLayer+" Packets");
        System.out.println(name+" send to low layer : "+numSendToLowLayer+" Packets");
        System.out.println(name+" drop : "+numDrop+" Packets");
        System.out.println("--------------------------------------------------");
        System.out.println(name+" receive from high layer : "+sizeReceiveFromHighLayer+" Bytes");
        System.out.println(name+" send to high layer : "+sizeSendToHighLayer+" Bytes");
        System.out.println(name+" receive from low layer : "+sizeReceiveFromLowLayer+" Bytes");
        System.out.println(name+" send to low layer : "+sizeSendToLowLayer+" Bytes");
        System.out.println(name+" drop : "+sizeDrop+" Bytes");
        System.out.println("--------------------------------------------------");
    }

}
