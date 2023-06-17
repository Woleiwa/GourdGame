package Gui.Frame;

import Being.Tile;
import Being.World;
import Gui.Factory.CreatureLabelFactory;
import Gui.Label.CreatureLabel;
import Gui.Label.PictureLabel;
import Gui.Label.RoleLabel;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public abstract class GameFrame extends JFrame {
    protected World world;
    protected int level;
    protected JLayeredPane jLayeredPane;

    protected boolean stop = false;
    protected JLabel stopLabel;
    protected JLabel winLabel;
    protected JLabel failLabel;
    protected int monster_num = 0;
    protected int killed_num = 0;
    protected boolean over = false;

    public GameFrame(int level){
        this.level = level;
        this.Init();
    }

    public GameFrame(String filepath){
        int index_1 = filepath.indexOf("_");
        int index_2 = filepath.indexOf("_",index_1 + 1);
        String str_level = filepath.substring(index_1 + 1, index_2);
        this.level = Integer.valueOf(str_level);
        this.Init();
    }
    public void Init(){
        jLayeredPane = new JLayeredPane();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1216,846);
        this.setTitle("Game");
        this.setResizable(false);
        this.world = new World(this.level);

        PictureLabel bg = new PictureLabel("img/game.png",0,0,1200,800);
        bg.setPreferredSize(new Dimension(1200,800));
        bg.setBounds(0, 0, 1200, 800);
        jLayeredPane.add(bg,JLayeredPane.DEFAULT_LAYER);


        this.stopLabel = new JLabel();
        ImageIcon img = new ImageIcon(new ImageIcon("img/stop.png").getImage().getScaledInstance(80,80, Image.SCALE_DEFAULT));
        this.stopLabel.setIcon(img);
        this.stopLabel.setBounds(560, 360, 80, 80);
        jLayeredPane.add(stopLabel,JLayeredPane.POPUP_LAYER);
        stopLabel.setVisible(false);

        this.winLabel = new JLabel();
        ImageIcon win_img = new ImageIcon(new ImageIcon("img/win.png").getImage().getScaledInstance(200,120, Image.SCALE_DEFAULT));
        this.winLabel.setIcon(win_img);
        this.winLabel.setBounds(500, 340, 200, 120);
        jLayeredPane.add(winLabel,JLayeredPane.POPUP_LAYER);
        winLabel.setVisible(false);

        this.failLabel = new JLabel();
        ImageIcon fail_img = new ImageIcon(new ImageIcon("img/fail.png").getImage().getScaledInstance(200,120, Image.SCALE_DEFAULT));
        this.failLabel.setIcon(fail_img);
        this.failLabel.setBounds(500, 340, 200, 120);
        jLayeredPane.add(failLabel,JLayeredPane.POPUP_LAYER);
        failLabel.setVisible(false);

        this.drawMap();
    }
    public void drawMap() {
        Tile[][] map = world.getMap();
        int width = 1200 / map.length;
        for(int i = 0; i < map.length; i++)
        {
            int height = 800 / map[i].length;
            for(int j = 0; j < map[i].length; j++)
            {
                if(map[i][j].getBeing().getName() != "Empty")
                {
                    int x = i * width;
                    int y = j * height;
                    String filepath = "img/"+map[i][j].getBeing().getName()+".png";
                    //System.out.println(filepath);
                    //PictureLabel being = new PictureLabel(filepath, x, y, width, height);
                    JLabel being =new JLabel(new ImageIcon(new ImageIcon(filepath).getImage().getScaledInstance(80,80,Image.SCALE_DEFAULT)));
                    being.setBounds(x, y, width, height);
                    being.setOpaque(true);
                    jLayeredPane.add(being,JLayeredPane.MODAL_LAYER);
                }
            }
        }
    }

    public abstract void readFromTxt();

    synchronized public void killMonster(){
        this.killed_num++;
        if(this.killed_num == this.monster_num){
            this.win();
        }
    }

    public int getLevel(){
        return this.level;
    }

    public void win() {
        winLabel.setVisible(true);
        this.Stop();
        this.over = true;
        this.stopLabel.setVisible(false);
        try {
            Thread.sleep(500);
            //StartFrame sf = new StartFrame();
            //sf.setVisible(true);
            this.setVisible(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void fail() {
        failLabel.setVisible(true);
        this.Stop();
        this.over = true;
        this.stopLabel.setVisible(false);
        try {
            Thread.sleep(500);
            //tartFrame sf = new StartFrame();
            //sf.setVisible(true);
            this.setVisible(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract void Stop();
    public Boolean isStop(){
        return this.stop;
    }
}
