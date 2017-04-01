package network;

import communicationModel.PhyLayer;
import communicationModel.packetModel.Packet;
import network.packets.PacketPhyControl;
import simulator.Interface_Event;
import simulator.Simulator;
import simulator.logSystem.Interface_Log;
import simulator.logSystem.Log;
import simulator.statistics.Interface_Statistics;
import simulator.statistics.Statistics;

/**
 * Created by ycqfeng on 2017/3/28.
 */
public class ControlTransceiver extends PhyLayer implements Interface_Log, Interface_Statistics{
    public static String NAME = "ControlTransceiver";
    private MediaAccessControl mac;
    private ControlChannel controlChannel;
    private int channelTransState;//0 - 空闲、 1 - 发送
    private int channelRecvState;//0 - 空闲、 1 - 发送
    private PacketPhyControl sendBuffer;
    private ReceiveBufferNode receiveBuffer;

    private double dataRate = 1000000;//bps

    public ControlTransceiver(){
        Log.enroll(this);
        Statistics.enroll(this);
    }

    public void installMAC(MediaAccessControl mac) {
        this.mac = mac;
    }

    public void attach(ControlChannel controlChannel){
        Log.printlnLogicInfo(this, getDirectory()+"-> attach(channel : "+controlChannel.getDirectory()+")");
        this.controlChannel = controlChannel;
        this.channelTransState = 0;
        this.channelRecvState = 0;
        controlChannel.attach(this);
    }

    /**
     * 发送来自高层的数据包
     * @param packet 来自高层的数据包
     * @param channelIndex 发送信道
     */
    @Override
    public void send(Packet packet, int channelIndex) {
        Log.printlnLogicInfo(this, getDirectory()+"-> send(packet : "+packet.getStringUid()+"], channelIndex : "+channelIndex+")");
        Statistics.addRecordReceiveFromHighLayer(this, packet);
        this.sendBuffer = new PacketPhyControl(packet, this);
        this.channelTransState = 1;
        sendBegin(channelIndex);
    }
    /**
     * 发送事件开始
     * @param channelIndex 信道
     */
    private void sendBegin(int channelIndex){
        Log.printlnLogicInfo(this, getDirectory()+"-> sendBegin(channelIndex : "+channelIndex+")");
        Statistics.addRecordSendToLowLayer(this, this.sendBuffer);
        double duration = this.calculateTransTime(this.sendBuffer);
        this.controlChannel.send(this.sendBuffer, this, channelIndex, duration);
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
        this.channelTransState = 0;
    }

    @Override
    public void receive(Packet packet, int channelIndex, double duration, double rxPowerDbm) {

    }

    @Override
    public void receive(Packet packet, int channelIndex, double duration) {
        Log.printlnLogicInfo(this, getDirectory()+"-> receive(packet : "+packet.getStringUid()
                +",channelIndex : "+channelIndex+",duration : "+duration+")");
        PacketPhyControl packetPhyControl;
        if (packet.getNAME().equals(PacketPhyControl.PacketName)){
            packetPhyControl = (PacketPhyControl) packet;
        }
        else {
            Log.printlnLogicInfo(this, getDirectory()+"-> receive(packet : "+packet.getStringUid()
                    +",channelIndex : "+channelIndex+",duration : "+duration+")");
            Log.printlnErrorInfo(this, getDirectory()+"-> 参数packet不是packetPhyControl数据包");
            return;
        }
        this.addReceiveBuffer(packetPhyControl, channelIndex);
        this.channelRecvState = 1;
        Statistics.addRecordReceiveFromLowLayer(this, packetPhyControl);
        this.receiveBegin(packetPhyControl, channelIndex, duration);
    }

    /**
     * 接收开始
     * @param packet 包
     * @param channelIndex 信道索引
     * @param duration 持续时间
     */
    private void receiveBegin(PacketPhyControl packet, int channelIndex, double duration){
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
    private void receiveEnd(PacketPhyControl packet, int channelIndex){
        Log.printlnLogicInfo(this, getDirectory()+"-> receiveEnd(packet : "+packet.getStringUid()
                +",channelIndex : "+channelIndex);
        if (this.removeReceiveBuffer(packet, channelIndex)){
            Statistics.addRecordDropPacket(this, packet);
        }
        else {
            Statistics.addRecordSendToHighLayer(this, packet.getPacket());
        }
        if (this.receiveBuffer == null){
            this.channelRecvState = 0;
        }
    }

    /**
     * 计算发送时间
     * @param packet 数据包
     * @return 发送时间
     */
    public double calculateTransTime(Packet packet){
        Log.printlnLogicInfo(this, getDirectory()+"-> calculateTransTime(packet :"+packet.getStringUid()+")");
        return packet.getSizeInBit()/this.dataRate;
    }

    /**
     * 添加Packet到接收缓冲区
     * @param packet 包
     */
    private void addReceiveBuffer(PacketPhyControl packet, int channelIndex){
        Log.printlnLogicInfo(this, getDirectory()+"-> addReceiveBuffer(packet : "+packet.getStringUid()+"," +
                "channelIndex : "+channelIndex+")");
        if (this.receiveBuffer == null){
            if (this.sendBuffer != null){
                this.receiveBuffer = new ReceiveBufferNode(packet, true);
            }
            else {
                this.receiveBuffer = new ReceiveBufferNode(packet, false);
            }
        }
        else {
            ReceiveBufferNode tNode = this.receiveBuffer;
            while (tNode.next != null){
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
    private boolean removeReceiveBuffer(PacketPhyControl packet, int channelIndex){
        Log.printlnLogicInfo(this, getDirectory()+"-> removeReceiveBuffer(packet : "+packet.getStringUid()+"," +
                "channelIndex : "+channelIndex+")");
        boolean isCollision = false;
        if (this.receiveBuffer.packet.getUid() == packet.getUid()) {
            isCollision = this.receiveBuffer.isCollision;
            this.receiveBuffer = this.receiveBuffer.next;
            return isCollision;
        }
        else {
            ReceiveBufferNode tNode = this.receiveBuffer;
            while (tNode.next.packet.getUid() != packet.getUid()){
                if (tNode.next == null){
                    Log.printlnErrorInfo(this, getDirectory()+"-> ReceiveBuffer删除出现错误，未找到应该删除的数据包");
                    return true;
                }
                tNode = tNode.next;
            }
            isCollision = this.receiveBuffer.isCollision;
            tNode.next = tNode.next.next;
            return isCollision;
        }
    }

    public boolean isTxAble(){
        return (this.channelTransState == 0) && (this.channelRecvState == 0);
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
        return NAME;
    }
    class ReceiveBufferNode{
        ReceiveBufferNode next;
        PacketPhyControl packet;
        boolean isCollision;
        ReceiveBufferNode(PacketPhyControl packet){
            next = null;
            this.packet = packet;
            this.isCollision = false;
        }
        ReceiveBufferNode(PacketPhyControl packet, boolean isCollision){
            this.next = null;
            this.packet = packet;
            this.isCollision = isCollision;
        }
    }
}
