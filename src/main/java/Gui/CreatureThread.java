package Gui;

import Gui.Label.CreatureLabel;

public class CreatureThread extends Thread{
    private CreatureLabel creatureLabel;

    public CreatureThread(CreatureLabel creatureLabel){
        this.creatureLabel = creatureLabel;
    }

    @Override
    public void run() {
        creatureLabel.Start();
    }
}
