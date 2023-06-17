package Gui.Label;

import javax.swing.*;
import Being.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import Algorithm.*;
import Being.Factory.CreatureFactory;
import Gui.CreatureThread;
import Gui.Factory.BulletLabelFactory;
import Gui.Frame.GameFrame;
import Gui.Frame.SingleGameFrame;
import Observer.Observer;

public class CreatureLabel extends JLabel {
    protected String name;
    protected Creature creature;
    protected CreatureThread thread;
    protected String direction;
    protected int index = 1;
    protected int cur_x = 0;
    protected int cur_y = 0;
    protected int width = 0;
    protected int height = 0;
    protected Timer timer;
    protected Timer bulletTimer;
    protected HpBar hpBar;
    protected Boolean detected = false;
    protected Boolean stop = false;
    protected SingleGameFrame singleGameFrame;
    private boolean client = false;
    protected Observer observer;
    protected boolean killed = false;

    public CreatureLabel(String name, int x, int y, int w, int h, World world, String direction) {
        this.name = name;
        this.creature = CreatureFactory.createCreature(true,name);
        if(direction.startsWith("f"))
        {
            this.direction = "front";
        }
        else if(direction.startsWith("b"))
        {
            this.direction = "back";
        }
        else if(direction.startsWith("l"))
        {
            this.direction = "left";
        }
        else
        {
            this.direction = "right";
        }
        String filepath = "img/" + name +"/" + this.direction +"_" + Integer.toString(index) +".png";
        this.cur_x = x * w;
        this.cur_y = y * h;
        this.width = w;
        this.height = h;
        ImageIcon img = new ImageIcon(new ImageIcon(filepath).getImage().getScaledInstance(width,height, Image.SCALE_DEFAULT));
        this.setIcon(img);
        this.setBounds(this.cur_x, this.cur_y, w, h);
        this.setOpaque(false);
        creature.setWorld(world);
        world.getMap()[x][y].creatureMoveToTile(creature);
        creature.setCur_tile(world.getMap()[x][y]);
    }

    public void setObserver(Observer observer){
        this.observer = observer;
    }
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
            RoleLabel target = this.singleGameFrame.getRoleLabel();
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

