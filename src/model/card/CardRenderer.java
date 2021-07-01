package model.card;

import view.GUI;
import model.role.Role;
import utils.ColorUtils;

import java.awt.*;

public class CardRenderer{
    private Color chosenColor = ColorUtils.CHOOSEN_COLOR;
    public void render(Image image, Graphics g, int index, boolean chosen) {
        int columnCellSize = GUI.WIDTH / 16;
        int rowCellSize = GUI.HEIGHT / 9;
        int width = columnCellSize;
        int height = (int)(rowCellSize * 1.5);
        g.drawImage(image, (int)(2 * columnCellSize + index * 1.5 * columnCellSize), (int)(7.25 * rowCellSize), width, height, null);
        if(chosen){
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(chosenColor);
            g2.setStroke(new BasicStroke(5));
            g.drawRect((int)(2 * columnCellSize + index * 1.5 * columnCellSize), (int)(7.25 * rowCellSize), width, height);
        }
        
    }

    public void renderAtShop(Image image, Graphics g, int index, boolean chosen, int price) {
        int columnCellSize = GUI.WIDTH / 16;
        int rowCellSize = GUI.HEIGHT / 9;
        int width = columnCellSize;
        int height = (int)(rowCellSize * 1.5);
        g.drawImage(image, (int)(5.25 * columnCellSize + index * 1.5 * columnCellSize), (int)(2 * rowCellSize), width, height, null);
        if(chosen){
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(chosenColor);
            g2.setStroke(new BasicStroke(5));
            g.drawRect((int)(5.25 * columnCellSize + index * 1.5 * columnCellSize), (int)(2 * rowCellSize), width, height);
        }
        g.setColor(Color.YELLOW);
        g.setFont(g.getFont().deriveFont((float)(columnCellSize * 0.15)));
        g.drawString(Integer.toString(price), (int)(5.25 * columnCellSize + index * 1.5 * columnCellSize), (int)(3.7 * rowCellSize));

    }

    public void evaluatingRender(Image image, Graphics g, Role role){
        return;
    }

    public void usingRender(Image image, Graphics g){
        int columnCellSize = GUI.WIDTH / 16;
        int rowCellSize = GUI.HEIGHT / 9;
        int width = columnCellSize * 2;
        int height = rowCellSize * 3;
        g.drawImage(image, 7 * columnCellSize, (int)(3 * rowCellSize), width, height, null);
        return;
    }
}
