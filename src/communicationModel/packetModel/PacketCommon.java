package communicationModel.packetModel;

/**
 * Created by ycqfeng on 2017/3/25.
 */
public class PacketCommon extends Packet{
    private int size;
    public PacketCommon(){
        this.size = 0;
    }
    public PacketCommon(int sizeInByte){
        this.size = sizeInByte;
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
