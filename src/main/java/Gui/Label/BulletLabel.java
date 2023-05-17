package Gui.Label;

import Being.Tile;
import Being.World;
import Gui.Frame.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class BulletLabel extends JLabel{
    private String name;
    protected String direction;
    protected int x,y,width,height;
    protected Timer timer;
    protected int speed;
    protected World world;
    protected boolean launcher;
    protected int atk = 0;
    private GameFrame gameFrame;
    protected boolean able = true;

    public BulletLabel(String name, String direction, int x, int y, int width, int height, int speed){
        this.name = name;
        this.direction = direction;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        String filepath = "img/bullet/" + this.name + "/" + this.direction + ".png";
        ImageIcon img = new ImageIcon(new ImageIcon(filepath).getImage().getScaledInstance(width,height, Image.SCALE_DEFAULT));
        this.setIcon(img);
        this.setBounds(this.x, this.y, width, height);
        this.setOpaque(false);
    }

    public void move() {
        if(this.gameFrame.stop || this.able == false){
            //System.out.println("I can't move!");
            return;
        }
        if(this.direction.startsWith("front")){
            y += this.speed;
        }
        else if(this.direction.startsWith("back")) {
            y -= this.speed;
        }
        else if(this.direction.startsWith("left")) {
            x -= this.speed;
        }
        else if(this.direction.startsWith("right")) {
            x += this.speed;
        }
        if(y < 0 || y + height > 800 || x < 0 || x + width > 1200){
             this.setVisible(false);
             this.able = false;
             this.timer.cancel();
             return;
        }
        else if(this.blockedByWall()) {
            this.setVisible(false);
            this.able = false;
            this.timer.cancel();
            return;
        }
        else if(this.launcher && this.gameFrame.bulletHitCreature(this)){
            this.setVisible(false);
            this.able = false;
            this.timer.cancel();
            return;
        }
        else if(!this.launcher && this.gameFrame.getRoleLabel().hitByBullet(this)){
            this.setVisible(false);
            this.able = false;
            this.timer.cancel();
            return;
        }
        this.setBounds(this.x, this.y, width, height);
    }

    public void launch(){
        this.timer = new Timer();
        TimerTask bulletTask = new BulletTask(this);
        this.timer.schedule(bulletTask,20,100);
    }

    public void setWorld(World world){
        this.world = world;
    }

    boolean blockedByWall() {
        Tile[][] map = this.world.getMap();
        int jx = this.x / 80;
        int jy = this.y / 80;
        if(map[jx][jy].isWall()) {
            return true;
        }
        jx = (this.x + width - 1)/80;
        if(map[jx][jy].isWall()) {
            return true;
        }
        jx = this.x / 80;
        jy = (this.y + height - 1)/80;
        if(map[jx][jy].isWall()) {
            return true;
        }
        jx = (this.x + width - 1)/80;
        if(map[jx][jy].isWall()) {
            return true;
        }
        return false;
    }

    public void setLauncher(boolean launcher){
        this.launcher = launcher;
    }

    public boolean getLauncher(){
        return this.launcher;
    }

    public void setAtk(int atk){
        this.atk = atk;
    }

    public int getAtk(){
        return this.atk;
    }

    public void setGameFrame(GameFrame gameFrame){
        this.gameFrame = gameFrame;
    }

    public int get_x(){
        return this.x;
    }

    public int get_y(){
        return this.y;
    }

    public int get_width(){
        return this.width;
    }

    public int get_height(){
        return this.height;
    }

    public boolean getAble() {return this.able;}

    public String getDirection() {return this.direction;}

    @Override
    public String getName() {return this.name;}

}

class BulletTask extends TimerTask{

    BulletLabel bulletLabel;

    public BulletTask(BulletLabel bulletLabel){
        this.bulletLabel = bulletLabel;
    }
    @Override
    public void run() {
        this.bulletLabel.move();
    }
}