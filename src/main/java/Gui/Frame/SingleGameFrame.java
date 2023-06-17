package Gui.Frame;

import javax.swing.*;
import Being.*;
import Gui.Factory.CreatureLabelFactory;
import Gui.Label.*;
import Gui.StackException;
import Observer.Observer;
import Observer.SingleGameObserver;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

public class SingleGameFrame extends GameFrame {
    private final ArrayList<CreatureLabel> creatureLabels = new ArrayList<>();
    private RoleLabel roleLabel;
    private final ArrayList<BulletLabel> bulletLabels = new ArrayList<>();
    private final Observer observer = new SingleGameObserver(this);
    private String content;
    private String[] contents;
    public SingleGameFrame(String role, int level) {
        super(level);
        this.readFromTxt();

        Tile init_position = this.world.findEmpty();
        this.roleLabel = new RoleLabel(role,init_position.getX(),init_position.getY(),80,80,world);
        HpBar hp = new HpBar(init_position.getX() * 80,init_position.getY() * 80,10,80,Color.GREEN,this.roleLabel.getRole());
        this.roleLabel.setHpBar(hp);
        this.roleLabel.setGameFrame(this);
        jLayeredPane.add(hp,JLayeredPane.POPUP_LAYER);
        jLayeredPane.add(this.roleLabel,JLayeredPane.POPUP_LAYER);
        this.roleLabel.startAtk();
        this.roleLabel.setObserver(this.observer);

        this.observer.addRole(this.roleLabel);

        this.add(jLayeredPane);
        this.addKeyListener(new RoleListener());
        this.addWindowListener(new GameWindowListener(this));
    }
    public SingleGameFrame(String filepath){
        super(filepath);
        this.loadFromFile(filepath);
        this.add(jLayeredPane);
        this.addKeyListener(new RoleListener());
    }

