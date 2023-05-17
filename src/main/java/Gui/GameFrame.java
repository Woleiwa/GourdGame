package Gui;

import javax.swing.*;
import Being.*;
import Gui.Factory.CreatureLabelFactory;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

public class GameFrame extends JFrame {
    private World world;
    private int level;
    private JLayeredPane jLayeredPane;
    private ArrayList<CreatureLabel> creatureLabels = new ArrayList<>();
    private RoleLabel roleLabel;
    public boolean stop = false;
    private JLabel stopLabel;
    private JLabel winLabel;
    private JLabel failLabel;
    private int monster_num = 0;
    private int killed_num = 0;
    private ArrayList<BulletLabel> bulletLabels = new ArrayList<>();
    private String content;
    private String[] contents;
    private boolean over = false;

    public GameFrame(String role, int level) {
        jLayeredPane = new JLayeredPane();
        this.level = level;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1216,846);
        this.setTitle("Game");
        this.setResizable(false);
        this.world = new World(this.level);

        PictureLabel bg = new PictureLabel("img/game.png",0,0,1200,800);
        bg.setPreferredSize(new Dimension(1200,800));
        bg.setBounds(0, 0, 1200, 800);
        jLayeredPane.add(bg,JLayeredPane.DEFAULT_LAYER);


        this.stopLabel = new JLabel();
        ImageIcon img = new ImageIcon(new ImageIcon("img/stop.png").getImage().getScaledInstance(80,80, Image.SCALE_DEFAULT));
        this.stopLabel.setIcon(img);
        this.stopLabel.setBounds(560, 360, 80, 80);
        jLayeredPane.add(stopLabel,JLayeredPane.POPUP_LAYER);
        stopLabel.setVisible(false);

        this.winLabel = new JLabel();
        ImageIcon win_img = new ImageIcon(new ImageIcon("img/win.png").getImage().getScaledInstance(200,120, Image.SCALE_DEFAULT));
        this.winLabel.setIcon(win_img);
        this.winLabel.setBounds(500, 340, 200, 120);
        jLayeredPane.add(winLabel,JLayeredPane.POPUP_LAYER);
        winLabel.setVisible(false);

        this.failLabel = new JLabel();
        ImageIcon fail_img = new ImageIcon(new ImageIcon("img/fail.png").getImage().getScaledInstance(200,120, Image.SCALE_DEFAULT));
        this.failLabel.setIcon(fail_img);
        this.failLabel.setBounds(500, 340, 200, 120);
        jLayeredPane.add(failLabel,JLayeredPane.POPUP_LAYER);
        failLabel.setVisible(false);

        this.drawMap();
        this.readFromTxt();

        Tile init_position = this.world.findEmpty();
        this.roleLabel = new RoleLabel(role,init_position.getX(),init_position.getY(),80,80,world);
        HpBar hp = new HpBar(init_position.getX() * 80,init_position.getY() * 80,10,80,Color.GREEN,roleLabel.getRole());
        this.roleLabel.setHpBar(hp);
        roleLabel.setGameFrame(this);
        jLayeredPane.add(hp,JLayeredPane.POPUP_LAYER);
        jLayeredPane.add(this.roleLabel,JLayeredPane.POPUP_LAYER);
        this.roleLabel.startAtk();

