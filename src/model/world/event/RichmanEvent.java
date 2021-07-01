package model.world.event;

import model.role.Role;
import model.world.Grid;
import middleware.AsyncMessageHandler;

import java.util.HashMap;
import java.util.List;
import java.awt.*;


public interface RichmanEvent {
    void render(Point position, Graphics g);
    void trigger(Role caster, AsyncMessageHandler handler, List<Role> castee) throws InterruptedException;
    void setArgs(List<Integer> arguments);
    void getPositionToGridMapper(HashMap<Point, Grid> positionToGrid);
    RichmanEvent clone() throws CloneNotSupportedException;
    void stateRender(Graphics g);
}
