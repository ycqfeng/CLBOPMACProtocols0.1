package network;

import communicationModel.MacLayer;
import communicationModel.PhyLayer;
import simulator.Interface_Event;
import simulator.Simulator;
import simulator.TimeUnitValue;
import simulator.logSystem.Interface_Log;
import simulator.logSystem.Log;
import simulator.statistics.Interface_Statistics;
import simulator.statistics.Statistics;

/**
 * Created by ycqfeng on 2017/3/28.
 */
public class MediaAccessControl extends MacLayer implements Interface_Log, Interface_Statistics{
    private static String NAME = "MediaAccessControl";
    private MediaAccessControl thisMAC;
    private PhaseState state;

    private ControlTransceiver controlTransceiver;
    private DataTransceiver dataTransceiver;

    private int IFS;//0 - none、1 - SIFS、2 - DIFS
    private boolean IFSInterrupt;

    public MediaAccessControl(){
        this.thisMAC = this;
        Log.enroll(this);
        Statistics.enroll(this);
        double Tms = 9 * TimeUnitValue.us;
        double Ts = 1.89 * TimeUnitValue.ms;
        int nChannel = 3;
        this.IFS = 0;
        this.state = new PhaseState(Tms, Ts, nChannel);

    }

    /**
     * SIFS
     * 0 - none、1 - RTS、2 - CTS
     * @param behaviorAfterIFS
     */
    public void SIFSBegin(int behaviorAfterIFS){
        this.IFS = 1;
        this.IFSInterrupt = false;
        Interface_Event end = new Interface_Event() {
            @Override
            public void run() {
                SIFSEnd(behaviorAfterIFS);
            }
        };
    }

    public void SIFSEnd(int behaviorAfterIFS){
        this.IFS = 0;
    }

    public void installTransceiver(PhyLayer transceiver){
        if (transceiver.getName().equals(ControlTransceiver.NAME)){
            this.controlTransceiver = (ControlTransceiver) transceiver;
            this.controlTransceiver.installMAC(this);
        }
        if (transceiver.getName().equals(DataTransceiver.NAME)){
            this.dataTransceiver = (DataTransceiver) transceiver;
            this.dataTransceiver.installMAC(this);
        }
    }

    public void setStateParameter(double Tms, double Ts, int nChannel){
        this.state = new PhaseState(Tms, Ts, nChannel);
    }

    @Override
    public String getDirectory() {
        return NAME+"("+getUid()+")";
    }

    @Override
    public String getName() {
        return NAME;
    }

    class PhaseState{
        String ReportPhase = "ReportPhase";
        String NegotiatePhase = "NegotiatePhase";
        double Tms;
        double Ts;
        int nChannel;
        int curReportChannel;
        String state;
        PhaseState(double Tms, double Ts, int nChannel){
            this.Tms = Tms;
            this.Ts = Ts;
            this.nChannel = nChannel;
            this.curReportChannel = 0;
            this.state = ReportPhase;
            this.ReportPhaseStart();
        }
        void ReportPhaseStart(){
            Log.printlnLogicInfo(thisMAC, getDirectory()+"-> Report Phase Start. Cur = "+curReportChannel);
            Interface_Event end = new Interface_Event() {
                @Override
                public void run() {
                    ReportPhaseEnd();
                }
            };
            Simulator.addEvent(Tms, end);
        }
        void ReportPhaseEnd(){
            if (curReportChannel >= nChannel -1){
                this.state = NegotiatePhase;
                this.curReportChannel = 0;
                NegotiatePhaseStart();
            }
            else {
                this.state = ReportPhase;
                this.curReportChannel += 1;
                ReportPhaseStart();
            }
        }
        void NegotiatePhaseStart(){
            Log.printlnLogicInfo(thisMAC, getDirectory()+"-> Negotiate Phase Start.");
            Interface_Event end = new Interface_Event() {
                @Override
                public void run() {
                    NegotiatePhaseEnd();
                }
            };
            Simulator.addEvent(Ts - Tms * nChannel, end);
        }
        void NegotiatePhaseEnd(){
            this.state = ReportPhase;
            ReportPhaseStart();
        }
    }
}
