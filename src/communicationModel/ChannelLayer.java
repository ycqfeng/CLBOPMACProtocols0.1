package communicationModel;

import communicationModel.packetModel.Packet;
import simulator.BaseObject;

/**
 * Created by ycqfeng on 2017/3/22.
 */
public abstract class ChannelLayer extends BaseObject{
    private static int index = 0;
    private int uid;
    protected ChannelLayer(){
        this.uid = index++;
    }
    public int getUid(){
        return this.uid;
    }
    public abstract void send(Packet packet, PhyLayer phyLayer, int channelIndex, double duration, double txPowerDbm);
    public abstract void send(Packet packet, PhyLayer phyLayer, int channelIndex, double duration);
}
