package Gui.Factory;

import Being.Tile;
import Being.World;
import Gui.Label.CreatureLabel;
import Gui.CreatureThread;
import Gui.Label.DoubleCreatureLabel;
import Gui.Label.HpBar;

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

    public static CreatureLabel createLoadedCreatureFactory(String content, World world){
        String[] elements = content.split(" ");
        int index_x = Integer.valueOf(elements[3]);
        int index_y = Integer.valueOf(elements[4]);
        int cur_x = Integer.valueOf(elements[5]);
        int cur_y = Integer.valueOf(elements[6]);
        int hp = Integer.valueOf(elements[2]);
        CreatureLabel creatureLabel = new CreatureLabel(elements[0],index_x,index_y,80,80,world,elements[1]);
        creatureLabel.setBounds(cur_x, cur_y,80,80);
        creatureLabel.setCur_x(cur_x);
        creatureLabel.setCur_y(cur_y);
        creatureLabel.getCreature().setHp(hp);
        CreatureThread creatureThread = new CreatureThread(creatureLabel);
        creatureLabel.setThread(creatureThread);
        if(elements[7].startsWith("false")){
            creatureLabel.setDetected(false);
        }
        else{
            creatureLabel.setDetected(true);
        }
        HpBar hpbar = new HpBar(cur_x, cur_y,10,80,Color.red,creatureLabel.getCreature());
        creatureLabel.setHpBar(hpbar);
        return creatureLabel;
    }
}
