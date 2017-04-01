package communicationModel.packetModel;

/**
 * Created by ycqfeng on 2017/3/24.
 */
public abstract class PacketBase {
    public abstract int getUid();
    public abstract String getStringUid();
    public abstract int getSizeInByte();
    public abstract int getSizeInBit();
    public abstract String getStringEntityNameBirth();
    public abstract String getStringEntityNameDeath();
    public abstract int getEntityBirthUid();
    public abstract int getEntityDeathUid();
    public abstract double getTimeBirth();
    public abstract double getTimeDeath();
}
