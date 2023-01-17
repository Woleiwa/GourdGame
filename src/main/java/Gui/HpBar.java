package Gui;

import Being.Creature;

import javax.swing.*;
import java.awt.*;

public class HpBar extends JLabel {
    int height;
    int ori_width;
    Color hpColor;
    Creature creature;
    int x, y;

    public HpBar(int x, int y, int height, int ori_width, Color hpColor, Creature creature) {
        this.height = height;
        this.ori_width = ori_width;
        this.x = x;
        this.y = y;
        this.hpColor = hpColor;
        this.creature = creature;
        this.setBackground(hpColor);
        this.setOpaque(true);
        upDateHpBar();
    }

    public void moveHp(int x, int y){
        this.x = x;
        this.y = y;
        float rate = (float) this.creature.getHp()/ (float) this.creature.getMax_hp();
        float width = rate * ori_width;
        this.setBounds(x,y,(int) width,height);
    }

    public void upDateHpBar(){
        float rate = (float) this.creature.getHp()/ (float) this.creature.getMax_hp();
        float width = rate * ori_width;
        this.setBounds(x,y,(int) width,height);
    }

    public void setHpColor(Color color){
        this.hpColor = color;
        this.setBackground(hpColor);
    }
}
