package Gui.Frame;

import Being.Tile;
import Being.World;
import Gui.Factory.CreatureLabelFactory;
import Gui.Label.*;
import Gui.StackException;
import Observer.Observer;
import Observer.DoubleGameObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;

public class DoubleGameFrame extends GameFrame{
    
    private int role_died_num = 0;
    protected ArrayList<DoubleBulletLabel> bulletLabels = new ArrayList<>();

    protected ArrayList<DoubleCreatureLabel> doubleCreatureLabels = new ArrayList<>();
    protected DoubleRoleLabel[] roleLabels = new DoubleRoleLabel[2];
    protected boolean end = false;

    protected Observer observer = new DoubleGameObserver(this);
    public DoubleGameFrame(String role1, String role2, int level){
        super(level);

        this.readFromTxt();

        Tile init_position = this.world.findEmpty();
        this.roleLabels[0] = new DoubleRoleLabel(role1,init_position.getX(),init_position.getY(),80,80,world);
        HpBar hp1 = new HpBar(init_position.getX() * 80,init_position.getY() * 80,10,80,Color.GREEN,roleLabels[0].getRole());
        this.roleLabels[0].setHpBar(hp1);
        this.roleLabels[0].setDoubleGameFrame(this);
        this.roleLabels[0].setIndex(0);
        jLayeredPane.add(hp1,JLayeredPane.POPUP_LAYER);
        this.observer.addRole(this.roleLabels[0]);
        this.roleLabels[0].setObserver(this.observer);
        jLayeredPane.add(this.roleLabels[0],JLayeredPane.POPUP_LAYER);

        Tile init_position2 = this.world.findEmptyExcept(init_position);
        this.roleLabels[1] = new DoubleRoleLabel(role2,init_position2.getX(),init_position2.getY(),80,80,world);
        HpBar hp2 = new HpBar(init_position2.getX() * 80,init_position2.getY() * 80,10,80,Color.GREEN,roleLabels[1].getRole());
        this.roleLabels[1].setHpBar(hp2);
        this.roleLabels[1].setDoubleGameFrame(this);
        this.roleLabels[1].setIndex(1);
        jLayeredPane.add(hp2,JLayeredPane.POPUP_LAYER);
        this.observer.addRole(this.roleLabels[1]);
        this.roleLabels[1].setObserver(this.observer);
        jLayeredPane.add(this.roleLabels[1],JLayeredPane.POPUP_LAYER);

        this.add(jLayeredPane);
    }

    public void test(){
        this.addKeyListener(new TestKeyListener());
    }

    @Override
    public void readFromTxt() {
        String filepath = "setting/monster_level_" +Integer.toString(this.level)+ ".txt";
        File file = new File(filepath);
        
        try (InputStream fin = new FileInputStream(file); ){
            StringBuffer line = new StringBuffer();
            int num = fin.available();
            for (int i = 0; i < num; i++) {
                line.append((char) fin.read());
            }
            String contents = line.toString();
            String[] lines = contents.split("\n");
            this.monster_num = lines.length;
            for (String s : lines) {
                DoubleCreatureLabel creatureLabel = (DoubleCreatureLabel)CreatureLabelFactory.createCreatureFactory(false, s, this.world);
                this.doubleCreatureLabels.add(creatureLabel);
                this.observer.addCreature(creatureLabel);
                creatureLabel.setObserver(this.observer);
                creatureLabel.setDoubleGameFrame(this);
                this.jLayeredPane.add(creatureLabel.getHpBar(), JLayeredPane.POPUP_LAYER);
                this.jLayeredPane.add(creatureLabel, JLayeredPane.POPUP_LAYER);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Stop() {
        this.stop = !(this.stop);
        for(int i = 0; i < this.doubleCreatureLabels.size(); i++){
            doubleCreatureLabels.get(i).setStop(this.stop);
        }
        this.roleLabels[0].setStop(this.stop);
        this.roleLabels[1].setStop(this.stop);
        if(this.stop){
            this.stopLabel.setVisible(true);
        }
        else{
            this.stopLabel.setVisible(false);
        }
    }

    public void MonsterStart() {
        for (CreatureLabel creatureLabel : this.doubleCreatureLabels) {
            creatureLabel.getThread().start();
        }
    }
    
    public boolean isEnd() {
        return this.end;
    }
    

    public RoleLabel getRoleLabel(int index){
        return this.roleLabels[index];
    }

    public void addBullet(DoubleBulletLabel bulletLabel){
        this.jLayeredPane.add(bulletLabel, JLayeredPane.POPUP_LAYER);
        bulletLabel.setDoubleGameFrame(this);
        this.bulletLabels.add(bulletLabel);
        bulletLabel.setObserver(this.observer);
    }

    public boolean bulletHitCreature(DoubleBulletLabel bulletLabel){
        if(bulletLabel.getLauncher()){
            for (CreatureLabel creatureLabel : doubleCreatureLabels) {
                if (creatureLabel.hitByBullet(bulletLabel)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void roleAtk(int index){
        for(int i = 0; i < this.doubleCreatureLabels.size(); i++){
            DoubleCreatureLabel doubleCreatureLabel = doubleCreatureLabels.get(i);
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

    }
}
