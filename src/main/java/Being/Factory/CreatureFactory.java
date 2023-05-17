package Being.Factory;
import Being.Creature;
import Being.Role;
public class CreatureFactory {
    public static Creature createCreature(boolean type, String name){
        Creature creature;
        if(type){
            creature = new Creature(name);
        }
        else{
            creature = new Role(name);
        }
        return creature;
    }
}
