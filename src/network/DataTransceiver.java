package network;

import communicationModel.PhyLayer;
import communicationModel.packetModel.Packet;
import network.packets.PacketPhyData;
import simulator.Interface_Event;
import simulator.Simulator;
import simulator.logSystem.Interface_Log;
import simulator.logSystem.Log;
import simulator.statistics.Interface_Statistics;
import simulator.statistics.Statistics;

/**
 * Created by ycqfeng on 2017/3/28.
 */
public class DataTransceiver extends PhyLayer implements Interface_Log, Interface_Statistics{
    public static String NAME = "DataTransceiver";
    private MediaAccessControl mac;

    private DataChannel dataChannel;
    private int[] channelTransState;
    private int[] channelRecvState;
    private PacketPhyData sendBuffer;
    private ReceiveBufferNode[] receiveBuffers;

    private double dataRate = 1000000;//bps

    public DataTransceiver(){
        Log.enroll(this);
        Statistics.enroll(this);
    }

    public void installMAC(MediaAccessControl mac){
        this.mac = mac;
    }

    /**
     * 发送来自高层的数据包
     * @param packet 来自高层的数据包
     * @param channelIndex 发送信道
     */
    public void send(Packet packet, int channelIndex){
        Log.printlnLogicInfo(this, getDirectory()+"-> send(packet : "+packet.getStringUid()+"], channelIndex : "+channelIndex+")");
        Statistics.addRecordReceiveFromHighLayer(this, packet);
        this.sendBuffer = new PacketPhyData(packet, this);
        this.channelTransState[channelIndex] = 1;
        Statistics.addRecordSendToLowLayer(this, this.sendBuffer);
        sendBegin(channelIndex);
    }

    /**
     * 发送事件开始
     * @param channelIndex 信道
     */
    private void sendBegin(int channelIndex){
        Log.printlnLogicInfo(this, getDirectory()+"-> sendBegin(channelIndex : "+channelIndex+")");
        double duration = this.calculateTransTime(this.sendBuffer);
        this.dataChannel.send(this.sendBuffer, this, channelIndex, duration);
        Interface_Event end = new Interface_Event() {
            @Override
            public void run() {
                sendEnd(channelIndex);
            }
        };
        Simulator.addEvent(duration, end);
    }

    /**
     * 发送事件结束
     * @param channelIndex 信道
     */
    private void sendEnd(int channelIndex){
        Log.printlnLogicInfo(this, getDirectory()+"-> sendEnd(channelIndex : "+channelIndex+")");
        this.sendBuffer = null;
        this.channelTransState[channelIndex] = 0;
    }

    /**
     * 接收数据包
     * @param packet 包
     * @param channelIndex 信道索引
     * @param duration 持续时间
     * @param rxPowerDbm 接收功率
     */
    @Override
    public void receive(Packet packet, int channelIndex, double duration, double rxPowerDbm) {
        Log.printlnLogicInfo(this, getDirectory()+"-> receive(packet : "+packet.getStringUid()
                +",channelIndex : "+channelIndex+",duration : "+duration+",rxPowerDbm"+rxPowerDbm+")");
    }

    /**
     * 接收数据包
     * @param packet 包
     * @param channelIndex 信道索引
     * @param duration 持续时间
     */
    @Override
    public void receive(Packet packet, int channelIndex, double duration) {
        Log.printlnLogicInfo(this, getDirectory()+"-> receive(packet : "+packet.getStringUid()
                +",channelIndex : "+channelIndex+",duration : "+duration+")");
        PacketPhyData packetPhyData = new PacketPhyData(packet, this);
        this.addReceiveBuffer(packetPhyData, channelIndex);
        this.channelRecvState[channelIndex] = 1;
        this.receiveBegin(packetPhyData, channelIndex, duration);
    }

    /**
     * 接收开始
     * @param packet 包
     * @param channelIndex 信道索引
     * @param duration 持续时间
     */
    private void receiveBegin(PacketPhyData packet, int channelIndex, double duration){
        Log.printlnLogicInfo(this, getDirectory()+"-> receiveBegin(packet : "+packet.getStringUid()
                +",channelIndex : "+channelIndex+",duration : "+duration+")");
        Interface_Event end = new Interface_Event() {
            @Override
            public void run() {
                receiveEnd(packet, channelIndex);
            }
        };
        Simulator.addEvent(duration, end);

    }

