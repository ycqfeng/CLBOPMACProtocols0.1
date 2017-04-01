package network;

import communicationModel.ChannelLayer;
import communicationModel.PhyLayer;
import communicationModel.packetModel.Packet;
import simulator.logSystem.Interface_Log;
import simulator.logSystem.Log;

/**
 * Created by ycqfeng on 2017/3/28.
 */
public class DataChannel extends ChannelLayer implements Interface_Log{
    private static String NAME = "DataChannel";
    private int sumChannelNum;
    private PhyNode phyNodes;

    public DataChannel(int sumChannelNum){
        this.sumChannelNum = sumChannelNum;
        Log.enroll(this);
    }

    public int getSumChannelNum() {
        return sumChannelNum;
    }

    @Override
    public void send(Packet packet, PhyLayer sender, int channelIndex, double duration, double txPowerDbm) {

    }

    @Override
    public void send(Packet packet, PhyLayer sender, int channelIndex, double duration) {
        Log.printlnLogicInfo(this, getDirectory()+"-> send(packet : "+packet.getStringUid()
                +",sender : "+getDirectory()+",channelIndex : "+channelIndex+",duration : "+ duration+")");
        if (channelIndex >= this.sumChannelNum){
            Log.printlnErrorInfo(this, getDirectory()+"-> 信道号超出范围");
            return;
        }
        PhyNode tPhyNode = this.phyNodes;
        while (tPhyNode != null){
            if (tPhyNode.uid != sender.getUid()){
                //执行接收动作
                tPhyNode.phyLayer.receive(packet, channelIndex, duration);
            }
            tPhyNode = tPhyNode.next;
        }
    }

    /**
     * 连接物理层
     * @param phyLayer 物理层
     */
    public void attach(PhyLayer phyLayer){
        if (isExist(phyLayer)) {
            return;
        }
        if (phyNodes == null){
            phyNodes = new PhyNode(phyLayer);
        }
        else {
            PhyNode tPhyNode = new PhyNode(phyLayer);
            tPhyNode.next = phyNodes;
            phyNodes = tPhyNode;
        }
    }

    /**
     * 检查是否存在
     * @param phyLayer 物理层
     * @return 是否存在
     */
    public boolean isExist(PhyLayer phyLayer){
        PhyNode tPhyNode = phyNodes;
        while (tPhyNode != null){
            if (tPhyNode.uid == phyLayer.getUid()){
                return true;
            }
            tPhyNode = tPhyNode.next;
        }
        return false;
    }

    @Override
    public String getDirectory() {
        return NAME+"("+getUid()+")";
    }

    @Override
    public String getName() {
        return NAME;
    }

    class PhyNode {
        PhyNode next;
        PhyLayer phyLayer;
        int uid;
        PhyNode(PhyLayer phyLayer){
            this.next = null;
            this.phyLayer = phyLayer;
            this.uid = phyLayer.getUid();
        }
    }
}
