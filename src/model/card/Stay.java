package model.card;

import model.role.Role;
import utils.ImageStateUtils;

import middleware.AsyncMessageHandler;
public class Stay extends Card {
    public Stay(){
        super();
        image = ImageStateUtils.readImageFromString("assets/card/stay.png");
        renderer = new CardRenderer();
    }
    @Override
    public boolean evaluate(Role role, AsyncMessageHandler messageHandler) throws InterruptedException{
        using();
        return true;
    }
}