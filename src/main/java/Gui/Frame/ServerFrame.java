package Gui.Frame;

public class ServerFrame extends DoubleGameFrame {

    public ServerFrame(String role1, String role2, int level) {
        super(role1, role2, level);
        this.roleLabels[0].startAtk();
        this.roleLabels[1].startAtk();
    }

    public void conductCommand(int index, String command) {
        String[] elements = command.split(" ");
        char code = elements[1].charAt(0);
        if (elements[0].equals("type")) {
            this.TypeAction(index, code);
        } else if (elements[0].equals("release")) {
            this.ReleaseAction(index, code);
        } else if (elements[0].equals("press")) {
            this.PressAction(index, code);
        }
    }

    public String ToString(int index) {
        String res = "";
        for (int i = 0; i < 2; i++) {
            res += this.roleLabels[i].stringInfo() + " ";
        }
        for (int i = 0; i < this.doubleCreatureLabels.size(); i++) {
            if (this.doubleCreatureLabels.get(i).isEmitDeath(index)) {
                continue;
            }
            res += "monster," + Integer.toString(i) + "," + doubleCreatureLabels.get(i).stringInfo(index) + " ";
        }
        for (int i = 0; i < this.bulletLabels.size(); i++) {
            if (!this.bulletLabels.get(i).emit(index)) {
                res += "bullet," + bulletLabels.get(i).stringInfo(index) + " ";
            }
        }
        return res;
    }

    @Override
    public void win() {
        winLabel.setVisible(true);
        this.Stop();
        try {
            Thread.sleep(100);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    @Override
    public void fail() {
        failLabel.setVisible(true);
        this.Stop();
        try {
            Thread.sleep(100);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
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
}

