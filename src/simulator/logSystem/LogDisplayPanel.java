package simulator.logSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ycqfeng on 2017/3/22.
 */
public class LogDisplayPanel {
    private JFrame frame;
    private static final int DEFAULT_WIDTH = 1200;
    private static final int DEFAULT_HEIGHT = 800;
    private boolean isVisible = false;

    private JPanel comboBoxPanel;
    private JPanel textAreaPanel;
    private JPanel textPanelLeft;
    private JPanel textPanelRight;
    private JTextArea allText;
    private JTextArea debugText;
    private JTextArea errorText;
    private JTextArea logicText;

    private JScrollPane allTextScrollPane;
    private JScrollPane debugTextScrollPane;
    private JScrollPane errorTextScrollPane;
    private JScrollPane logicTextScrollPane;

    private JComboBox<String> jComboBoxLeft;
    private JComboBox<String> jComboBoxRight;

    private JButton controlButton;

    protected LogDisplayPanel(String title){
        initSingle(title);
    }

    private void initDouble(String title){
        this.frame = new JFrame(title);
        //this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        this.allText = new JTextArea("All Log\n");
        this.debugText = new JTextArea("Debug Log\n");
        this.errorText = new JTextArea("Error Log\n");
        this.logicText = new JTextArea("Logic Log\n");

        this.allTextScrollPane = new JScrollPane(this.allText);
        this.debugTextScrollPane = new JScrollPane(this.debugText);
        this.errorTextScrollPane = new JScrollPane(this.errorText);
        this.logicTextScrollPane = new JScrollPane(this.logicText);

        this.jComboBoxLeft = new JComboBox<>();
        this.jComboBoxLeft.addItem("All Log");
        this.jComboBoxLeft.addItem("Debug Log");
        this.jComboBoxLeft.addItem("Error Log");
        this.jComboBoxLeft.addItem("Logic Log");
        this.jComboBoxLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectComboBoxLeft();
            }
        });

        this.jComboBoxRight = new JComboBox<>();
        this.jComboBoxRight.addItem("All Log");
        this.jComboBoxRight.addItem("Debug Log");
        this.jComboBoxRight.addItem("Error Log");
        this.jComboBoxRight.addItem("Logic Log");
        this.jComboBoxRight.setSelectedItem("Debug Log");
        this.jComboBoxRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectComboBoxRight();
            }
        });

        this.comboBoxPanel = new JPanel();
        this.comboBoxPanel.setLayout(new GridLayout(1, 2));
        this.comboBoxPanel.add(this.jComboBoxLeft);
        this.comboBoxPanel.add(this.jComboBoxRight);

        this.textPanelLeft = new JPanel();
        this.textPanelLeft.setLayout(new GridLayout(1,1));
        this.textPanelLeft.add(this.allTextScrollPane);
        this.textPanelRight = new JPanel();
        this.textPanelRight.setLayout(new GridLayout(1,1));
        this.textPanelRight.add(this.debugTextScrollPane);


        this.textAreaPanel = new JPanel();
        this.textAreaPanel.setLayout(new GridLayout(1, 2));
        this.textAreaPanel.add(this.textPanelLeft);
        this.textAreaPanel.add(this.textPanelRight);

        this.frame.add(comboBoxPanel, BorderLayout.NORTH);
        this.frame.add(textAreaPanel, BorderLayout.CENTER);
        this.frame.setVisible(this.isVisible);

    }
    private void initSingle(String title){
        this.frame = new JFrame(title);
        this.frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        this.allText = new JTextArea("All Log\n");
        this.debugText = new JTextArea("Debug Log\n");
        this.errorText = new JTextArea("Error Log\n");
        this.logicText = new JTextArea("Logic Log\n");

        this.allTextScrollPane = new JScrollPane(this.allText);
        this.debugTextScrollPane = new JScrollPane(this.debugText);
        this.errorTextScrollPane = new JScrollPane(this.errorText);
        this.logicTextScrollPane = new JScrollPane(this.logicText);

        this.jComboBoxLeft = new JComboBox<>();
        this.jComboBoxLeft.addItem("All Log");
        this.jComboBoxLeft.addItem("Debug Log");
        this.jComboBoxLeft.addItem("Error Log");
        this.jComboBoxLeft.addItem("Logic Log");
        this.jComboBoxLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectComboBoxLeft();
            }
        });

