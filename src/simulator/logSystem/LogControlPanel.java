package simulator.logSystem;

import javax.swing.*;

/**
 * Created by ycqfeng on 2017/3/22.
 */
public class LogControlPanel {
    private JFrame frame;
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;

    private JPanel panel;

    protected LogControlPanel(){
        this.frame = new JFrame("Log Control Panel");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.panel = new JPanel();
        this.frame.add(panel);
        this.frame.setVisible(true);
    }

    protected void addButton(JButton jButton){
        this.panel.add(jButton);
        this.panel.updateUI();
    }

    public static void main(String[] args) {
        LogControlPanel logControlPanel = new LogControlPanel();

    }
}
