package Gui.Frame;

import Gui.Label.PictureLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class StartFrame extends JFrame {
    public StartFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,600);
        this.setTitle("Start");
        this.setLayout(null);
        this.setResizable(false);

        JButton resume_button = new JButton("Resume");
        resume_button.setFont(new Font("Times New Roman",Font.PLAIN,32));
        resume_button.setBounds(300, 350,200, 50);
        this.add(resume_button);

        JButton start_button = new JButton("Start");
        start_button.setFont(new Font("Times New Roman",Font.PLAIN,32));
        start_button.setBounds(300, 250,200, 50);
        this.add(start_button);

        PictureLabel bg = new PictureLabel("img/background.png",0,0, 800,600);
        bg.setPreferredSize(new Dimension(800,600));
        bg.setBounds(0, 0, 800, 600);
        this.add(bg);

        start_button.addActionListener(new StartListener());
        resume_button.addActionListener(new ResumeListener());
    }

    class StartListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            Start();
        }
    }

    class ResumeListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            Resume();
        }
    }

    public void Start() {
        SelectFrame sf = new SelectFrame();
        sf.setVisible(true);
    }

    public void Resume(){
        ResumeFrame rf = null;
        try {
            rf = new ResumeFrame();
        } catch (Exception e) {
            e.printStackTrace();
        }
        rf.setVisible(true);
    }

}
