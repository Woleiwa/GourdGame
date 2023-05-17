package Being;

import Being.Factory.BeingFactory;

public class Tile {
    private int x, y;
    private Being being;

    public Tile(int x, int y, Being being) {
        this.x = x;
        this.y = y;
        this.being = being;
    }

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.being = null;
    }

    public Tile(String line) {
        String[] res = line.split(",");
        String str_x = res[0].substring(1);
        String str_y = res[1].substring(0, res[1].indexOf(')'));
        //System.out.println(str_x);
        //System.out.println(str_y);
        this.x = Integer.parseInt(str_x);
        this.y = Integer.parseInt(str_y);
    }

    public Being getBeing() {
        return this.being;
    }

    synchronized public boolean creatureMoveToTile(Creature creature) {
        if(this.being.beOccupied()) {
            return false;
        }
        this.being.setCreature(creature);
        return true;
    }

    public void creatureMoveOut() {
        this.being.creatureMoveOut();
    }

    public void setBeing(Being being) { this.being = being; }

    public int getX() {return this.x;}

    public int getY() {return this.y;}

    public boolean isWall() {
        if(this.being.getName() == "Wall") {
            return true;
        }
        return false;
    }

    public boolean isRoad(){
        if(this.being.getName() == "Road"){
            return true;
        }
        return false;
    }
}
