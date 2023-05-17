package Gui.Frame;

import Being.Tile;
import Being.World;
import Gui.Factory.CreatureLabelFactory;
import Gui.Label.*;
import Gui.StackException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;

public class DoubleGameFrame extends JFrame{
    private World world;
    private int level;
    protected JLayeredPane jLayeredPane;
    protected ArrayList<DoubleCreatureLabel> creatureLabels = new ArrayList<>();
    public boolean stop = false;
    protected JLabel stopLabel;
    protected JLabel winLabel;
    protected JLabel failLabel;
    private int monster_num = 0;
    private int killed_num = 0;
    private int role_died_num = 0;
    protected ArrayList<DoubleBulletLabel> bulletLabels = new ArrayList<>();
    protected DoubleRoleLabel[] roleLabels = new DoubleRoleLabel[2];
    protected boolean over = false;
    protected boolean end = false;

    public DoubleGameFrame(String role1, String role2, int level){
        jLayeredPane = new JLayeredPane();
        this.level = level;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1216,846);
        this.setTitle("Game Level" + this.level);
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
        this.readFromTxt();

        Tile init_position = this.world.findEmpty();
        this.roleLabels[0] = new DoubleRoleLabel(role1,init_position.getX(),init_position.getY(),80,80,world);
        HpBar hp1 = new HpBar(init_position.getX() * 80,init_position.getY() * 80,10,80,Color.GREEN,roleLabels[0].getRole());
        this.roleLabels[0].setHpBar(hp1);
        this.roleLabels[0].setDoubleGameFrame(this);
        this.roleLabels[0].setIndex(0);
        jLayeredPane.add(hp1,JLayeredPane.POPUP_LAYER);
        jLayeredPane.add(this.roleLabels[0],JLayeredPane.POPUP_LAYER);

        Tile init_position2 = this.world.findEmptyExcept(init_position);
        this.roleLabels[1] = new DoubleRoleLabel(role2,init_position2.getX(),init_position2.getY(),80,80,world);
        HpBar hp2 = new HpBar(init_position2.getX() * 80,init_position2.getY() * 80,10,80,Color.GREEN,roleLabels[1].getRole());
        this.roleLabels[1].setHpBar(hp2);
        this.roleLabels[1].setDoubleGameFrame(this);
        this.roleLabels[1].setIndex(1);
        jLayeredPane.add(hp2,JLayeredPane.POPUP_LAYER);
        jLayeredPane.add(this.roleLabels[1],JLayeredPane.POPUP_LAYER);

