package model.role;

import model.unit.ImageRenderer;
import view.GUI;

import java.awt.*;

public class AvatarRenderer implements ImageRenderer{

    @Override
    public void render(Image image, Graphics g) {
        int x = 0;
        int y = GUI.HEIGHT * 7 / 9;
        int size = (int)(Math.min(GUI.WIDTH * 2 / 16, GUI.HEIGHT * 7 / 9) * 0.9);
        g.drawImage(image, x, y, size, size, null);
    }
}
