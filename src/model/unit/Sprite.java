package model.unit;

import java.awt.*;
import model.world.*;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public abstract class Sprite {
    protected World world;
    protected Grid grid;
    protected Direction face;;

    public abstract void update();

    public abstract void render(Graphics g);

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Grid getGrid() {
        return grid;
    }

    public int getX() {
        return (int)getRange().getX();
    }

    public int getY() {
        return (int)getRange().getY();
    }

    public abstract Rectangle getRange();

    public abstract Dimension getBodyOffset();

    public abstract Dimension getBodySize();

    public Direction getFace() {
        return face;
    }

    public void setFace(Direction face) {
        this.face = face;
    }

    public Rectangle getBody() {
        return getArea(getBodyOffset(), getBodySize());
    }

    public Rectangle getArea(Dimension offset, Dimension size) {
        return new Rectangle(
            new Point(
                offset.width + this.getX(),
                offset.height + this.getY()
            ),
            size
        );
    }

}
