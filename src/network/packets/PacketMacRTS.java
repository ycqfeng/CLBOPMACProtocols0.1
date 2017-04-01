package network.packets;

import communicationModel.addressModel.AddressMac;
import communicationModel.packetModel.Packet;
import simulator.BaseObject;

/**
 * Created by ycqfeng on 2017/3/29.
 */
public class PacketMacRTS extends Packet{
    public static String PacketName = "PacketMacRTS";
    private int size = 44;// 44 bytes
    private AddressMac source;
    private AddressMac destination;

    public PacketMacRTS(int length, BaseObject birthMac){
        super(birthMac);
        this.size = length;
        this.NAME = PacketName;
    }

    public void setSource(AddressMac source){
        this.source = source;
    }

    public void setDestination(AddressMac destination){
        this.destination = destination;
    }

    public AddressMac getSource() {
        return source;
    }

    public AddressMac getDestination() {
        return destination;
    }

    public void setDeathMac(BaseObject deathMac){
        setDeath(deathMac);
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
