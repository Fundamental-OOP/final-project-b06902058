package model.world.event;

import middleware.AsyncMessageHandler;
import model.role.Role;
import model.world.Grid;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public abstract class RichmanEventAbstract implements RichmanEvent, Cloneable {
    protected HashMap<Point, Grid> positionToGrid;

    @Override
    public RichmanEvent clone() throws CloneNotSupportedException {
        return (RichmanEvent) super.clone();
    }
    public abstract void render(Point position, Graphics g);
    public abstract void stateRender(Graphics g);
    public abstract void trigger(Role caster, AsyncMessageHandler handler, List<Role> castee) throws InterruptedException;
    public abstract void setArgs(List<Integer> arguments);
    public void getPositionToGridMapper(HashMap<Point, Grid> positionToGrid){
        this.positionToGrid = positionToGrid;
    }
}

