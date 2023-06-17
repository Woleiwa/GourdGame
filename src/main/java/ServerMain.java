import Network.Server;

import java.io.IOException;

public class ServerMain {
    public static void main(String args[]){
        Server server = Server.getInstance();
        try {
            server.constructConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
