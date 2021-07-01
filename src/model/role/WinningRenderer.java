package model.role;

import model.unit.ImageRenderer;
import utils.Render3DUtils;
import view.GUI;

import java.awt.*;

public class WinningRenderer implements ImageRenderer{

    protected Role role;

    public WinningRenderer(Role role) {
        this.role = role;
    }

    @Override
    public void render(Image image, Graphics g) {
        int columnCellSize = GUI.WIDTH / 16;
        int rowCellSize = GUI.HEIGHT / 9;
        int width = columnCellSize * 1;
        int height = rowCellSize * 1;
        g.drawImage(image, (int)(7.5 * columnCellSize), (int)(4 * rowCellSize), width, height, null);
    }
}
