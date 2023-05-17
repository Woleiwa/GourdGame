package Gui.Label;

import Being.Factory.CreatureFactory;
import Being.Role;
import Being.Tile;
import Being.World;
import Gui.Factory.BulletLabelFactory;
import Gui.Frame.GameFrame;
import Gui.StackException;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

public class RoleLabel extends JLabel {
    protected String name;
    protected Role role;
    protected String direction = "front";
    protected int index = 1;
    protected int cur_x = 0;
    protected int cur_y = 0;
    protected int width = 0;
    protected int height = 0;
    protected Timer timer;
    protected boolean movable = false;
    protected HpBar hpBar;
    private GameFrame gameFrame;
    protected Timer bulletTimer;
    protected boolean launch = false;
    protected RoleMoveCommandStack moveStack = new RoleMoveCommandStack();
    protected RoleMoveCommandStack releaseStack = new RoleMoveCommandStack();
    protected boolean stop = false;

    public RoleLabel(String name, int x, int y, int w, int h, World world){
        this.name = name;
        this.role = (Role) CreatureFactory.createCreature(false,name);
        String filepath = "img/" + name +"/" + this.direction +"_" + Integer.toString(index) +".png";
        this.cur_x = x * w;
        this.cur_y = y * h;
        this.width = w;
        this.height = h;
        ImageIcon img = new ImageIcon(new ImageIcon(filepath).getImage().getScaledInstance(width,height, Image.SCALE_DEFAULT));
        this.setIcon(img);
        this.setBounds(this.cur_x, this.cur_y, w, h);
        this.setOpaque(false);
        role.setWorld(world);
        world.getMap()[x][y].creatureMoveToTile(role);
        role.setCur_tile(world.getMap()[x][y]);
        this.timer = new Timer();
        TimerTask task = new RoleTask(this);
        timer.schedule(task,10,80);
    }

    public Role getRole(){
        return this.role;
    }

    public void setHpBar(HpBar hpBar){
        this.hpBar = hpBar;
    }

    public void move() {
        if(!this.movable || this.role.isDead() || this.stop) {
            return;
        }
        this.index = (this.index) % 4 + 1;
        String filepath = "img/" + name +"/" + direction +"_" + Integer.toString(index) +".png";
        ImageIcon img = new ImageIcon(new ImageIcon(filepath).getImage().getScaledInstance(width,height, Image.SCALE_DEFAULT));
        this.setIcon(img);
        /*
        if(this.cur_x % 80 == 0 || this.cur_y % 80 == 0){
            int new_x = this.cur_x / 80;
            int new_y = this.cur_y / 80;
            boolean judge = false;
            if(this.direction == "front" && this.cur_y % 80 == 0) {
                new_y += 1;
                judge = true;
            }
            else if(this.direction == "back" && this.cur_y % 80 == 0) {
                new_y -= 1;
                judge = true;
            }
            else if(this.direction == "right" && this.cur_x % 80 == 0) {
                new_x += 1;
                judge = true;
            }
            else if(this.direction == "left" && this.cur_x % 80 == 0) {
                new_x -= 1;
                judge = true;
            }
            if(accessible(new_x, new_y) && judge)
            {
                if(this.role.getWorld().getMap()[new_x][new_y].creatureMoveToTile(this.role)) {
                    int x = this.role.getCur_tile().getX();
                    int y = this.role.getCur_tile().getY();
                    this.role.getWorld().getMap()[x][y].creatureMoveOut();
                    this.role.setCur_tile(this.role.getWorld().getMap()[new_x][new_y]);
                }
                else {
                    this.move();
                    return;
                }
            }
            else if(! accessible(new_x, new_y) && judge){
                return;
            }
        }*/
        int speed = this.role.getSpeed();
        //System.out.println(speed);
        int pre_x = cur_x;
        int pre_y = cur_y;
        if(this.direction == "front") {
            this.cur_y += speed;
        }
        else if(this.direction == "back") {
            this.cur_y -= speed;
        }
        else if(this.direction == "right") {
            this.cur_x += speed;
        }
        else if(this.direction == "left") {
            this.cur_x -= speed;
        }
        if(cur_x < 0) {
            cur_x = 0;
        }
        else if(cur_x > 1200 - width) {
            cur_x = 1200 - width;
        }
        if(cur_y < 0) {
            cur_y = 0;
        }
        else if(cur_y > 800 - height) {
            cur_y = 800 - height;
        }
        if(blockedByWall((cur_x + width - 1)/80, cur_y / 80)
        || blockedByWall(cur_x/80, (cur_y + height - 1)/ 80) ||blockedByWall((cur_x + width - 1)/80, (cur_y + height - 1)/ 80)
        || blockedByWall(cur_x/80,cur_y / 80)) {
            cur_x = pre_x;
            cur_y = pre_y;
        }
        this.hpBar.moveHp(this.cur_x, this.cur_y);
        this.setBounds(this.cur_x, this.cur_y, this.width, this.height);
    }

    protected boolean accessible(int x, int y) {
        if( x < 0  || y < 0)
            return false;
        else if(x >= 15 || y >= 10)
            return false;
        Tile[][] map = role.getWorld().getMap();
        if(map[x][y].getBeing().beOccupied())
            return false;

        return true;
    }