//        this.jComboBoxRight = new JComboBox<>();
//        this.jComboBoxRight.addItem("All Log");
//        this.jComboBoxRight.addItem("Debug Log");
//        this.jComboBoxRight.addItem("Error Log");
//        this.jComboBoxRight.addItem("Logic Log");
//        this.jComboBoxRight.setSelectedItem("Debug Log");
//        this.jComboBoxRight.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                selectComboBoxRight();
//            }
//        });

        this.comboBoxPanel = new JPanel();
        this.comboBoxPanel.setLayout(new GridLayout(1, 2));
        this.comboBoxPanel.add(this.jComboBoxLeft);
//        this.comboBoxPanel.add(this.jComboBoxRight);

        this.textPanelLeft = new JPanel();
        this.textPanelLeft.setLayout(new GridLayout(1,1));
        this.textPanelLeft.add(this.allTextScrollPane);
//        this.textPanelRight = new JPanel();
//        this.textPanelRight.setLayout(new GridLayout(1,1));
//        this.textPanelRight.add(this.debugTextScrollPane);


        this.textAreaPanel = new JPanel();
        this.textAreaPanel.setLayout(new GridLayout(1, 1));
        this.textAreaPanel.add(this.textPanelLeft);
//        this.textAreaPanel.add(this.textPanelRight);

        this.frame.add(comboBoxPanel, BorderLayout.NORTH);
        this.frame.add(textAreaPanel, BorderLayout.CENTER);
        this.frame.setVisible(this.isVisible);

    }
    private void selectComboBoxRight(){
        if (this.jComboBoxRight.getSelectedIndex() == 0){
            this.textPanelRight.removeAll();
            this.textPanelRight.add(this.allTextScrollPane);
            this.textPanelRight.updateUI();
            return;
        }
        if (this.jComboBoxRight.getSelectedIndex() == 1){
            this.textPanelRight.removeAll();
            this.textPanelRight.add(this.debugTextScrollPane);
            this.textPanelRight.updateUI();
            return;
        }
        if (this.jComboBoxRight.getSelectedIndex() == 2){
            this.textPanelRight.removeAll();
            this.textPanelRight.add(this.errorTextScrollPane);
            this.textPanelRight.updateUI();
            return;
        }
        if (this.jComboBoxRight.getSelectedIndex() == 3){
            this.textPanelRight.removeAll();
            this.textPanelRight.removeAll();
            this.textPanelRight.add(this.logicTextScrollPane);
            this.textPanelRight.updateUI();
            return;
        }
    }
    private void selectComboBoxLeft(){
        if (this.jComboBoxLeft.getSelectedIndex() == 0){
            this.textPanelLeft.removeAll();
            this.textPanelLeft.add(this.allTextScrollPane);
            this.textPanelLeft.updateUI();
            return;
        }
        if (this.jComboBoxLeft.getSelectedIndex() == 1){
            this.textPanelLeft.removeAll();
            this.textPanelLeft.add(this.debugTextScrollPane);
            this.textPanelLeft.updateUI();
            return;
        }
        if (this.jComboBoxLeft.getSelectedIndex() == 2){
            this.textPanelLeft.removeAll();
            this.textPanelLeft.add(this.errorTextScrollPane);
            this.textPanelLeft.updateUI();
            return;
        }
        if (this.jComboBoxLeft.getSelectedIndex() == 3){
            this.textPanelLeft.removeAll();
            this.textPanelLeft.removeAll();
            this.textPanelLeft.add(this.logicTextScrollPane);
            this.textPanelLeft.updateUI();
            return;
        }
    }

    public void appendToDebugInfo(String str){
        this.allText.append(str+" [Debug Info]\n");
        this.debugText.append(str+"\n");
    }
    public void appendToErrorInfo(String str){
        this.allText.append(str+" [Error Info]\n");
        this.errorText.append(str+"\n");
    }
    public void appendToLogicInfo(String str){
        this.allText.append(str+" [Logic Info]\n");
        this.logicText.append(str+"\n");
    }

    public void setVisible(boolean isVisible){
        this.frame.setVisible(isVisible);
    }


    public static void main(String[] args) {
        LogDisplayPanel logDisplayPanel = new LogDisplayPanel("title");
    }
}
