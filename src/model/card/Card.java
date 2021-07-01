package model.card;

import model.role.Role;
import utils.Delay;
import middleware.AsyncMessageHandler;

import java.awt.*;

public abstract class Card implements Cloneable{
    protected Image image;
    protected CardRenderer renderer;
    protected boolean using = false;
    protected boolean evaluating = false;
    public abstract boolean evaluate(Role role, AsyncMessageHandler messageHandler) throws InterruptedException;
    public void render(Graphics g, int index, boolean chosen, Role role){
        if(using)
            renderer.usingRender(image, g);
        else if(evaluating)
            renderer.evaluatingRender(image, g, role);
        else
            renderer.render(image, g, index, chosen);
    }
    protected void using(){
        using = true;
        Delay.delay(1000);
        using = false;
    }

    public void renderAtShop(Graphics g, int index, boolean chosen, int price){
        renderer.renderAtShop(image, g, index, chosen, price);
    }

    public Card clone() throws CloneNotSupportedException {
        return (Card) super.clone();
    }
}
