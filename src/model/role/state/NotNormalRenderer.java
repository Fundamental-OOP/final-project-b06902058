package model.role.state;

import view.GUI;

import java.awt.*;

public class NotNormalRenderer {

    public void render(Image image, Graphics g, int index, int remainRounds){
        int columnCellSize = GUI.WIDTH / 16;
        int rowCellSize = GUI.HEIGHT / 9;
        int bodySize = Math.min(columnCellSize, rowCellSize) * 2 / 3;
        g.drawImage(image, columnCellSize * 14 + columnCellSize * 4 / 3, rowCellSize * index + rowCellSize / 6, bodySize, bodySize, null);
        g.setColor(Color.RED);
        g.setFont(g.getFont().deriveFont((float)(columnCellSize * 0.15)));
        g.drawString(Integer.toString(remainRounds), columnCellSize * 16 - columnCellSize / 6, rowCellSize * index + rowCellSize / 4);
    }
}
