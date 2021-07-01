package model.world.event;

import middleware.AsyncMessageHandler;
import model.role.Role;

import java.awt.*;
import java.util.List;

public class NullEvent extends RichmanEventAbstract{

    @Override
    public void render(Point position, Graphics g) {
        // TODO Auto-generated method stub
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
