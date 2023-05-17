package Network;

import Gui.Frame.ServerFrame;

import java.io.*;
import java.net.ServerSocket;

import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Server {
    private ServerSocket serverSocket = null;
    private ServerFrame serverFrame = null;
    private int level;
    private String role1;
    private String role2;
    private Socket socket1;
    private Socket socket2;
    private CommandThread thread1;
    private CommandThread thread2;

    public Server(){
        try {
            serverSocket = new ServerSocket(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void constructConnect() throws IOException {

        System.out.println("Server is waiting!");

        Socket socket_1 = serverSocket.accept();
        this.socket1 = socket_1;
        InputStream inputStream1 = socket1.getInputStream();
        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1));
        String s1 = bufferedReader1.readLine();
        System.out.println(s1);

        String[] elements1 = s1.split(" ");
        this.level = Integer.valueOf(elements1[0]);
        this.role1 = elements1[1];

        Socket socket_2 = serverSocket.accept();
        this.socket2 = socket_2;
        InputStream inputStream2 = socket2.getInputStream();
        BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream2));
        String s2 = bufferedReader2.readLine();
        System.out.println(s2);

        String[] elements2 = s2.split(" ");
        this.role2 = elements2[1];

        OutputStream outputStream1 = socket1.getOutputStream();
        BufferedWriter writer1 = new BufferedWriter(new OutputStreamWriter(outputStream1));
        writer1.write(this.level + " " + 0 + " " + this.role1 + " " + this.role2);
        writer1.newLine();
        writer1.flush();

        OutputStream outputStream2 = socket2.getOutputStream();
        BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(outputStream2));
        writer2.write(this.level + " " + 1 + " " + this.role1 + " " + this.role2);
        writer2.newLine();
        writer2.flush();

        this.serverFrame = new ServerFrame(this.role1, this.role2, this.level);
        serverFrame.MonsterStart();
        this.serverFrame.setVisible(true);

        thread1 = new CommandThread(socket1, this.serverFrame, 0);
        thread2 = new CommandThread(socket2, this.serverFrame, 1);
        thread1.start();
        thread2.start();
        UpdateThread updateThread1 = new UpdateThread(socket1, this.serverFrame, 0);
        UpdateThread updateThread2 = new UpdateThread(socket2, this.serverFrame, 1);
        updateThread1.start();
        updateThread2.start();

        this.ServerRun();
    }

    public void ServerRun() throws IOException {
        while (!serverFrame.getOver() && !socket1.isClosed() && !socket2.isClosed()){
            continue;
        }
        thread1.interrupt();
        thread2.interrupt();
        System.out.println("Server refreshed!");
        /*if(!this.socket1.isClosed()){
            this.socket1.close();
        }
        if(!this.socket2.isClosed()){
            this.socket2.close();
        }*/
        this.constructConnect();
    }
}

class CommandThread extends Thread{
    private Socket socket;
    private ServerFrame ServerFrame;
    private int index;

    public CommandThread(Socket socket, ServerFrame ServerFrame, int index){
        this.socket = socket;
        this.ServerFrame = ServerFrame;
        this.index = index;
    }

    @Override
    public void run(){
        try {
            InputStream inputStream = this.socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while (! ServerFrame.getOver() && ! socket.isClosed()){
                String command = bufferedReader.readLine();
                if(command != null){
                    //System.out.println(command);
                    ServerFrame.conductCommand(this.index,command);
                }
            }
            //System.out.println("Command Thread Over!");
            bufferedReader.close();
            socket.close();
            this.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class UpdateThread extends Thread{
    private Socket socket;
    private ServerFrame ServerFrame;
    int index;

    public UpdateThread(Socket socket, ServerFrame ServerFrame, int index){
        this.socket = socket;
        this.ServerFrame = ServerFrame;
        this.index = index;
    }

    @Override
    public void run(){
        Timer taskTimer = new Timer();
        UpdateTask task = new UpdateTask(this.socket, this.ServerFrame, this.index);
        taskTimer.schedule(task,10,50);
        while (!ServerFrame.getOver() && !socket.isClosed()){
            continue;
        }
        System.out.println("Game over!");
        taskTimer.cancel();
        this.interrupt();
    }
}

class UpdateTask extends TimerTask{
    private Socket socket;
    private ServerFrame ServerFrame;
    private int index;

    public UpdateTask(Socket socket, ServerFrame ServerFrame, int index){
        this.socket = socket;
        this.ServerFrame = ServerFrame;
        this.index = index;
    }

    @Override
    public void run() {
        if(socket.isClosed()){
            return;
        }
        try {
            OutputStream outputStream = socket.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(ServerFrame.ToString(index));
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}