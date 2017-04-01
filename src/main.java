import communicationModel.packetModel.PacketCommon;
import network.*;
import simulator.Simulator;
import simulator.statistics.Statistics;

/**
 * Created by ycqfeng on 2017/3/28.
 */
public class main {
    public static void main(String[] args){
        Simulator.init();

        ControlChannel controlChannel = new ControlChannel();
        DataChannel dataChannel = new DataChannel(5);
        ControlTransceiver controlTransceiver0 = new ControlTransceiver();
        ControlTransceiver controlTransceiver1 = new ControlTransceiver();
        DataTransceiver dataTransceiver0 = new DataTransceiver();
        DataTransceiver dataTransceiver1 = new DataTransceiver();
        MediaAccessControl mac0 = new MediaAccessControl();
        MediaAccessControl mac1 = new MediaAccessControl();

        controlTransceiver0.attach(controlChannel);
        controlTransceiver1.attach(controlChannel);
        dataTransceiver0.attach(dataChannel);
        dataTransceiver1.attach(dataChannel);
        mac0.installTransceiver(controlTransceiver0);
        mac0.installTransceiver(dataTransceiver0);
        mac1.installTransceiver(controlTransceiver1);
        mac1.installTransceiver(dataTransceiver1);



        Simulator.setStopTime(10);
        Simulator.start();
        Statistics.print();
    }
}