    @Override
    public void readFromTxt(){
        String filepath = "setting/monster_level_" + this.level + ".txt";
        File file = new File(filepath);
        try(InputStream fin = new FileInputStream(file)){
            StringBuffer line = new StringBuffer();
            int num = fin.available();
            for (int i = 0; i < num; i++) {
                line.append((char) fin.read());
            }
            String contents = line.toString();
            String[] lines = contents.split("\n");
            this.monster_num = lines.length;
            for (String s : lines) {
                CreatureLabel creatureLabel = CreatureLabelFactory.createCreatureFactory(true, s, this.world);
                this.creatureLabels.add(creatureLabel);
                this.observer.addCreature(creatureLabel);
                creatureLabel.setGameFrame(this);
                creatureLabel.setObserver(this.observer);
                this.jLayeredPane.add(creatureLabel.getHpBar(), JLayeredPane.POPUP_LAYER);
                this.jLayeredPane.add(creatureLabel, JLayeredPane.POPUP_LAYER);
            }

            for (CreatureLabel creatureLabel : this.creatureLabels) {
                creatureLabel.getThread().start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class RoleListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            char code = e.getKeyChar();
            TypeAction(code);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            char code = e.getKeyChar();
            PressAction(code);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            char code = e.getKeyChar();
            ReleaseAction(code);
        }
    }
    public void TypeAction(char code){
        if(code == 'w' || code == 'a' || code == 's' || code == 'd') {
            this.roleLabel.startMove();
        }
    }
    public void PressAction(char code){
        if(code == 'w' || code == 'a' || code == 's' || code == 'd') {
            this.roleLabel.setDirection(code);
        }
        else if(code == 'j'){
            this.roleLabel.setLaunch(true);
        }
    }
    public void ReleaseAction(char code){
        if(code == 'w' || code == 'a' || code == 's' || code == 'd') {
            try {
                this.roleLabel.endMove(code);
            } catch (StackException stackException) {
                stackException.printStackTrace();
            }
        }
        else if(code == 'j'){
            this.roleLabel.setLaunch(false);
        }
        else if(code == 'q'){
            Stop();
        }
        else if(code == 'e'){
            if(stop){
                saveToFile();
                try {
                    Thread.sleep(500);
                    setVisible(false);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
        else if(code == 'r'){
            if(stop){
                try {
                    Thread.sleep(500);
                    setVisible(false);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }
    public void addBullet(BulletLabel bulletLabel){
        this.jLayeredPane.add(bulletLabel, JLayeredPane.POPUP_LAYER);
        bulletLabel.setGameFrame(this);
        bulletLabel.setObserver(this.observer);
        this.bulletLabels.add(bulletLabel);
    }
    public RoleLabel getRoleLabel(){
        return this.roleLabel;
    }
    public void saveToFile(){
        if(this.over){
            return;
        }
        Calendar calendar = Calendar.getInstance();
        int y= calendar.get(Calendar.YEAR);
        int m= calendar.get(Calendar.MONTH) + 1;
        int d= calendar.get(Calendar.DATE);
        int h= calendar.get(Calendar.HOUR_OF_DAY);
        int mi= calendar.get(Calendar.MINUTE);
        int s= calendar.get(Calendar.SECOND);
        String filename = "level_" + this.level + "_" + y + "_" + Integer.toString(m) + "_" + Integer.toString(d)
                + "_" + Integer.toString(h) + "_" + Integer.toString(mi) + "_" + Integer.toString(s) + ".txt";
        String filepath = "save/" + filename;
        try {
            OutputStream stream = new FileOutputStream(filepath);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.append("Monster:");
            writer.append("\r\n");

            for(int i = 0; i < this.monster_num; i++){
                CreatureLabel creatureLabel = creatureLabels.get(i);
                Creature creature = creatureLabel.getCreature();
                if(creature.isDead()){
                    continue;
                }
                String name = creature.getName();
                String direction = creatureLabel.getDirection();
                String hp = Integer.toString(creature.getHp());
                String tile_x = Integer.toString(creature.getCur_tile().getX());
                String tile_y = Integer.toString(creature.getCur_tile().getY());
                String index_x = Integer.toString(creatureLabel.getCur_x());
                String index_y = Integer.toString(creatureLabel.getCur_y());
                String detected = "false";
                if(creatureLabel.getDetected()){
                    detected = "true";
                }
                writer.append(name + " " + direction + " " + hp + " " + tile_x + " " + tile_y + " " + index_x + " " + index_y + " " + detected);
                writer.append("\r\n");
            }

            writer.append("Role:");
            writer.append("\r\n");
            String name = this.roleLabel.getRole().getName();
            String direction = this.roleLabel.getDirection();
            String hp = Integer.toString(this.roleLabel.getRole().getHp());
            String index_x = Integer.toString(this.roleLabel.getX());
            String index_y = Integer.toString(this.roleLabel.getY());
            writer.append(name + " " + direction + " " + hp + " " + index_x + " " + index_y);
            writer.append("\r\n");

            writer.append("Bullet:");

            for (BulletLabel bulletLabel : this.bulletLabels) {
                if (!bulletLabel.getAble()) {
                    continue;
                }
                writer.append("\r\n");
                String bullet_name = bulletLabel.getName();
                String bullet_atk = Integer.toString(bulletLabel.getAtk());
                String bullet_direction = bulletLabel.getDirection();
                String bullet_x = Integer.toString(bulletLabel.get_x());
                String bullet_y = Integer.toString(bulletLabel.get_y());
                writer.append(bullet_name + " " + bullet_direction + " " + bullet_atk + " " + bullet_x + " " + bullet_y);
            }

            writer.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadFromFile(String filepath){
        File file = new File(filepath);
        try {
            FileInputStream stream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(stream);
            StringBuffer buffer = new StringBuffer();
            while (reader.ready()){
                buffer.append((char)reader.read());
            }
            content = buffer.toString();
            contents = content.split("\r\n");
            int i = 1;
            for(; i < contents.length; i++){
                if(contents[i].startsWith("Role")){
                    break;
                }
                CreatureLabel creatureLabel = CreatureLabelFactory.createLoadedCreatureFactory(contents[i],world);
                creatureLabel.setGameFrame(this);
                this.observer.addCreature(creatureLabel);
                creatureLabel.setObserver(this.observer);
                this.jLayeredPane.add(creatureLabel.getHpBar(), JLayeredPane.POPUP_LAYER);
                this.jLayeredPane.add(creatureLabel,JLayeredPane.POPUP_LAYER);
                this.creatureLabels.add(creatureLabel);
                this.monster_num++;
            }

            i+= 1;
            String[] elements = contents[i].split(" ");
            int index_x = Integer.valueOf(elements[3]);
            int index_y = Integer.valueOf(elements[4]);
            int hp = Integer.valueOf(elements[2]);
            this.roleLabel = new RoleLabel(elements[0],index_x/80,index_y/80,80,80,this.world);
            this.roleLabel.setBounds(index_x, index_y,80,80);
            this.roleLabel.setCur_x(index_x);
            this.roleLabel.setCur_y(index_y);
            this.roleLabel.getRole().setHp(hp);
            HpBar hpbar = new HpBar(index_x, index_y,10,80,Color.green,this.roleLabel.getRole());
            this.jLayeredPane.add(hpbar, JLayeredPane.POPUP_LAYER);
            this.jLayeredPane.add(this.roleLabel,JLayeredPane.POPUP_LAYER);
            this.roleLabel.setHpBar(hpbar);
            this.roleLabel.setGameFrame(this);
            this.roleLabel.setObserver(this.observer);
            this.roleLabel.startAtk();

            this.observer.addRole(this.roleLabel);

            this.Stop();
            i+= 2;
            for( ;i < contents.length; i++){
                String[] bullet_elements = contents[i].split(" ");
                int bullet_x = Integer.valueOf(bullet_elements[3]);
                int bullet_y = Integer.valueOf(bullet_elements[4]);
                int atk = Integer.valueOf(bullet_elements[2]);
                BulletLabel bulletLabel = new BulletLabel(bullet_elements[0],bullet_elements[1],bullet_x,bullet_y,20,20,10);
                bulletLabel.setLauncher(bullet_elements[0].startsWith(this.roleLabel.getName()));
                this.addBullet(bulletLabel);
                bulletLabel.setWorld(this.world);
                bulletLabel.setGameFrame(this);
                bulletLabel.setObserver(this.observer);
                bulletLabel.setAtk(atk);
                bulletLabel.launch();
            }

            for(i = 0; i < this.creatureLabels.size(); i++) {
                this.creatureLabels.get(i).getThread().start();
                //System.out.println(this.creatureLabels.get(i).getName());
                if(this.creatureLabels.get(i).getDetected()){
                    if(creatureLabels.get(i).getCreature().getAtkMode()){
                        creatureLabels.get(i).StartLaunch();
                    }
                    else{
                        creatureLabels.get(i).StartAtk();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getContent() {
        return content;
    }

    public String[] getContents(){
        return contents;
    }
    class GameWindowListener extends WindowAdapter {

        private final SingleGameFrame gf;

        public  GameWindowListener(SingleGameFrame gf){
            this.gf = gf;
        }
        @Override
        public void windowClosing(WindowEvent e){
            gf.setVisible(false);
            System.out.println("Closed");
        }
    }

    public int getMonster_num() {
        return monster_num;
    }

    public void Stop(){
        this.stop = !(this.stop);
        for (CreatureLabel creatureLabel : this.creatureLabels) {
            creatureLabel.setStop(this.stop);
        }
        this.roleLabel.setStop(this.stop);
        this.stopLabel.setVisible(this.stop);
    }
}
