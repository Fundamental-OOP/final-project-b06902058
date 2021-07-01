package model.mover;

import model.unit.ImageRenderer;

import java.awt.*;

public class WheelMoverRenderer implements ImageRenderer{
    private WheelMover mover;
    public WheelMoverRenderer(WheelMover mover) {
        this.mover = mover;
    }

    @Override
    public void render(Image image, Graphics g) {
        Rectangle range = mover.getRange();
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.rotate(Math.toRadians(-mover.getRotate()), range.x + range.width / 2, range.y + range.height / 2);
        g.drawImage(image, range.x, range.y, range.width, range.height, null);
        g2d.rotate(Math.toRadians(mover.getRotate()), range.x + range.width / 2, range.y + range.height / 2);
        g2d.setStroke(new BasicStroke(3));
        g.setColor(Color.BLACK);
        g.drawLine(range.x + range.width / 2, range.y + range.height / 2, range.x + range.width / 5, range.y + range.height / 2);
        g.fillPolygon(
            new int[]{range.x + range.width / 3, range.x + range.width / 3, range.x + range.width / 6}, 
            new int[]{range.y + range.height * 5 / 12, range.y + range.height * 7 / 12, range.y + range.height / 2}, 
            3
        );
        // g.setColor(Color.RED);
        // g.drawRect(body.x, body.y, body.width, body.height);
    }
}
