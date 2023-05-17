package Gui.Label;

import Being.World;
import Gui.Factory.BulletLabelFactory;
import Gui.Frame.DoubleGameFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class DoubleRoleLabel extends RoleLabel{

    private boolean killed = false;
    private DoubleGameFrame doubleGameFrame;
    private int roleIndex;

    public DoubleRoleLabel(String name, int x, int y, int w, int h, World world) {
        super(name, x, y, w, h, world);
    }

    public void setIndex(int roleIndex){
        this.roleIndex = roleIndex;
    }

    public int getIndex(){
        return this.roleIndex;
    }

    @Override
    public void launchBullet(){
        if(!launch || this.role.isDead() || this.stop){
            return;
        }
        String[] directions = {"back","front","left","right"};
        for(int i = 0; i < directions.length; i++){
            DoubleBulletLabel bulletLabel = (DoubleBulletLabel) BulletLabelFactory.createBulletLabel(false,name,directions[i],cur_x,cur_y,this.role.getWorld(), this.role.getAtk(),true);
            this.doubleGameFrame.addBullet(bulletLabel);
            bulletLabel.setAttacker(this.roleIndex);
            bulletLabel.launch();
        }
    }

    @Override
    public void attack(){
        if (!this.launch ||this.role.isDead() || this.stop) {
            return;
        }
        if(this.launch){
            this.doubleGameFrame.roleAtk(this.roleIndex);
            //System.out.println("I attacked!");
        }
    }

    public void setDoubleGameFrame(DoubleGameFrame doubleGameFrame){
        this.doubleGameFrame = doubleGameFrame;
    }

    @Override
    synchronized public void updateHp(){
        if(this.role.isDead() && ! this.killed){
            this.killed = true;
            this.doubleGameFrame.killRole();
            this.setVisible(false);
        }
        this.hpBar.upDateHpBar();
    }

   
    public String stringInfo(){
        String res = "role";
        String str_roleIndex = Integer.toString(this.roleIndex);
        String str_direction = this.direction;
        String pic_index = Integer.toString(this.index);
        String cur_x = Integer.toString(this.cur_x);
        String cur_y = Integer.toString(this.cur_y);
        String str_hp = Integer.toString(this.getRole().getHp());
        String str_launch = Boolean.toString(this.launch);
        res = res + "," + str_roleIndex + "," + str_direction + "," + pic_index + "," + cur_x + "," + cur_y + "," + str_hp + "," + str_launch;
        return res;
    }

    public void update(String direction, String pic_index, String cur_x, String cur_y, String str_hp, String launch){
        this.index = Integer.valueOf(pic_index);
        this.direction = direction;
        this.cur_x = Integer.valueOf(cur_x);
        this.cur_y = Integer.valueOf(cur_y);
        this.getRole().setHp(Integer.valueOf(str_hp));
        String filepath = "img/" + this.name +"/" + direction +"_" + Integer.toString(index) +".png";
        ImageIcon img = new ImageIcon(new ImageIcon(filepath).getImage().getScaledInstance(width,height, Image.SCALE_DEFAULT));
        this.setIcon(img);
        this.setBounds(this.cur_x, this.cur_y, width, height);
        this.hpBar.moveHp(this.cur_x, this.cur_y);
        this.launch = Boolean.valueOf(launch);
        //System.out.println(this.roleIndex + ":" + this.launch);
        this.updateHp();
    }

    public void clientLaunchBullet(){
        if(this.role.isDead() || this.stop || !this.launch){
            return;
        }
        DoubleBulletLabel bulletLabel1 = new DoubleBulletLabel(this.name, "back", cur_x + 30, cur_y + 30, 20, 20, 10);
        bulletLabel1.setAttacker(this.roleIndex);
        this.doubleGameFrame.addBullet(bulletLabel1);
        bulletLabel1.setWorld(this.role.getWorld());
        //bulletLabel1.setAtk(this.role.getAtk());
        bulletLabel1.setLauncher(true);
        bulletLabel1.launch();

        DoubleBulletLabel bulletLabel2 = new DoubleBulletLabel(this.name, "front", cur_x + 30, cur_y + 30, 20, 20, 10);
        bulletLabel2.setAttacker(this.roleIndex);
        this.doubleGameFrame.addBullet(bulletLabel2);
        bulletLabel2.setWorld(this.role.getWorld());
        //bulletLabel2.setAtk(this.role.getAtk());
        bulletLabel2.setLauncher(true);
        bulletLabel2.launch();

        DoubleBulletLabel bulletLabel3 = new DoubleBulletLabel(this.name, "left", cur_x + 30, cur_y + 30, 20, 20, 10);
        bulletLabel3.setAttacker(this.roleIndex);
        this.doubleGameFrame.addBullet(bulletLabel3);
        bulletLabel3.setWorld(this.role.getWorld());
        //bulletLabel3.setAtk(this.role.getAtk());
        bulletLabel3.setLauncher(true);
        bulletLabel3.launch();

        DoubleBulletLabel bulletLabel4 = new DoubleBulletLabel(this.name, "right", cur_x + 30, cur_y + 30, 20, 20, 10);
        bulletLabel4.setAttacker(this.roleIndex);
        this.doubleGameFrame.addBullet(bulletLabel4);
        bulletLabel4.setWorld(this.role.getWorld());
        //bulletLabel4.setAtk(this.role.getAtk());
        bulletLabel4.setLauncher(true);
        bulletLabel4.launch();
    }

    public void startClientLaunch(){
        java.util.Timer clientTimer = new Timer();
        RoleClientBulletTask task = new RoleClientBulletTask(this);
        clientTimer.schedule(task,10,500);
    }//试验时用的代码，由于信息统一度不高被弃用
}

class RoleClientBulletTask extends TimerTask {

    private DoubleRoleLabel roleLabel;

    public RoleClientBulletTask(DoubleRoleLabel roleLabel){
        this.roleLabel = roleLabel;
    }

    @Override
    public void run() {
        roleLabel.clientLaunchBullet();
    }
}