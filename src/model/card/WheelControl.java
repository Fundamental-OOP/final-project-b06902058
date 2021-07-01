package model.card;

import model.mover.Mover;
import model.mover.WheelMover;
import model.role.Role;
import utils.ImageStateUtils;
import middleware.AsyncMessageHandler;

public class WheelControl extends Card {
    
    public WheelControl(){
        super();
        image = ImageStateUtils.readImageFromString("assets/card/wheelcontrol.png");
        renderer = new CardRenderer();
    }
    @Override
    public boolean evaluate(Role role, AsyncMessageHandler messageHandler) throws InterruptedException{
        using();
        Mover mover = role.getMover();
        if(mover instanceof WheelMover){
            WheelMover wheelmover = (WheelMover)mover;
            wheelmover.setRotateSpeed(5);
        }
        return false;
    }

}