package network.packets;

import communicationModel.packetModel.Packet;
import simulator.BaseObject;

/**
 * Created by ycqfeng on 2017/3/31.
 */
public class PacketMacCTS extends Packet{
    public static String PacketName = "PacketMacCTS";
    private int size = 38;

    public PacketMacCTS(int length, BaseObject birthMac){
        super(birthMac);
        this.size = length;
        this.NAME = PacketName;
    }

    @Override
    public int getSizeInByte() {
        return this.size;
    }

    @Override
    public int getSizeInBit() {
        return this.size * 8;
    }
}
