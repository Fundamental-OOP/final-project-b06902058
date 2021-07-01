package model.mover;

import middleware.AsyncMessageHandler;
import utils.ImageStateUtils;

import java.awt.event.KeyEvent;
import java.awt.*;

public class WheelMover extends Mover{
    private int rotate;
    private boolean updating = false;
    private int defaultRotateSpeed = 23;
    private int rotateSpeed = defaultRotateSpeed;

    public WheelMover(){
        super();
        image = ImageStateUtils.readImageFromString("assets/mover/wheel/wheel.png");
        renderer = new WheelMoverRenderer(this);
        rendering = false;
    }

    @Override
    public int decideMoveStep(AsyncMessageHandler messageHandler) throws InterruptedException {
        updating = true;
        messageHandler.clear();
        while(true){
            KeyEvent keyEvent = messageHandler.consume();
            if(keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
                int steps = getSteps();
                messageHandler.clear();
                updating = false;
                return steps;
            }
        }
    }

    private int getSteps(){
        return 1 + (4 + ((rotate + 30) / 60)) % 6;
    }

    @Override
    public void render(Graphics g) {
        if(rendering){
            renderer.render(image, g);
        }
    }

    @Override
    public void update() {
        if(updating){
            rotate += rotateSpeed;
            if(rotate == 360)
                rotate = 0;
        }
    }
    @Override
    public void reset(){
        rotateSpeed = defaultRotateSpeed;
    }

    public int getRotate(){
        return rotate;
    }

    public void setRotateSpeed(int rotateSpeed){
        this.rotateSpeed = rotateSpeed;
    }

    public void setDefaultSpeed(){
        this.rotateSpeed = defaultRotateSpeed;
    }
    
}
