package Being.Factory;

import Being.Being;
import Being.Empty;
import Being.Tile;
import Being.Road;
import Being.Wall;
public class BeingFactory {
    public static Being createBeing(String type,Tile tile){
        if(type.equals("Empty")){
            return new Empty(tile);
        }
        else if(type.equals("Road")){
            return new Road(tile);
        }
        else if(type.equals("Wall")){
            return new Wall(tile);
        }
        return null;
    }
}
