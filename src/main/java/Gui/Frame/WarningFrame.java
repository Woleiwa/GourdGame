package Gui.Frame;

import javax.swing.*;

public class WarningFrame extends JFrame {
    String warning;
    JLayeredPane jLayeredPane;
    public WarningFrame(String warning){
        this.warning = warning;
        int width = this.warning.length() * 7;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Warning");
        this.setResizable(false);
        this.setSize(width + 80,120);
        JLabel warningLabel = new JLabel(this.warning);

        warningLabel.setBounds(40,20,width,80);
        jLayeredPane = new JLayeredPane();
        jLayeredPane.add(warningLabel,jLayeredPane.POPUP_LAYER);
        this.add(jLayeredPane);
        this.setVisible(true);
    }
}
