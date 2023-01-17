package Gui;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PictureLabel extends JPanel {
    private String filepath;
    private int x, y, width, height;
    private BufferedImage bf_img;

    public PictureLabel(String filepath, int x, int y, int width, int height){
        this.filepath = filepath;
        this.setOpaque(true);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.setBounds(x,y,width,height);
        File bg_img = new File(filepath);
        try {
            bf_img = ImageIO.read(bg_img);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(bf_img,x , y, width, height, this);
    }
}
