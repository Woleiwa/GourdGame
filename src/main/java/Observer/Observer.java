package Observer;

import Gui.Frame.GameFrame;
import Gui.Label.BulletLabel;
import Gui.Label.CreatureLabel;
import Gui.Label.RoleLabel;



public abstract class Observer {
    protected GameFrame gameFrame;
    public Observer(GameFrame gameFrame){
        this.gameFrame = gameFrame;
    }
    public abstract void addCreature(CreatureLabel creatureLabel);
    public abstract void addRole(RoleLabel roleLabel);
    public abstract void bulletNotify(BulletLabel bulletLabel);
    public abstract void creatureNotify(CreatureLabel creatureLabel);
    public abstract void roleNotify(RoleLabel roleLabel);

    public abstract void roleDied(RoleLabel roleLabel);
    public abstract void creatureDied(CreatureLabel creatureLabel);
}
