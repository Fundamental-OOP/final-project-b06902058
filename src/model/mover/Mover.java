package model.mover;

import middleware.AsyncMessageHandler;
import model.unit.ImageRenderer;
import view.GUI;

import java.awt.*;

public abstract class Mover {
    protected Image image;
    protected boolean rendering;
    protected ImageRenderer renderer;
    protected Mover(){
        
    }
    public abstract int decideMoveStep(AsyncMessageHandler messageHandler) throws InterruptedException;
    public abstract void render(Graphics g);
    public Rectangle getRange(){
        int bodySize = Math.min(GUI.WIDTH*2/16, GUI.HEIGHT*2/9);
        return new Rectangle(new Point(GUI.WIDTH * 14/16, GUI.HEIGHT*7/9), new Dimension(bodySize, bodySize));
    }
    public void setRendering(boolean value){
        rendering = value;
    }
    public abstract void update();
    public abstract void reset();
}
