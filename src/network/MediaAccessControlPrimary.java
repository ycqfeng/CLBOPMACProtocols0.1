package network;

import communicationModel.MacLayer;
import simulator.Interface_Event;
import simulator.Simulator;
import simulator.TimeUnitValue;
import simulator.logSystem.Interface_Log;
import simulator.logSystem.Log;
import simulator.statistics.Interface_Statistics;
import simulator.statistics.Statistics;

/**
 * Created by ycqfeng on 2017/4/1.
 */
public class MediaAccessControlPrimary extends MacLayer implements Interface_Log, Interface_Statistics{
    private static String NAME = "MediaAccessControlPrimary";
    private MediaAccessControlPrimary thisMac;
    private DataTransceiver dataTransceiver;

    public MediaAccessControlPrimary(){
        this.thisMac = this;
        Log.enroll(this);
        Statistics.enroll(this);
        double Ts = 1.89 * TimeUnitValue.ms;
    }

    @Override
    public String getDirectory() {
        return NAME+"("+getUid()+")";
    }

    @Override
    public String getName() {
        return NAME;
    }

    class PhaseState {
        double Ts;
        int nChannel;

        PhaseState(double Ts, int nChannel) {
            this.Ts = Ts;
            this.nChannel = nChannel;
        }

        void SlotStart(){
            Log.printlnLogicInfo(thisMac, getDirectory()+"-> Slot Start.");
            Interface_Event end = new Interface_Event() {
                @Override
                public void run() {
                    SlotEnd();
                }
            };
            Simulator.addEvent(Ts, end);
        }
        void SlotEnd(){
            Log.printlnLogicInfo(thisMac, getDirectory()+"-> Slot End.");
            Interface_Event start = new Interface_Event() {
                @Override
                public void run() {
                    SlotStart();
                }
            };
            Simulator.addEvent(0, start);
        }
    }
}
