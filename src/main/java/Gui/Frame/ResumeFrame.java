package Gui.Frame;

import Gui.Facade.GameFacade;

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
    private JComboBox<String> loadSelect = null;
    private JLabel title = null;
    private JButton backButton = null;
    private JButton resumeButton = null;

    static volatile private ResumeFrame instance = null;
    static public ResumeFrame getInstance() throws Exception {
        if(instance == null){
            synchronized (ResumeFrame.class){
                if(instance == null){
                    instance = new ResumeFrame();
                }
            }
        }
        else {
            instance.refresh();
        }
        return instance;
    }
    private ResumeFrame() throws Exception {
        this.setLayout(null);
        this.setResizable(false);
        this.refresh();
    }

    private void refresh() throws Exception {
        if(this.title != null){
            this.title.setVisible(false);
            this.title = null;
        }
        if(this.resumeButton != null){
            this.resumeButton.setVisible(false);
            this.resumeButton = null;
        }
        if(this.loadSelect != null){
            this.loadSelect.setVisible(false);
            this.loadSelect = null;
        }
        if(this.backButton != null){
            this.backButton.setVisible(false);
            this.backButton = null;
        }
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

        if(file_num == 0){
            this.setSize(320,500);
            title = new JLabel("No archive logs now!");
            this.add(title);
            title.setFont(new Font("Times New Roman",Font.PLAIN,32));
            title.setBounds(20,100,280,100);

            backButton = new JButton("Back");
            backButton.setFont(new Font("Times New Roman",Font.PLAIN,32));
            backButton.setBounds(100,400,100,50);
            backButton.addActionListener(new BackListener());
            this.add(backButton);
        }
        else
        {
            title = new JLabel("Please Select the Log You Want to Resume!");
            title.setBounds(20,20,260,60);
            this.add(title);
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

            resumeButton = new JButton("Resume");
            resumeButton.setFont(new Font("Times New Roman",Font.PLAIN,20));
            resumeButton.setBounds(30,160,240,50);
            resumeButton.addActionListener(new ResumeListener());
            this.add(resumeButton);
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
            GameFacade.GameResume(file);
        }
    }
}