    private boolean blockedByWall(int x, int y) {
        Tile[][] map = this.role.getWorld().getMap();
        if(x >= 15 || y >= 10) {
            return false;
        }
        if(map[x][y].isWall()) {
            return true;
        }
        return false;
    }

    public void setDirection(char w){
        if(!this.moveStack.isEmpty()){
            try {
                if(this.moveStack.getTop().charValue() == w) {
                    return;
                }
            } catch (StackException e) {
                e.printStackTrace();
            }
        }
        if(w == 'w'){
            this.direction = "back";
        }
        else if(w == 'a') {
            this.direction = "left";
        }
        else if(w == 's') {
            this.direction = "front";
        }
        else if(w == 'd'){
            this.direction = "right";
        }
        //System.out.println("push " + w);
        this.moveStack.push(w);
    }

    public void startMove() {
        if(this.movable == true) {
            return;
        }
        this.movable = true;
    }

    public void endMove(char w) throws StackException {
        this.releaseStack.push(w);

        while (!releaseStack.isEmpty() && !moveStack.isEmpty() && releaseStack.getTop().charValue() == moveStack.getTop().charValue()){
            //System.out.println(releaseStack.getTop().charValue());
            //System.out.println(moveStack.getTop().charValue());
            releaseStack.pop();
            moveStack.pop();
            //System.out.println(moveStack.isEmpty());
        }
        if(moveStack.isEmpty()){
            //System.out.println("Set false");
            this.movable = false;
        }
        else{
            char d = moveStack.getTop().charValue();
            moveStack.pop();
            this.setDirection(d);
        }
    }

    public void launchBullet(){
        if(!launch || this.role.isDead() || this.stop){
            return;
        }
        String[] directions = {"back","front","left","right"};
        for(int i = 0; i < directions.length; i++){
            BulletLabel bulletLabel =  BulletLabelFactory.createBulletLabel(true,name,directions[i],cur_x,cur_y,this.role.getWorld(), this.role.getAtk(),true);
            this.gameFrame.addBullet(bulletLabel);
            bulletLabel.launch();
        }
    }

    public void setGameFrame(GameFrame gameFrame){
        this.gameFrame = gameFrame;
    }

    public void startAtk(){
        if(this.role.getAtkMode())
        {
            this.bulletTimer = new Timer();
            RoleBulletTask rbt = new RoleBulletTask(this);
            bulletTimer.schedule(rbt,10,500);
        }
        else
        {
            this.bulletTimer = new Timer();
            RoleAttackTask rbt = new RoleAttackTask(this);
            bulletTimer.schedule(rbt,10,500);
        }
    }

    public void setLaunch(boolean launch){
        this.launch = launch;
    }

    public int get_x(){
        return (this.cur_x + this.width/2)/ width;
    }

    public int get_y(){
        return (this.cur_y + this.height/2)/ height;
    }

    synchronized public boolean hitByBullet(BulletLabel bullet){
        boolean res = false;
        int x1 = bullet.get_x();
        int y1 = bullet.get_y();
        int x2 = x1 + bullet.get_width();
        int y2 = y1 + bullet.get_height();

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
            this.role.setHp(this.role.getHp() - bullet.getAtk());
            this.updateHp();
        }
        return res;
    }

    public void updateHp(){
        if(this.role.isDead()){
            this.gameFrame.fail();
        }
        this.hpBar.upDateHpBar();
    }

    public void attack(){
        if (!this.launch ||this.role.isDead() || this.stop) {
            return;
        }
        if(this.launch){
            this.gameFrame.roleAtk();
            System.out.println("I attacked!");
        }
    }

    public void setStop(boolean stop){
        this.stop = stop;
    }

    public String getDirection() {return this.direction;}

    public int getCur_x() {return this.cur_x;}

    public int getCur_y() {return this.cur_y;}

    public void setCur_x(int x){
        this.cur_x = x;
    }

    public void setCur_y(int y){
        this.cur_y = y;
    }

    @Override
    public String getName(){
        return this.name;
    }

    public HpBar getHpBar(){
        return this.hpBar;
    }

}

class RoleTask extends TimerTask {

    private RoleLabel rolelabel;
    public RoleTask(RoleLabel rolelabel){
        this.rolelabel = rolelabel;
    }

    @Override
    public void run() {
        rolelabel.move();
    }
}

class RoleBulletTask extends TimerTask {

    private RoleLabel roleLabel;
    public RoleBulletTask(RoleLabel roleLabel){
        this.roleLabel = roleLabel;
    }

    @Override
    public void run() {
        roleLabel.launchBullet();
    }
}

class RoleAttackTask extends TimerTask {

    private RoleLabel roleLabel;
    public RoleAttackTask(RoleLabel roleLabel){
        this.roleLabel = roleLabel;
    }

    @Override
    public void run() {
        roleLabel.attack();
    }
}

class RoleMoveCommandStack {
    private Stack<Character> stack = new Stack<>();

    boolean isEmpty(){
        return stack.isEmpty();
    }

    void pop(){
        stack.pop();
    }

    void push(char w){
        Character e = new Character(w);
        stack.push(e);
    }

    Character getTop() throws StackException {
        if(stack.isEmpty()){
            throw new StackException();
        }
        return stack.peek();
    }
}

