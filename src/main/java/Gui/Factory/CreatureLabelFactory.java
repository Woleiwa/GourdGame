package Gui.Factory;

import Being.Tile;
import Being.World;
import Gui.CreatureLabel;
import Gui.CreatureThread;
import Gui.DoubleCreatureLabel;
import Gui.HpBar;

import java.awt.*;

public class CreatureLabelFactory {
    public static CreatureLabel createCreatureFactory(boolean type, String line, World world){
        String[] elements = line.split(" ");
        String name = elements[0];
        Tile tile = new Tile(elements[2]);
        String direction = elements[1];
        CreatureLabel creatureLabel;
        if(type){
            creatureLabel= new CreatureLabel(name, tile.getX(), tile.getY(), 80, 80, world, direction);
        }
        else {
            creatureLabel = new DoubleCreatureLabel(name, tile.getX(), tile.getY(), 80, 80, world, direction);
        }
        CreatureThread creatureThread = new CreatureThread(creatureLabel);
        creatureLabel.setThread(creatureThread);
        HpBar hp = new HpBar(tile.getX() * 80, tile.getY() * 80,10,80, Color.red,creatureLabel.getCreature());
        creatureLabel.setHpBar(hp);
        return creatureLabel;
    }
}
