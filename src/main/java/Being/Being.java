package Being;

public class Being {
    private Tile tile;
    private String name;

    public Being(Tile tile, String name){
        this.name = name;
        this.tile = tile;
    }

    public Being(String name)
    {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean beOccupied() {
        return true;
    }

    public void setCreature(Creature creature) {
        if(this.beOccupied()){
            return;
        }
    }

    public void creatureMoveOut() {
        return;
    }

}
