package communicationModel.packetModel;

import simulator.BaseObject;
import simulator.Simulator;

/**
 * Created by ycqfeng on 2017/3/22.
 */
public abstract class Packet extends PacketBase{
    private static int index = 0;
    private int uid;
    private double timeBirth;
    private double timeDeath;
    private String entityNameBirth;
    private String entityNameDeath;
    private int entityBirthUid;
    private int entityDeathUid;
    protected String NAME;


    protected Packet(){
        this.uid = index++;
        this.timeBirth = Simulator.getCurTime();
        this.timeDeath = -1;
        this.entityNameBirth = "";
        this.entityNameDeath = "";
        this.entityBirthUid = -1;
        this.entityDeathUid = -1;
        this.NAME = "Packet";
    }

    protected Packet(BaseObject birth){
        this.uid = index++;
        this.timeBirth = Simulator.getCurTime();
        this.timeDeath = -1;
        this.entityNameBirth = birth.getName();
        this.entityNameDeath = "";
        this.entityBirthUid = birth.getUid();
        this.entityDeathUid = -1;
        this.NAME = "Packet";
    }

    public String getNAME(){
        return this.NAME;
    }

    public void setDeath(BaseObject death){
        this.timeDeath = Simulator.getCurTime();
        this.entityNameDeath = death.getName();
        this.entityDeathUid = death.getUid();
    }

    @Override
    public int getEntityBirthUid() {
        return this.entityBirthUid;
    }

    @Override
    public int getEntityDeathUid() {
        return this.entityDeathUid;
    }

    @Override
    public String getStringEntityNameBirth() {
        return this.entityNameBirth;
    }

    @Override
    public String getStringEntityNameDeath() {
        return this.entityNameDeath;
    }

    @Override
    public double getTimeBirth() {
        return this.timeBirth;
    }

    @Override
    public double getTimeDeath() {
        return this.timeDeath;
    }

    @Override
    public String getStringUid() {
        return this.NAME+"["+this.uid+"]";
    }

    @Override
    public int getUid() {
        return this.uid;
    }
}