    public boolean changeDirection() {
        if(this.cur_x % 80 != 0 || this.cur_y % 80 != 0)
        {
            return false;
        }
        if(this.detected){
            int[][] map = this.creature.getWorld().mapToInt();
            RoleLabel target = this.singleGameFrame.getRoleLabel();
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
    public int getCreatureAtk(){
        return this.creature.getAtk();
    }

    protected boolean accessible(int x, int y) {
        if( x < 0  || y < 0)
            return false;
        else if(x >= 15 || y >= 10)
            return false;
        Tile[][] map = creature.getWorld().getMap();
        if(map[x][y].getBeing().beOccupied())
            return false;
        return true;
    }

    public void setThread(CreatureThread creatureThread){
        this.thread = creatureThread;
    }

    public CreatureThread getThread(){
        return this.thread;
    }

    public void Start() {
        this.timer = new Timer();
        CreatureTask ctask = new CreatureTask(this);
        if(this.detected){
            timer.schedule(ctask,200,100);
        }
        else
        {
            timer.schedule(ctask,200,150);
        }

    }

    public void setHpBar(HpBar hpBar){
        this.hpBar = hpBar;
    }

    public HpBar getHpBar(){
        return this.hpBar;
    }

    public Creature getCreature(){
        return this.creature;
    }

    public void launchBullet(){
        if(this.creature.isDead() || this.stop){
            return;
        }
        String[] directions = {"back","front","left","right"};
        for(int i = 0; i < directions.length; i++){
            BulletLabel bulletLabel = BulletLabelFactory.createBulletLabel(true,name,directions[i],cur_x,cur_y,this.creature.getWorld(), this.creature.getAtk(),false);
            this.singleGameFrame.addBullet(bulletLabel);
            bulletLabel.launch();
        }
    }

    public void setGameFrame(SingleGameFrame singleGameFrame){
        this.singleGameFrame = singleGameFrame;
    }

    public void StartLaunch(){
        this.bulletTimer = new Timer();
        CreatureBulletTask cbt = new CreatureBulletTask(this);
        bulletTimer.schedule(cbt,60,500);
    }

    @Override
    public String getName(){
        return this.name;
    }

    public void StartAtk(){
        this.bulletTimer = new Timer();
        CreatureAttackTask cat = new CreatureAttackTask(this);
        bulletTimer.schedule(cat,60,500);
    }

    public void attack(){
        if(this.creature.isDead() || this.stop){
            return;
        }
        this.observer.creatureNotify(this);
    }

    synchronized public boolean hitByBullet(BulletLabel bullet){
        if(this.creature.isDead()){
            return false;
        }
        int x1 = bullet.get_x();
        int y1 = bullet.get_y();
        int x2 = x1 + bullet.get_width();
        int y2 = y1 + bullet.get_height();
        boolean res = false;

        if(this.cur_x <= x1 && this.cur_y <= y1 && this.cur_x + this.width >= x1 && this.cur_y + this.height >= y1){
            res = true;
        }
        else if(this.cur_x <= x2 && this.cur_y <= y1 && this.cur_x + this.width >= x2 && this.cur_y + this.height >= y1){
            res = true;
        }
        else if(this.cur_x <= x1 && this.cur_y <= y2 && this.cur_x + this.width >= x1 && this.cur_y + this.height >= y2){
            res = true;
        }
        else if(this.cur_x <= x2 && this.cur_y <= y2 && this.cur_x + this.width >= x2 && this.cur_y + this.height >= y2){
            res = true;
        }

        if(res){
            this.creature.setHp(this.creature.getHp() - bullet.getAtk());
            this.updateHpBar();
            if(!this.detected && !this.client) {
                if(this.creature.getAtkMode()){
                    this.StartLaunch();
                }
                else{
                    this.StartAtk();
                }
                timer.cancel();
                this.timer = new Timer();
                Tile cur_tile = this.creature.getCur_tile();
                cur_tile.creatureMoveOut();
                CreatureTask ctask = new CreatureTask(this);
                timer.schedule(ctask,0,100);
            }
            this.detected = true;
        }
        return res;
    }

    public int get_x(){
        return (this.cur_x + this.width/2)/ width;
    }

    public int get_y(){
        return (this.cur_y + this.height/2)/ height;
    }

    public void setClient(boolean client){
        this.client = client;
    }
    
    synchronized public void getAtk(){
        if(this.creature.isDead()){
            return;
        }
        this.creature.setHp(this.creature.getHp() - this.singleGameFrame.getRoleLabel().getRole().getAtk());
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
    }

    public void setStop(boolean stop){
        this.stop = stop;
    }

    public String getDirection() {return this.direction;}

    public int getCur_x() {return this.cur_x;}

    public int getCur_y() {return this.cur_y;}

    public boolean getDetected() {return this.detected;}

    public void setDetected(Boolean detected) {
        this.detected = detected;
        if(detected){
            if(this.creature.getAtkMode()){
                if(this.creature.getAtkMode()){
                    this.StartLaunch();
                }
                else{
                    this.StartAtk();
                }
                Tile cur_tile = this.creature.getCur_tile();
                cur_tile.creatureMoveOut();
            }
        }
    }

    public void setCur_x(int x){
        this.cur_x = x;
    }

    public void setCur_y(int y){
        this.cur_y = y;
    }

    synchronized public void updateHpBar(){
        this.hpBar.upDateHpBar();
        if(this.creature.isDead() && !this.killed){
            this.killed = true;
            this.setVisible(false);
            this.thread.interrupt();
            this.observer.creatureDied(this);
        }
    }
}

class CreatureTask extends TimerTask{

    private CreatureLabel clabel;
    public CreatureTask(CreatureLabel clabel){
        this.clabel = clabel;
    }

    @Override
    public void run() {
        clabel.move();
    }
}

class CreatureBulletTask extends TimerTask{

    CreatureLabel creatureLabel;

    public CreatureBulletTask(CreatureLabel creatureLabel){
        this.creatureLabel = creatureLabel;
    }
    @Override
    public void run() {
        creatureLabel.launchBullet();
    }
}

class CreatureAttackTask extends TimerTask{

    CreatureLabel creatureLabel;

    public CreatureAttackTask(CreatureLabel creatureLabel){
        this.creatureLabel = creatureLabel;
    }
    @Override
    public void run() {
        creatureLabel.attack();
    }
}