    /**
     * 接收结束
     * @param packet 包
     * @param channelIndex 信道索引
     */
    private void receiveEnd(PacketPhyData packet, int channelIndex){
        Log.printlnLogicInfo(this, getDirectory()+"-> receiveEnd(packet : "+packet.getStringUid()
                +",channelIndex : "+channelIndex);
        if (this.removeReceiveBuffer(packet, channelIndex)){
            Statistics.addRecordDropPacket(this, packet);
        }
        else {
            if (packet.getPacket().getNAME().equals(PacketPhyData.PacketName)){
                Statistics.addRecordSendToHighLayer(this, packet.getPacket());
            }
            else {
                packet.getPacket().setDeath(this);
                Statistics.addRecordDropPacket(this, packet.getPacket());
            }
        }
        if (this.receiveBuffers[channelIndex] == null){
            this.channelRecvState[channelIndex] = 0;
        }
    }

    /**
     * 添加Packet到接收缓冲区
     * @param packet 包
     */
    private void addReceiveBuffer(PacketPhyData packet, int channelIndex){
        Log.printlnLogicInfo(this, getDirectory()+"-> addReceiveBuffer(packet : "+packet.getStringUid()+"," +
                "channelIndex : "+channelIndex+")");
        if (this.receiveBuffers[channelIndex] == null) {
            if (this.sendBuffer != null) {
                this.receiveBuffers[channelIndex] = new ReceiveBufferNode(packet, true);
            }
            else {
                this.receiveBuffers[channelIndex] = new ReceiveBufferNode(packet, false);
            }
        }
        else {
            ReceiveBufferNode tNode = this.receiveBuffers[channelIndex];
            while (tNode.next != null) {
                tNode.isCollision = true;
                tNode = tNode.next;
            }
            tNode.isCollision = true;
            tNode.next = new ReceiveBufferNode(packet, true);
        }
    }

    /**
     * 将包从接收缓冲区删除
     * @param packet 包
     * @return 是否碰撞
     */
    private boolean removeReceiveBuffer(PacketPhyData packet, int channelIndex){
        Log.printlnLogicInfo(this, getDirectory()+"-> removeReceiveBuffer(packet : "+packet.getStringUid()+"," +
                "channelIndex : "+channelIndex+")");
        boolean isCollision;
        if (this.receiveBuffers[channelIndex].packet.getUid() == packet.getUid()) {
            isCollision = this.receiveBuffers[channelIndex].isCollision;
            this.receiveBuffers[channelIndex] = this.receiveBuffers[channelIndex].next;
            return isCollision;
        }
        else {
            ReceiveBufferNode tNode = this.receiveBuffers[channelIndex];
            while (tNode.next.packet.getUid() != packet.getUid()){
                if (tNode.next == null){
                    Log.printlnErrorInfo(this, getDirectory()+"-> ReceiveBuffer删除出现错误，未找到应该删除的数据包");
                    return true;
                }
                tNode = tNode.next;
            }
            isCollision = this.receiveBuffers[channelIndex].isCollision;
            tNode.next = tNode.next.next;
            return isCollision;
        }
    }

    public void attach(DataChannel channel){
        Log.printlnLogicInfo(this, getDirectory()+"-> attach(channel : "+channel.getDirectory()+")");
        this.dataChannel = channel;
        this.channelTransState = new int[this.dataChannel.getSumChannelNum()];
        this.channelRecvState = new int[this.dataChannel.getSumChannelNum()];
        this.receiveBuffers = new ReceiveBufferNode[this.dataChannel.getSumChannelNum()];
        this.dataChannel.attach(this);
    }

    private double calculateTransTime(Packet packet){
        Log.printlnLogicInfo(this, getDirectory()+"-> calculateTransTime(packet :"+packet.getStringUid()+")");
        return packet.getSizeInBit()/this.dataRate;
    }

    public boolean isTxAble(int channelIndex){
        return (this.channelRecvState[channelIndex] == 0)
                && (this.channelTransState[channelIndex] == 0);
    }

    @Override
    public String getDirectory() {
        if (this.mac == null){
            return this.NAME+"("+getUid()+")";
        }
        else {
            return this.mac.getDirectory()+"::"+this.NAME+"("+getUid()+")";
        }
    }

    @Override
    public String getName() {
        return this.NAME;
    }

    class ReceiveBufferNode{
        ReceiveBufferNode next;
        PacketPhyData packet;
        boolean isCollision;
        ReceiveBufferNode(PacketPhyData packet){
            next = null;
            this.packet = packet;
            this.isCollision = false;
        }
        ReceiveBufferNode(PacketPhyData packet, boolean isCollision){
            this.next = null;
            this.packet = packet;
            this.isCollision = isCollision;
        }
    }

}
