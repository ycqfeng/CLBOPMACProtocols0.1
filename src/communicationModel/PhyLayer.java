package communicationModel;

import communicationModel.packetModel.Packet;
import simulator.BaseObject;

/**
 * Created by ycqfeng on 2017/3/22.
 */
public abstract class PhyLayer extends BaseObject{
    private static int index = 0;
    private int uid;
    protected PhyLayer(){
        this.uid = index++;
    }
    public int getUid(){
        return this.uid;
    }
    public abstract void send(Packet packet, int channelIndex);
    public abstract void receive(Packet packet, int channelIndex, double duration, double rxPowerDbm);
    public abstract void receive(Packet packet, int channelIndex, double duration);
}
