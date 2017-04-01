package simulator.logSystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ycqfeng on 2017/3/22.
 */
public class LogRecordNode {
    protected LogRecordNode next;
    private LogDisplayPanel displayPanel;
    protected Interface_Log interfaceLog;
    private JButton controlButton;
    private boolean isVisible;

    protected LogRecordNode(Interface_Log interface_log){
        this.interfaceLog = interface_log;
        this.next = null;
        this.isVisible = false;
        this.displayPanel = new LogDisplayPanel(interface_log.getDirectory());
        this.controlButton = new JButton(interface_log.getDirectory());
        this.controlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible();
            }
        });
    }

    private void setVisible(){
        displayPanel.setVisible(!isVisible);
        this.isVisible = !this.isVisible;
    }

    protected JButton getControlButton(){
        return this.controlButton;
    }

    protected void printlnDebugInfo(String string){
        this.displayPanel.appendToDebugInfo(string);
    }
    protected void printlnErrorInfo(String string){
        this.displayPanel.appendToErrorInfo(string);
    }
    protected void printlnLogicInfo(String string){
        this.displayPanel.appendToLogicInfo(string);
    }
}
