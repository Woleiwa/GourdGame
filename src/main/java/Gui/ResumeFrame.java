package Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;


public class ResumeFrame extends JFrame {
    private final String dir = "save";
    private File file;
    private ArrayList<String> file_list = new ArrayList<>();
    private int file_num = 0;
    private JComboBox<String> loadSelect;
    public ResumeFrame() throws Exception {
         file = new File(dir);
         if(isDir()){
             String[] list = file.list();
             for(int i = 0; i < list.length; i++){
                 String filepath = dir + "/" + list[i];
                 System.out.println(filepath);
                 file_list.add(filepath);
                 file_num++;
             }
         }
         else {
             throw new Exception("Not a directory!");
         }
         this.setLayout(null);
        this.setResizable(false);
         if(file_num == 0){
             this.setSize(320,500);
             JLabel warning = new JLabel("No archive logs now!");
             this.add(warning);
             warning.setFont(new Font("Times New Roman",Font.PLAIN,32));
             warning.setBounds(20,100,280,100);

             JButton button = new JButton("Back");
             button.setFont(new Font("Times New Roman",Font.PLAIN,32));
             button.setBounds(100,400,100,50);
             button.addActionListener(new BackListener());
             this.add(button);
         }
         else
         {
             JLabel tile = new JLabel("Please Select the Log You Want to Resume!");
             tile.setBounds(20,20,260,60);
             this.add(tile);
             this.setSize(320,300);

             loadSelect = new JComboBox<>();
             for(int i = 0; i < file_list.size(); i++){
                 String item = file_list.get(i);
                 int[] index = new int[8];
                 index[0] = item.indexOf("_");
                 for(int j = 1; j < 7; j++){
                     index[j] = item.indexOf("_",index[j - 1] + 1);
                 }
                 index[7] = item.indexOf(".txt");
                 String[] str = new String[7];
                 for(int j = 0; j < 7; j++){
                     str[j] = item.substring(index[j] + 1, index[j + 1]);
                 }
                 String line = "level:" + str[0] + " " + str[1] + "." + str[2] + "." + str[3] + " " + str[4] + ":" + str[5] + ":" + str[6];
                 loadSelect.addItem(line);
             }
             loadSelect.setBounds(20,100,260,30);
             this.add(loadSelect);

             JButton button = new JButton("Resume");
             button.setFont(new Font("Times New Roman",Font.PLAIN,20));
             button.setBounds(30,160,240,50);
             button.addActionListener(new ResumeListener());
             this.add(button);
         }
    }

    public boolean isDir(){
        return file.isDirectory();
    }

    public int getFile_num(){
        return this.file_num;
    }

    class BackListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
    }

    class ResumeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int index = loadSelect.getSelectedIndex();
            String file = file_list.get(index);
            GameFrame gameframe = new GameFrame(file);
            gameframe.setVisible(true);
        }
    }
}
