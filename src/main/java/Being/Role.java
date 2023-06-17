package Being;

import java.io.*;

public class Role extends Creature{

    public Role(String name){
        this.setName(name);
        this.readFromTxt();
    }

    @Override
    public void readFromTxt(){
        String filename = "role/" + this.getName() + ".txt";
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
            this.setMaxHp(Integer.parseInt(str_hp));
            this.setHp(Integer.parseInt(str_hp));
            String str_atk = settings[1].substring(0, settings[1].length() - 1);
            this.setAtk(Integer.parseInt(str_atk));
            String str_speed = settings[2].substring(0, settings[2].length() - 1);
            this.setSpeed(Integer.parseInt(str_speed));
            this.setAtk_mode(true);
            System.out.println(settings[3]);
            if(settings[3].equals("false"))
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
}
