package Gui.Label;

import Gui.Frame.DoubleGameFrame;

public class DoubleBulletLabel extends BulletLabel{
    private DoubleGameFrame doubleGameFrame;
    private int attacker;
    private boolean[] emited = new boolean[2];

    public DoubleBulletLabel(String name, String direction, int x, int y, int width, int height, int speed) {
        super(name, direction, x, y, width, height, speed);
        emited[0] = false;
        emited[1] = false;
    }

    public void setDoubleGameFrame(DoubleGameFrame doubleGameFrame){
        this.doubleGameFrame = doubleGameFrame;
    }

    public void setAttacker(int attacker){
        this.attacker = attacker;
    }

    public int getAttacker(){
        return this.attacker;
    }

    @Override
    public void move() {
        if(this.doubleGameFrame.isStop() || this.able == false){
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
        if(y < 0 || y + height > 800 || x < 0 || x + width > 1200 || this.blockedByWall()){
            disappear();
            return;
        }
        this.setBounds(this.x, this.y, width, height);
        this.observer.bulletNotify(this);
    }

    public Boolean emit(int index){
        return this.emited[index];
    }
    public String stringInfo(int index){
        String cur_x = Integer.toString(this.x);
        String cur_y = Integer.toString(this.y);
        String res = this.getName() + "," + this.direction + "," + cur_x + "," + cur_y;
        this.emited[index] = true;
        return res;
    }
}
