package Gui.Frame;

import Gui.Label.DoubleBulletLabel;
import Network.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class ClientFrame extends DoubleGameFrame{
    private int index;
    private Client client;

    public ClientFrame(String role1, String role2, int level, int index) {
        super(role1, role2, level);
        this.index = index;
        this.addKeyListener(new ClientListener());
        this.getRoleLabel(index).getHpBar().setHpColor(Color.cyan);
        for(int i = 0; i < this.doubleCreatureLabels.size(); i++){
            doubleCreatureLabels.get(i).setClient(true);
            /*if(doubleCreatureLabels.get(i).getCreature().getAtkMode()){
                doubleCreatureLabels.get(i).startClientLaunch();
            }*/
        }
        //roleLabels[0].startClientLaunch();
        //roleLabels[1].startClientLaunch();
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void sendCommand(String command) throws IOException {
        if(!this.isEnd()){
            this.client.clientSendCommand(command);
        }
    }

    class ClientListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            char code = e.getKeyChar();
            typeAction(code);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            char code = e.getKeyChar();
            pressAction(code);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            char code = e.getKeyChar();
            releaseAction(code);
        }
    }
    public void typeAction(char code){
        if(code == 'w' || code == 's' || code == 'a' || code == 'd' || code == 'j'){
            String command = "type " + code;
            try {
                sendCommand(command);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    public void pressAction(char code){
        if(code == 'w' || code == 's' || code == 'a' || code == 'd' || code == 'j'){
            String command = "press " + code;
            try {
                sendCommand(command);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void releaseAction(char code){
        if(code == 'w' || code == 's' || code == 'a' || code == 'd' || code == 'j'){
            String command = "release " + code;
            try {
                sendCommand(command);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }


    public void updateToString(String info){
        //this.updateBullet();
        String[] informs = info.split(" ");
        for(int i = 0; i < informs.length; i++){
            String[] elements = informs[i].split(",");
            if(elements[0].equals("role")){
                int role_index = Integer.valueOf(elements[1]);
                roleLabels[role_index].update(elements[2],elements[3],elements[4],elements[5],elements[6],elements[7]);
            }
            else if(elements[0].equals("monster")){
                int creature_index = Integer.valueOf(elements[1]);
                this.doubleCreatureLabels.get(creature_index).update(elements[2],elements[3],elements[4],elements[5],elements[6],elements[7]);
            }
            else if(elements[0].equals("bullet")){
                //System.out.println(informs[i]);
                int x = Integer.valueOf(elements[3]);
                int y = Integer.valueOf(elements[4]);
                DoubleBulletLabel bullet = new DoubleBulletLabel(elements[1],elements[2],x,y,20,20,10);
                this.jLayeredPane.add(bullet, JLayeredPane.POPUP_LAYER);
                if(elements[1].equals(this.roleLabels[0].getName()) || elements[1].equals(this.roleLabels[1].getName())){
                    bullet.setLauncher(true);
                }
                bullet.setAttacker(0);
                bullet.setDoubleGameFrame(this);
                bullet.setObserver(this.observer);
                bullet.setWorld(this.roleLabels[0].getRole().getWorld());
                bullet.launch();
            }
        }
    }

    /*private void updateBullet(){
        for(int i = 0; i < this.bulletLabels.size(); i++){
            this.bulletLabels.get(i).setVisible(false);
        }
        this.bulletLabels.clear();
    }*/
}
