package model.world.event;

import middleware.AsyncMessageHandler;
import view.GUI;
import model.role.Role;
import utils.ImageStateUtils;

import java.awt.*;
import java.util.List;

public class HospitalEvent extends RichmanEventAbstract{
    private Image im;
    public HospitalEvent(){
        im = ImageStateUtils.readImageFromString("assets/event/hospital/hospital.png");
    }
    @Override
    public void render(Point position, Graphics g){
        
        int newX = 610 + (int) (0.92 * (position.getX() - position.getY()));
        int newY = 200 + (int) (0.32 * (position.getX() + position.getY()));

        newX = (int) ((double) newX * GUI.HEIGHT / 810);
        newY = (int) ((double) newY * GUI.WIDTH / 1440);
        g.drawImage(im, newX + 10 *GUI.WIDTH/1440, newY - 98 * GUI.HEIGHT / 810, im.getWidth(null) * 100 * GUI.WIDTH / (1440*im.getHeight(null)), 100 * GUI.HEIGHT / 810, null);

    }
    @Override
    public void trigger(Role caster, AsyncMessageHandler handler, List<Role> castee) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void setArgs(List<Integer> arguments) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void stateRender(Graphics g) {
        // TODO Auto-generated method stub
        
    }
}
