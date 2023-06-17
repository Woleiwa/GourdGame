package Network;

import Gui.Frame.ClientFrame;

import java.io.*;
import java.net.Socket;

public class Client {
    private String role;
    private int index;
    private int level;
    private Socket socket;
    private ClientFrame clientFrame;

    public Client(String role, int level){
        this.role = role;
        this.level = level;
        try {
            socket = new Socket("172.24.41.230",8080);//ip地址要根据服务器运行的电脑以及网络环境修改
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void constructConnect() throws IOException {
        System.out.println("Connecting to server!");
        OutputStream outputStream = socket.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        writer.write(Integer.toString(level) +  " " + role);
        writer.newLine();
        writer.flush();

        InputStream inputStream = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String config = reader.readLine();
        String[] element = config.split(" ");
        this.level = Integer.valueOf(element[0]);
        this.index = Integer.valueOf(element[1]);

        this.clientFrame = new ClientFrame(element[2], element[3], level, index);
        clientFrame.setVisible(true);
        clientFrame.setClient(this);

        ClientThread thread = new ClientThread(this.socket, clientFrame);
        thread.start();
    }

    public void clientSendCommand(String command) throws IOException {
        if(socket.isClosed()){
            return;
        }
        OutputStream outputStream = this.socket.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        writer.write(command);
        writer.newLine();
        writer.flush();
    }

    public ClientFrame getClientFrame() {
        return clientFrame;
    }
}

class ClientThread extends Thread{
    private Socket socket;
    private ClientFrame clientFrame;

    public ClientThread(Socket socket, ClientFrame clientFrame){
        this.socket = socket;
        this.clientFrame = clientFrame;
    }

    @Override
    public void run(){
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while (!clientFrame.isEnd() && !socket.isClosed()){
                String info = reader.readLine();
                if(info != null){
                    this.clientFrame.updateToString(info);
                }
            }
            reader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
