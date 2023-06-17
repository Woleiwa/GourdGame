import Being.Creature;
import Being.Role;

import Gui.Frame.*;
import Network.Client;
import Network.Server;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GameTest {
    @Test
    public void CreatureTest(){
        Creature creature = new Creature("wolf");
        assertEquals(false,creature.getAtkMode());
        assertEquals(100,creature.getMax_hp());
        assertEquals(5,creature.getAtk());
        assertEquals(5,creature.getSpeed());
        Creature creature1 = new Creature("blue_dragon");
        assertEquals(true,creature1.getAtkMode());
        assertEquals(50,creature1.getMax_hp());
        assertEquals(2,creature1.getAtk());
        assertEquals(4,creature1.getSpeed());
    }

    @Test
    public void RoleTest(){
        Role role = new Role("elf");
        assertEquals(true,role.getAtkMode());
        assertEquals(100,role.getMax_hp());
        assertEquals(15,role.getAtk());
        assertEquals(4,role.getSpeed());
    }

    @Test
    public void GameTestWithLevel(){
        SingleGameFrame gf = new SingleGameFrame("elf",3);
        try {
            Thread.sleep(1000);
            assertEquals(3,gf.getLevel());
            assertEquals(8,gf.getMonster_num());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void StartFrameTest(){
        StartFrame sf = StartFrame.getInstance();
        sf.setVisible(true);
        try {
            Thread.sleep(1000);
            sf.Resume();
            Thread.sleep(1000);
            sf.Start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ResumeTest(){
        ResumeFrame rf = null;
        try {
            rf = new ResumeFrame();
            File file = new File("save");
            if(file.isDirectory()){
                String[] list = file.list();
                assertEquals(true,rf.isDir());
                assertEquals(list.length,rf.getFile_num());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void GameFrameActionTest(){
        SingleGameFrame gf = new SingleGameFrame("soldier",2);
        gf.setVisible(true);
        try {
            Thread.sleep(100);
            gf.PressAction('w');
            gf.TypeAction('w');
            Thread.sleep(1000);
            gf.PressAction('s');
            gf.TypeAction('s');
            Thread.sleep(500);
            gf.PressAction('s');
            Thread.sleep(500);
            gf.PressAction('j');
            Thread.sleep(500);
            gf.ReleaseAction('w');
            Thread.sleep(500);
            gf.PressAction('d');
            gf.ReleaseAction('s');
            gf.TypeAction('d');
            Thread.sleep(1000);
            gf.ReleaseAction('e');
            Thread.sleep(1000);
            gf.ReleaseAction('d');
            gf.ReleaseAction('q');
            gf.ReleaseAction('e');
            gf.win();
            //gf.ReleaseAction('e');
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void LoadTest(){
        SingleGameFrame gf = new SingleGameFrame("save/level_2_2023_1_7_22_17_28.txt");
        System.out.println(gf.getContent());
        String[] contents = gf.getContents();
        for(int i = 0; i < contents.length; i++){
            System.out.print(i);
            System.out.println(":" + contents[i]);
        }
        gf.setVisible(true);
        try {
            Thread.sleep(100);
            gf.ReleaseAction('q');
            Thread.sleep(100);
            gf.PressAction('w');
            gf.TypeAction('w');
            Thread.sleep(1000);
            gf.PressAction('s');
            gf.TypeAction('s');
            Thread.sleep(500);
            gf.PressAction('s');
            Thread.sleep(500);
            gf.PressAction('j');
            Thread.sleep(500);
            gf.ReleaseAction('w');
            Thread.sleep(500);
            gf.PressAction('d');
            gf.ReleaseAction('s');
            gf.TypeAction('d');
            Thread.sleep(1000);
            gf.ReleaseAction('e');
            Thread.sleep(1000);
            gf.ReleaseAction('d');
            gf.ReleaseAction('q');
            Thread.sleep(1000);
            gf.fail();
            assertEquals(2,gf.getLevel());
            assertEquals(6,gf.getMonster_num());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void DoubleGameFrameTest(){
        ServerFrame gf = new ServerFrame("elf","fighter",2);
        gf.setVisible(true);
        gf.MonsterStart();
        gf.test();
        while (!gf.getOver())
        {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void ConnectionTest() throws IOException, InterruptedException {
        Server testServer = Server.getInstance();
        TestServerThread thread = new TestServerThread(testServer);
        thread.start();

        Client testClient1 = new Client("elf",1);
        Client testClient2 = new Client("fighter",1);

        TestClientThread thread1 = new TestClientThread(testClient1);
        TestClientThread thread2 = new TestClientThread(testClient2);

        thread1.start();
        thread2.start();

        Thread.sleep(1000);

        ClientFrame frame1 = testClient1.getClientFrame();
        ClientFrame frame2 = testClient2.getClientFrame();

        frame1.typeAction('s');
        frame1.pressAction('s');
        Thread.sleep(200);
        frame1.typeAction('j');
        frame1.pressAction('j');
        Thread.sleep(500);

        frame2.typeAction('s');
        frame2.pressAction('s');
        Thread.sleep(200);
        frame2.typeAction('j');
        frame2.pressAction('j');
        Thread.sleep(500);

        frame1.releaseAction('s');
        frame2.releaseAction('s');
        frame1.releaseAction('j');
        frame2.releaseAction('j');

    }

}

class TestServerThread extends Thread{
    private Server server;

    public TestServerThread(Server server){
        this.server = server;
    }

    @Override
    public void run(){
        try {
            server.constructConnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

class TestClientThread extends Thread{
    private Client client;

    public TestClientThread(Client client){
        this.client = client;
    }

    @Override
    public void run(){
        try {
            client.constructConnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

