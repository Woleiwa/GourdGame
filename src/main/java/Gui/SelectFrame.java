package Gui;

import Network.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SelectFrame extends JFrame{

    private JLayeredPane jLayeredPane;
    private JComboBox<String> roleSelect;
    private JComboBox<String> modSelect;
    private JComboBox<String> levelSelect;

    public SelectFrame(){

        jLayeredPane = new JLayeredPane();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Select");
        this.setResizable(false);
        this.setSize(220,280);

        roleSelect = new JComboBox<>();
        JLabel title1 = new JLabel("Role");
        roleSelect.addItem("soldier");
        roleSelect.addItem("elf");
        roleSelect.addItem("fighter");

        JLabel title2 = new JLabel("Mod");
        modSelect = new JComboBox<>();
        modSelect.addItem("single");
        modSelect.addItem("double");

        JLabel title3 = new JLabel("Level");
        levelSelect = new JComboBox<>();
        levelSelect.addItem("1");
        levelSelect.addItem("2");
        levelSelect.addItem("3");

        title1.setBounds(40,20,120,20);
        roleSelect.setBounds(40,40,120,20);
        title2.setBounds(40,80,120,20);
        modSelect.setBounds(40,100,120,20);
        title3.setBounds(40,140,120,20);
        levelSelect.setBounds(40,160,120,20);

        jLayeredPane.add(title1);
        jLayeredPane.add(roleSelect,JLayeredPane.POPUP_LAYER);
        jLayeredPane.add(title2);
        jLayeredPane.add(modSelect,JLayeredPane.POPUP_LAYER);
        jLayeredPane.add(title3);
        jLayeredPane.add(levelSelect,JLayeredPane.POPUP_LAYER);

        JButton select = new JButton("Play");
        select.setBounds(40, 200,120,20);

        jLayeredPane.add(select);

        select.addActionListener(new SelectListener());
        this.add(jLayeredPane);
    }

    class SelectListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Play();
        }
    }

    public void Play(){
        int mod_cnt = modSelect.getSelectedIndex();
        int role_cnt = roleSelect.getSelectedIndex();
        String role = roleSelect.getItemAt(role_cnt);
        int level = levelSelect.getSelectedIndex() + 1;
        if(mod_cnt == 0){
            //System.out.println(level);
            GameFrame gameFrame = new GameFrame(role,level);
            gameFrame.setVisible(true);
        }
        else{
            Client client = new Client(role, level);
            try {
                client.constructConnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
