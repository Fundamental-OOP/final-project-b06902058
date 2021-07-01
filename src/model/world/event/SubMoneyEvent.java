package model.world.event;

import middleware.AsyncMessageHandler;
import model.role.Role;

import java.awt.*;
import java.util.List;

public class SubMoneyEvent extends RichmanEventAbstract{
    private int amount;


    public SubMoneyEvent(int amount){
        this.amount = amount;
    }
    @Override
    public void render(Point position, Graphics g) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void trigger(Role caster, AsyncMessageHandler handler, List<Role> castee) {
        // TODO Auto-generated method stub
        caster.subMoney(this.amount);
    }

    @Override
    public void setArgs(List<Integer> arguments) {
        assert arguments.size() == 1;
        this.amount = arguments.get(0);
    }
    @Override
    public void stateRender(Graphics g) {
        // TODO Auto-generated method stub
        
    }
}
