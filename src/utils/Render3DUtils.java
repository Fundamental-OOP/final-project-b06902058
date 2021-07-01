package utils;

import view.GUI;

import java.awt.*;

public class Render3DUtils {
    public static void drawDiamond(Point position, Graphics g) {
        g.setColor(Color.BLACK);    
        int newX = 600 + (int) (0.92 * (position.getX() - position.getY()));
        int newY = 200 + (int) (0.32 * (position.getX() + position.getY()));

        newX = (int) ((double) newX * GUI.HEIGHT / 800);
        newY = (int) ((double) newY * GUI.WIDTH / 1440);
        int delta = (int) (13.5 * ((double)GUI.HEIGHT / 800 + (double)GUI.WIDTH / 1440 ));

        g.setColor(ColorUtils.GRID_COLOR);
        g.fillPolygon(
            new int[]{newX, newX+3 * delta, newX+6 * delta, newX+3 * delta},
            new int[]{newY, newY+delta, newY, newY-delta},
            4
        );
        g.setColor(ColorUtils.GRID_ROUND_COLOR);
        g.drawLine(newX,newY,newX+3 * delta,newY + delta);
        g.drawLine(newX+3 * delta,newY+delta,newX+6 * delta,newY);
        g.drawLine(newX+6 * delta,newY,newX+3 * delta,newY-delta);
        g.drawLine(newX+3 * delta,newY-delta,newX,newY);
    }

    public static void drawRole(Image image, Graphics g, int x, int y, int w, int h) {
        g.setColor(Color.BLACK);
        int newRangeX = 650 + (int) (0.92 * (x - y));
        int newRangeY = 140 + (int) (0.32 * (x + y));

        newRangeX = (int) ((double) newRangeX * GUI.HEIGHT / 800);
        newRangeY = (int) ((double) newRangeY * GUI.WIDTH / 1440);
        int newW = (int) ((double) w * GUI.HEIGHT / 800);
        int newH = (int) ((double) h * GUI.WIDTH / 1440);
        g.drawImage(image, newRangeX, newRangeY, newW, newH, null);
    }
}