        this.add(jLayeredPane);
    }

    public void test(){
        this.addKeyListener(new TestKeyListener());
    }

    public void drawMap() {
        Tile[][] map = world.getMap();
        int width = 1200 / map.length;
        for(int i = 0; i < map.length; i++)
        {
            int height = 800 / map[i].length;
            for(int j = 0; j < map[i].length; j++)
            {
                if(!map[i][j].getBeing().getName().equals("Empty"))
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

    public void readFromTxt() {
        String filepath = "setting/monster_level_" +Integer.toString(this.level)+ ".txt";
        File file = new File(filepath);
        InputStream fin = null;
        try {
            fin = new FileInputStream(file);
            StringBuffer line = new StringBuffer();
            int num = fin.available();
            for (int i = 0; i < num; i++) {
                line.append((char) fin.read());
            }
            String contents = line.toString();
            String[] lines = contents.split("\n");
            this.monster_num = lines.length;
            for(int i = 0; i < lines.length; i++) {
                DoubleCreatureLabel creatureLabel =  (DoubleCreatureLabel) CreatureLabelFactory.createCreatureFactory(false,lines[i],this.world);
                this.creatureLabels.add(creatureLabel);
                creatureLabel.setDoubleGameFrame(this);
                this.jLayeredPane.add(creatureLabel.getHpBar(), JLayeredPane.POPUP_LAYER);
                this.jLayeredPane.add(creatureLabel, JLayeredPane.POPUP_LAYER);
            }

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void MonsterStart() {
        for(int i = 0; i < this.creatureLabels.size(); i++) {
            this.creatureLabels.get(i).getThread().start();
        }
    }

    public void Stop(){
        this.stop = !(this.stop);
        for(int i = 0; i < this.creatureLabels.size(); i++){
            creatureLabels.get(i).setStop(this.stop);
        }
        roleLabels[0].setStop(this.stop);
        roleLabels[1].setStop(this.stop);
        if(this.stop){
            this.stopLabel.setVisible(true);
        }
        else{
            this.stopLabel.setVisible(false);
        }
    }

    public void win() {
        this.end = true;
        winLabel.setVisible(true);
        this.Stop();
        this.stopLabel.setVisible(false);
        try {
            Thread.sleep(500);
            //StartFrame sf = new StartFrame();
            //sf.setVisible(true);
            this.over = true;
            this.setVisible(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void fail() {
        this.end = true;
        failLabel.setVisible(true);
        this.Stop();
        this.stopLabel.setVisible(false);
        try {
            Thread.sleep(500);
            //tartFrame sf = new StartFrame();
            //sf.setVisible(true);
            this.over = true;
            this.setVisible(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isEnd() {
        return this.end;
    }

    synchronized public void killMonster(){
        this.killed_num++;
        if(this.killed_num == this.monster_num){
            this.win();
        }
    }

    public RoleLabel getRoleLabel(int index){
        return this.roleLabels[index];
    }

    public void addBullet(DoubleBulletLabel bulletLabel){
        this.jLayeredPane.add(bulletLabel, JLayeredPane.POPUP_LAYER);
        bulletLabel.setDoubleGameFrame(this);
        this.bulletLabels.add(bulletLabel);
    }

    public boolean bulletHitCreature(DoubleBulletLabel bulletLabel){
        if(bulletLabel.getLauncher()){
            for(int i = 0; i < creatureLabels.size(); i++){
                if(creatureLabels.get(i).hitByBullet(bulletLabel)){
                    return true;
                }
            }
        }
        return false;
    }

    public void roleAtk(int index){
        for(int i = 0; i < this.creatureLabels.size(); i++){
            DoubleCreatureLabel doubleCreatureLabel = creatureLabels.get(i);
            int dx = roleLabels[index].get_x() - doubleCreatureLabel.get_x();
            int dy = roleLabels[index].get_y() - doubleCreatureLabel.get_y();
            int distance = dx * dx + dy * dy;
            if(distance <= 1){
                doubleCreatureLabel.getAtk(index);
            }
        }
    }

    synchronized public void killRole(){
        this.role_died_num++;
        if(this.role_died_num == 2){
            this.fail();
        }
    }

    class TestKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            char code = e.getKeyChar();
            TypeAction(0,code);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            char code = e.getKeyChar();
            PressAction(0, code);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            char code = e.getKeyChar();
            ReleaseAction(0, code);
        }
    }//only for test


    public boolean getOver(){return this.over;}

    public void TypeAction(int index, char code){
        if(code == 'w' || code == 'a' || code == 's' || code == 'd') {
            roleLabels[index].startMove();
        }
    }

    public void PressAction(int index, char code){
        if(code == 'w' || code == 'a' || code == 's' || code == 'd') {
            roleLabels[index].setDirection(code);
        }
        else if(code == 'j'){
            roleLabels[index].setLaunch(true);
        }
    }

    public void ReleaseAction(int index, char code){
        if(code == 'w' || code == 'a' || code == 's' || code == 'd') {
            try {
                roleLabels[index].endMove(code);
            } catch (StackException stackException) {
                stackException.printStackTrace();
            }
        }
        else if(code == 'j'){
            roleLabels[index].setLaunch(false);
        }
        else if(code == 'q'){
            Stop();
        }
    }
}
