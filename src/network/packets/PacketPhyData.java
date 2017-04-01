package network.packets;

import communicationModel.packetModel.Packet;
import simulator.BaseObject;

/**
 * Created by ycqfeng on 2017/3/28.
 */
public class PacketPhyData extends Packet{
    public static String PacketName = "PacketPhyData";
    private int size;
    private Packet packet;

    public PacketPhyData(Packet packet, BaseObject birthPhy){
        super(birthPhy);
        this.size = packet.getSizeInByte();
        this.packet = packet;
        this.NAME = PacketName;
    }

    public void setDeathPhy(BaseObject deathPhy){
        setDeath(deathPhy);
    }

    public Packet getPacket(){
        return this.packet;
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
