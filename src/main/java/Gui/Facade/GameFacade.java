package Gui.Facade;

import Gui.Frame.GameFrame;
import Network.Client;

import java.io.IOException;

public class GameFacade {
    public static void GameStart(int mod,int level, String role){
        if(mod == 0){
            GameFrame gameFrame = new GameFrame(role,level);
            gameFrame.setVisible(true);
        }
        else{
            Client client = new Client(role, level);
            try {
                client.constructConnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
