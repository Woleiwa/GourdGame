package Being;

import java.io.*;

public class Creature {
    private int hp = 0;
    private int speed = 0;
    private int atk = 0;
    private int max_hp = 0;
    private boolean atk_mode = false;
    private String name;
    private World world;
    private Tile cur_tile;

    public Creature(){}

    public Creature(int hp, int atk, int speed, boolean atk_mode, World world) {
        this.max_hp = hp;
        this.hp = hp;
        this.speed = speed;
        this.atk = atk;
        this.atk_mode = atk_mode;
        this.world = world;
    }

    public Creature(String name){
        this.name = name;
        this.readFromTxt();
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    synchronized public void setHp(int hp) {
        if(hp < 0) {
            this.hp = 0;
        }
        else {
            this.hp = hp;
        }
    }

    public void setMaxHp(int max_hp) {
        this.max_hp = max_hp;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public int getHp() {
        return this.hp;
    }

    public int getMax_hp() {
        return this.max_hp;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getAtk() {
        return this.atk;
    }

    public boolean isDead() {
        return this.hp == 0;
    }

    public boolean getAtkMode() {
        return this.atk_mode;
    }

    //public synchronized void getAtk(int atk) {
        //int new_hp = this.hp - atk;
        //this.setHp(new_hp);
    //}

    public void setCur_tile(Tile tile) {
        this.cur_tile = tile;
    }

    public void readFromTxt(){
        String filename = "monster/" + this.name + ".txt";
        File file = new File(filename);
        try {
            InputStream fin = new FileInputStream(file);
            int num = fin.available();
            StringBuffer line = new StringBuffer();
            for(int i = 0; i < num; i++) {
                line.append((char)fin.read());
            }
            String content = line.toString();
            String settings[] = content.split("\n");

            String str_hp = settings[0].substring(0, settings[0].length() - 1);
            this.max_hp = Integer.parseInt(str_hp);
            this.hp = this.max_hp;
            String str_atk = settings[1].substring(0, settings[1].length() - 1);
            this.atk = Integer.parseInt(str_atk);
            String str_speed = settings[2].substring(0, settings[2].length() - 1);
            this.speed = Integer.parseInt(str_speed);
            this.atk_mode = true;
            if(settings[3].startsWith("f"))
            {
                this.setAtk_mode(false);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld(){
        return this.world;
    }

    public Tile getCur_tile() {
        return this.cur_tile;
    }

    public void setAtk_mode(boolean atk_mode) {
        this.atk_mode = atk_mode;
    }
}
