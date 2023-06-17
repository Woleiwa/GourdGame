package Gui.Label;

import Algorithm.Bfs;
import Being.Role;
import Being.Tile;
import Being.World;
import Gui.Factory.BulletLabelFactory;
import Gui.Frame.DoubleGameFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class DoubleCreatureLabel extends CreatureLabel{
    private DoubleGameFrame doubleGameFrame;
    private int attacker_index = -1;
    private boolean[] emit_death = new boolean[2];
    private boolean launch = false;

    public DoubleCreatureLabel(String name, int x, int y, int w, int h, World world, String direction) {
        super(name, x, y, w, h, world, direction);
        emit_death[0] = false;
        emit_death[1] = false;
    }

    public void setDoubleGameFrame(DoubleGameFrame doubleGameFrame){
        this.doubleGameFrame = doubleGameFrame;
    }

    @Override
    public boolean changeDirection() {
        if(this.cur_x % 80 != 0 || this.cur_y % 80 != 0)
        {
            return false;
        }
        if(this.detected){
            int[][] map = this.creature.getWorld().mapToInt();
            RoleLabel target = this.doubleGameFrame.getRoleLabel(this.attacker_index);
            if(target.getRole().isDead()){
                this.attacker_index = 1 - this.attacker_index;
                target = this.doubleGameFrame.getRoleLabel(this.attacker_index);
            }
            int t_x = target.get_x();
            int t_y = target.get_y();
            Bfs bfs = new Bfs(map, this.cur_x/80,this.cur_y/80, t_x, t_y);
            String temp = bfs.findDirection();
            if(temp != null) {
                this.direction = temp;
            }
            return true;
        }
        Tile cur_tile = this.creature.getCur_tile();
        int x = cur_tile.getX();
        int y = cur_tile.getY();
        if(this.direction.equals("front")) {
            if (accessible(x,y + 1)) {
            }
            else if (accessible(x,y - 1)) {
                this.direction = "back";
            }
            else if(accessible(x + 1,y)) {
                this.direction = "right";
            }
            else if(accessible(x - 1,y)) {
                this.direction = "left";
            }
        }
        else if (this.direction.equals("back")) {
            if (accessible(x,y - 1)) {
            }
            else if (accessible(x,y + 1)) {
                this.direction = "front";
            }
            else if(accessible(x + 1,y)) {
                this.direction = "right";
            }
            else if(accessible(x - 1,y)) {
                this.direction = "left";
            }
        }
        else if (this.direction.equals("right")) {
            if(accessible(x + 1,y)) {
            }
            else if(accessible(x - 1,y)) {
                this.direction = "left";
            }
            else if (accessible(x,y + 1)) {
                this.direction = "front";
            }
            else if (accessible(x,y - 1)) {
                this.direction = "back";
            }
        }
        else if (this.direction.equals("left")) {
            if(accessible(x - 1,y)) {
                this.direction = "left";
            }
            else if(accessible(x + 1,y)) {
                this.direction = "right";
            }
            else if (accessible(x,y + 1)) {
                this.direction = "front";
            }
            else if (accessible(x,y - 1)) {
                this.direction = "back";
            }
        }
        return true;
    }

    synchronized public boolean hitByBullet(DoubleBulletLabel bullet){
        boolean res = super.hitByBullet(bullet);
        if(res){
            if(attacker_index == -1) {
                this.attacker_index = bullet.getAttacker();
            }
            this.launch = true;
        }
        return res;
    }

    synchronized public void getAtk(int index){
        if(this.creature.isDead()){
            return;
        }
        this.creature.setHp(this.creature.getHp() - this.doubleGameFrame.getRoleLabel(index).getRole().getAtk());
        this.updateHpBar();
        if(!this.detected) {
            if(this.creature.getAtkMode()){
                this.StartLaunch();
            }
            else{
                this.StartAtk();
            }
            timer.cancel();
            this.timer = new Timer();
            CreatureTask ctask = new CreatureTask(this);
            timer.schedule(ctask,0,100);
        }
        this.detected = true;
        this.launch = true;
        if(this.attacker_index == -1){
            this.attacker_index = index;
        }
    }

    @Override
    public void move() {
        if(this.creature.isDead() || this.stop) {
            return;
        }
        if(this.changeDirection()) {
            if(!this.detected)
            {
                int new_x = this.creature.getCur_tile().getX();
                int new_y = this.creature.getCur_tile().getY();
                if(this.direction.equals("front")) {
                    new_y += 1;
                }
                else if(this.direction.equals("back")) {
                    new_y -= 1;
                }
                else if(this.direction.equals("right")) {
                    new_x += 1;
                }
                else if(this.direction.equals("left")) {
                    new_x -= 1;
                }
            /*System.out.println(this.name + ":");
            System.out.println(new_x);
            System.out.println(new_y);*/
                if(accessible(new_x, new_y))
                {
                    if(this.creature.getWorld().getMap()[new_x][new_y].creatureMoveToTile(this.creature)) {
                        int x = this.creature.getCur_tile().getX();
                        int y = this.creature.getCur_tile().getY();
                        this.creature.getWorld().getMap()[x][y].creatureMoveOut();
                        this.creature.setCur_tile(this.creature.getWorld().getMap()[new_x][new_y]);
                    }
                    else {
                        this.move();
                        return;
                    }
                }
                else {
                    return;
                }
            }
        }
        int speed = this.creature.getSpeed();
        if(this.detected){
            RoleLabel target = this.doubleGameFrame.getRoleLabel(attacker_index);
            if(target.getRole().isDead()){
                this.attacker_index = 1 - this.attacker_index;
                target = this.doubleGameFrame.getRoleLabel(this.attacker_index);
            }
            int t_x = target.get_x();
            int t_y = target.get_y();
            int x = this.creature.getCur_tile().getX();
            int y = this.creature.getCur_tile().getY();
            this.creature.getWorld().getMap()[x][y].creatureMoveOut();
            if(t_x == this.cur_x / 80 && t_y == this.cur_y / 80 && this.cur_x % 80 == 0 && this.cur_y % 80 == 0){
                this.index = (this.index) % 4 + 1;
                String filepath = "img/" + name +"/" + direction +"_" + Integer.toString(index) +".png";
                ImageIcon img = new ImageIcon(new ImageIcon(filepath).getImage().getScaledInstance(width,height, Image.SCALE_DEFAULT));
                this.setIcon(img);
                return;
            }
        }
        if(this.direction.equals("front")) {
            this.cur_y += speed;
        }
        else if(this.direction.equals("back")) {
            this.cur_y -= speed;
        }
        else if(this.direction.equals("right")) {
            this.cur_x += speed;
        }
        else if(this.direction.equals("left")) {
            this.cur_x -= speed;
        }
        this.index = (this.index) % 4 + 1;
        String filepath = "img/" + name +"/" + direction +"_" + Integer.toString(index) +".png";
        ImageIcon img = new ImageIcon(new ImageIcon(filepath).getImage().getScaledInstance(width,height, Image.SCALE_DEFAULT));
        this.setIcon(img);
        this.setBounds(this.cur_x, this.cur_y, this.width, this.height);
        this.hpBar.moveHp(this.cur_x,this.cur_y);
    }

    @Override
    public void attack(){
        if(this.creature.isDead() || this.stop){
            return;
        }
       this.observer.creatureNotify(this);
    }
    
    @Override
    public void launchBullet(){

        if(this.creature.isDead() || this.stop){
            return;
        }
        String[] directions = {"back","front","left","right"};
        for(int i = 0; i < directions.length; i++){
            DoubleBulletLabel bulletLabel = (DoubleBulletLabel) BulletLabelFactory.createBulletLabel(false,name,directions[i],cur_x,cur_y,this.creature.getWorld(), this.creature.getAtk(),false);
            this.doubleGameFrame.addBullet(bulletLabel);
            bulletLabel.launch();
        }
    }

    public boolean isEmitDeath(int index){
        return this.emit_death[index];
    }

    public String stringInfo(int index){
        if(this.creature.isDead()){
            this.emit_death[index] = true;
        }
        String str_direction = this.direction;
        String pic_index = Integer.toString(this.index);
        String cur_x = Integer.toString(this.cur_x);
        String cur_y = Integer.toString(this.cur_y);
        String str_hp = Integer.toString(this.creature.getHp());
        String str_launch = Boolean.toString(this.launch);
        String res = str_direction + "," + pic_index + "," + cur_x + "," + cur_y + "," + str_hp + "," + str_launch;

        return res;
    }

    public void update(String direction, String pic_index, String cur_x, String cur_y, String str_hp, String launch){
        this.index = Integer.valueOf(pic_index);
        this.direction = direction;
        this.cur_x = Integer.valueOf(cur_x);
        this.cur_y = Integer.valueOf(cur_y);
        this.creature.setHp(Integer.valueOf(str_hp));
        this.launch = Boolean.valueOf(launch);
        String filepath = "img/" + this.name +"/" + direction +"_" + Integer.toString(index) +".png";
        ImageIcon img = new ImageIcon(new ImageIcon(filepath).getImage().getScaledInstance(width,height, Image.SCALE_DEFAULT));
        this.setIcon(img);
        this.setBounds(this.cur_x, this.cur_y, width, height);
        this.hpBar.moveHp(this.cur_x, this.cur_y);
        this.updateHpBar();
    }

    public void startClientLaunch(){
        Timer clientTimer = new Timer();
        ClientBulletTask task = new ClientBulletTask(this);
        clientTimer.schedule(task,0,500);
    }

    public void clientLaunchBullet(){
        if(this.creature.isDead() || this.stop || !this.launch){
            return;
        }
        DoubleBulletLabel bulletLabel = new DoubleBulletLabel(this.name, "front", cur_x + 30, cur_y + 30, 20, 20, 10);
        this.doubleGameFrame.addBullet(bulletLabel);
        bulletLabel.setWorld(this.creature.getWorld());
        bulletLabel.setLauncher(false);
        bulletLabel.launch();

        bulletLabel = new DoubleBulletLabel(this.name, "right", cur_x + 30, cur_y + 30, 20, 20, 10);
        this.doubleGameFrame.addBullet(bulletLabel);
        bulletLabel.setWorld(this.creature.getWorld());
        bulletLabel.setLauncher(false);
        bulletLabel.launch();

        bulletLabel = new DoubleBulletLabel(this.name, "left", cur_x + 30, cur_y + 30, 20, 20, 10);
        this.doubleGameFrame.addBullet(bulletLabel);
        bulletLabel.setWorld(this.creature.getWorld());
        bulletLabel.setLauncher(false);
        bulletLabel.launch();

        bulletLabel = new DoubleBulletLabel(this.name, "back", cur_x + 30, cur_y + 30, 20, 20, 10);
        this.doubleGameFrame.addBullet(bulletLabel);
        bulletLabel.setWorld(this.creature.getWorld());
        bulletLabel.setLauncher(false);
        bulletLabel.launch();
    }
}

class ClientBulletTask extends TimerTask{

    private DoubleCreatureLabel creatureLabel;

    public ClientBulletTask(DoubleCreatureLabel creatureLabel){
        this.creatureLabel = creatureLabel;
    }

    @Override
    public void run() {
        creatureLabel.clientLaunchBullet();
    }
}