        this.add(jLayeredPane);
        this.addKeyListener(new RoleListener());
        this.addWindowListener(new GameWindowListener(this));
    }

    public GameFrame(String filepath){
        jLayeredPane = new JLayeredPane();
        int index_1 = filepath.indexOf("_");
        int index_2 = filepath.indexOf("_",index_1 + 1);
        String str_level = filepath.substring(index_1 + 1, index_2);
        this.level = Integer.valueOf(str_level);


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1216,846);
        this.setTitle("Game");
        this.setResizable(false);
        this.world = new World(this.level);

        PictureLabel bg = new PictureLabel("img/game.png",0,0,1200,800);
        bg.setPreferredSize(new Dimension(1200,800));
        bg.setBounds(0, 0, 1200, 800);
        jLayeredPane.add(bg,JLayeredPane.DEFAULT_LAYER);

        this.stopLabel = new JLabel();
        ImageIcon img = new ImageIcon(new ImageIcon("img/stop.png").getImage().getScaledInstance(80,80, Image.SCALE_DEFAULT));
        this.stopLabel.setIcon(img);
        this.stopLabel.setBounds(560, 360, 80, 80);
        jLayeredPane.add(stopLabel,JLayeredPane.POPUP_LAYER);
        stopLabel.setVisible(false);

        this.winLabel = new JLabel();
        ImageIcon win_img = new ImageIcon(new ImageIcon("img/win.png").getImage().getScaledInstance(200,120, Image.SCALE_DEFAULT));
        this.winLabel.setIcon(win_img);
        this.winLabel.setBounds(500, 340, 200, 120);
        jLayeredPane.add(winLabel,JLayeredPane.POPUP_LAYER);
        winLabel.setVisible(false);

        this.failLabel = new JLabel();
        ImageIcon fail_img = new ImageIcon(new ImageIcon("img/fail.png").getImage().getScaledInstance(200,120, Image.SCALE_DEFAULT));
        this.failLabel.setIcon(fail_img);
        this.failLabel.setBounds(500, 340, 200, 120);
        jLayeredPane.add(failLabel,JLayeredPane.POPUP_LAYER);
        failLabel.setVisible(false);

        this.drawMap();

        this.loadFromFile(filepath);
        this.add(jLayeredPane);
        this.addKeyListener(new RoleListener());

    }

    public void drawMap() {
        Tile[][] map = world.getMap();
        int width = 1200 / map.length;
        for(int i = 0; i < map.length; i++)
        {
            int height = 800 / map[i].length;
            for(int j = 0; j < map[i].length; j++)
            {
                if(map[i][j].getBeing().getName() != "Empty")
                {
                    int x = i * width;
                    int y = j * height;
                    String filepath = "img/"+map[i][j].getBeing().getName()+".png";
                    //System.out.println(filepath);
                    //PictureLabel being = new PictureLabel(filepath, x, y, width, height);
                    JLabel being =new JLabel(new ImageIcon(new ImageIcon(filepath).getImage().getScaledInstance(80,80,Image.SCALE_DEFAULT)));
                    being.setBounds(x, y, width, height);
                    being.setOpaque(true);
                    jLayeredPane.add(being,JLayeredPane.MODAL_LAYER);
                }
            }
        }
    }

    public void readFromTxt() {
        String filepath = "setting/monster_level_" +Integer.toString(this.level)+ ".txt";
        File file = new File(filepath);
        InputStream fin = null;
        try {
            fin = new FileInputStream(file);
            StringBuffer line = new StringBuffer();
            int num = fin.available();
            for (int i = 0; i < num; i++) {
                line.append((char) fin.read());
            }
            String contents = line.toString();
            String[] lines = contents.split("\n");
            this.monster_num = lines.length;
            for(int i = 0; i < lines.length; i++) {
                CreatureLabel creatureLabel =  CreatureLabelFactory.createCreatureFactory(true,lines[i],this.world);
                this.creatureLabels.add(creatureLabel);
                creatureLabel.setGameFrame(this);
                this.jLayeredPane.add(creatureLabel.getHpBar(), JLayeredPane.POPUP_LAYER);
                this.jLayeredPane.add(creatureLabel, JLayeredPane.POPUP_LAYER);
            }

            for(int i = 0; i < this.creatureLabels.size(); i++) {
                this.creatureLabels.get(i).getThread().start();
                //System.out.println(this.creatureLabels.get(i).getName());
                //if(i == 1){
                //    this.creatureLabels.get(i).StartLaunch();
                //}
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
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
            roleLabel.startMove();
        }
    }

    public void PressAction(char code){
        if(code == 'w' || code == 'a' || code == 's' || code == 'd') {
            roleLabel.setDirection(code);
        }
        else if(code == 'j'){
            roleLabel.setLaunch(true);
        }
    }

    public void ReleaseAction(char code){
        if(code == 'w' || code == 'a' || code == 's' || code == 'd') {
            try {
                roleLabel.endMove(code);
            } catch (StackException stackException) {
                stackException.printStackTrace();
            }
        }
        else if(code == 'j'){
            roleLabel.setLaunch(false);
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
        this.bulletLabels.add(bulletLabel);
    }

    public boolean bulletHitCreature(BulletLabel bulletLabel){
        if(bulletLabel.getLauncher()){
            for(int i = 0; i < creatureLabels.size(); i++){
                if(creatureLabels.get(i).hitByBullet(bulletLabel)){
                    return true;
                }
            }
        }
        return false;
    }

    public RoleLabel getRoleLabel(){
        return this.roleLabel;
    }

    public void roleAtk(){
        for(int i = 0; i < this.creatureLabels.size(); i++){
            CreatureLabel creaturelabel = creatureLabels.get(i);
            int dx = roleLabel.get_x() - creaturelabel.get_x();
            int dy = roleLabel.get_y() - creaturelabel.get_y();
            int distance = dx * dx + dy * dy;
            if(distance <= 1){
                creaturelabel.getAtk();
            }
        }
    }

    public void Stop(){
        this.stop = !(this.stop);
        for(int i = 0; i < this.creatureLabels.size(); i++){
            creatureLabels.get(i).setStop(this.stop);
        }
        roleLabel.setStop(this.stop);
        if(this.stop){
            this.stopLabel.setVisible(true);
        }
        else{
            this.stopLabel.setVisible(false);
        }
    }

    public void win() {
        winLabel.setVisible(true);
        this.Stop();
        this.over = true;
        this.stopLabel.setVisible(false);
        try {
            Thread.sleep(500);
            //StartFrame sf = new StartFrame();
            //sf.setVisible(true);
            this.setVisible(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void fail() {
        failLabel.setVisible(true);
        this.Stop();
        this.over = true;
        this.stopLabel.setVisible(false);
        try {
            Thread.sleep(500);
            //tartFrame sf = new StartFrame();
            //sf.setVisible(true);
            this.setVisible(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        String filename = "level_" + Integer.toString(this.level) + "_" + Integer.toString(y) + "_" + Integer.toString(m) + "_" + Integer.toString(d)
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
            String hp = Integer.toString(roleLabel.getRole().getHp());
            String index_x = Integer.toString(roleLabel.getX());
            String index_y = Integer.toString(roleLabel.getY());
            writer.append(name + " " + direction + " " + hp + " " + index_x + " " + index_y);
            writer.append("\r\n");

            writer.append("Bullet:");

            for(int i = 0; i < this.bulletLabels.size(); i++){
                BulletLabel bulletLabel = bulletLabels.get(i);
                if(!bulletLabel.getAble()){
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
                String[] elements = contents[i].split(" ");
                int index_x = Integer.valueOf(elements[3]);
                int index_y = Integer.valueOf(elements[4]);
                int cur_x = Integer.valueOf(elements[5]);
                int cur_y = Integer.valueOf(elements[6]);
                int hp = Integer.valueOf(elements[2]);
                CreatureLabel creatureLabel = new CreatureLabel(elements[0],index_x,index_y,80,80,this.world,elements[1]);
                creatureLabel.setBounds(cur_x, cur_y,80,80);
                creatureLabel.setCur_x(cur_x);
                creatureLabel.setCur_y(cur_y);
                creatureLabel.getCreature().setHp(hp);
                CreatureThread creatureThread = new CreatureThread(creatureLabel);
                creatureLabel.setThread(creatureThread);
                creatureLabel.setGameFrame(this);
                if(elements[7].startsWith("false")){
                    creatureLabel.setDetected(false);
                }
                else{
                    creatureLabel.setDetected(true);
                }
                HpBar hpbar = new HpBar(cur_x, cur_y,10,80,Color.red,creatureLabel.getCreature());
                this.jLayeredPane.add(hpbar, JLayeredPane.POPUP_LAYER);
                this.jLayeredPane.add(creatureLabel,JLayeredPane.POPUP_LAYER);
                creatureLabel.setHpBar(hpbar);
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
            roleLabel.setCur_x(index_x);
            roleLabel.setCur_y(index_y);
            this.roleLabel.getRole().setHp(hp);
            HpBar hpbar = new HpBar(index_x, index_y,10,80,Color.green,roleLabel.getRole());
            this.jLayeredPane.add(hpbar, JLayeredPane.POPUP_LAYER);
            this.jLayeredPane.add(roleLabel,JLayeredPane.POPUP_LAYER);
            this.roleLabel.setHpBar(hpbar);
            roleLabel.setGameFrame(this);
            roleLabel.startAtk();

            this.Stop();
            i+= 2;
            for( ;i < contents.length; i++){
                String[] bullet_elements = contents[i].split(" ");
                int bullet_x = Integer.valueOf(bullet_elements[3]);
                int bullet_y = Integer.valueOf(bullet_elements[4]);
                int atk = Integer.valueOf(bullet_elements[2]);
                BulletLabel bulletLabel = new BulletLabel(bullet_elements[0],bullet_elements[1],bullet_x,bullet_y,20,20,10);
                if(bullet_elements[0].startsWith(this.roleLabel.getName())){
                    bulletLabel.setLauncher(true);
                }
                else{
                    bulletLabel.setLauncher(false);
                }
                this.addBullet(bulletLabel);
                bulletLabel.setWorld(this.world);
                bulletLabel.setGameFrame(this);
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

    synchronized public void killMonster(){
        this.killed_num++;
        if(this.killed_num == this.monster_num){
            this.win();
        }
    }

    public int getLevel(){
        return this.level;
    }

    class GameWindowListener extends WindowAdapter {

        private GameFrame gf;

        public  GameWindowListener(GameFrame gf){
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
}
