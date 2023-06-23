package Gui.Facade;

import Gui.Frame.SingleGameFrame;
import Gui.Frame.WarningFrame;
import Network.Client;

import java.io.IOException;

public class GameFacade {
    public static void GameStart(int mod,int level, String role){
        if(mod == 0){
            SingleGameFrame singleGameFrame = new SingleGameFrame(role,level);
            singleGameFrame.setVisible(true);
        }
        else if (mod == 1){
            Client client;
            try {
                client = new Client(role, level);
                try {
                    client.constructConnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                String warning = "Please check the ip address in setting/ip/ip.txt.";
                WarningFrame warningFrame = new WarningFrame(warning);
                warningFrame.setVisible(true);
            }
        }
    }

    public static void GameResume(String filepath){
        SingleGameFrame gameframe = new SingleGameFrame(filepath);
        gameframe.setVisible(true);
    }
}
