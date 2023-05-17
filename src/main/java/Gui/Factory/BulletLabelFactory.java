package Gui.Factory;

import Being.World;
import Gui.Label.BulletLabel;
import Gui.Label.DoubleBulletLabel;

public class BulletLabelFactory {
    public static BulletLabel createBulletLabel(boolean type, String name, String direction, int x, int y, World world, int atk, boolean launcher){
        BulletLabel bulletLabel;
        if(type) {
            bulletLabel = new BulletLabel(name, direction, x + 30, y + 30, 20, 20, 10);
        }
        else{
            bulletLabel = new DoubleBulletLabel(name, direction, x + 30, y + 30, 20, 20, 10);
        }
        bulletLabel.setWorld(world);
        bulletLabel.setAtk(atk);
        bulletLabel.setLauncher(launcher);
        return bulletLabel;
    }
}
