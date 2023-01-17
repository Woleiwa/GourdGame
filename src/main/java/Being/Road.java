package Being;

public class Road extends Being{
    private Creature creature = null;
    private boolean occupied = false;
    public Road(Tile tile, String name) {
        super(tile, name);
    }

    public Road(Tile tile) {
        super(tile,"Road");
    }

    @Override
    public boolean beOccupied() {
        return this.occupied;
    }

    @Override
    synchronized public void setCreature(Creature creature) {
        if(this.occupied) {
            return;
        }
        this.creature = creature;
        this.occupied = true;
    }

    @Override
    public void creatureMoveOut() {
        this.occupied = false;
        this.creature = null;
    }


}
