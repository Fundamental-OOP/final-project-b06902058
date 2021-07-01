package model.world;

import middleware.AsyncMessageHandler;
import model.role.Role;
import model.world.event.EstateEvent;
import model.world.event.RichmanEvent;
import utils.Render3DUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Grid {

    private Point position;
    private RichmanEvent event;
    private List<Grid> neighbors;
    private String name;

    public Grid(Point position, RichmanEvent event, String name){
        this.position = position;
        this.event = event;
        this.name = name;
        this.neighbors = new ArrayList<>();
    }

    public void trigger(Role caster, AsyncMessageHandler handler, List<Role> castee) throws InterruptedException {
        this.event.trigger(caster, handler, castee);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void addNeighbor(Grid neighbor) {
        this.neighbors.add(neighbor);
    }

    public List<Grid> getNeighbors(){
        return new ArrayList<>(this.neighbors);
    }

    public void render(Graphics g){
        // Draw the grid first
        Render3DUtils.drawDiamond(position, g);
        this.event.render(this.position, g);
    }

    public void getPositionToGridMapper(HashMap<Point, Grid> positionToGrid) {
        this.event.getPositionToGridMapper(positionToGrid);
    }

    public void stateRender(Graphics g) {
        this.event.stateRender(g);
    }
}