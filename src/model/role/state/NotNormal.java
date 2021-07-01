package model.role.state;

import model.role.Role;
import middleware.AsyncMessageHandler;
import model.mover.Mover;

import java.awt.*;

public abstract class NotNormal implements RoleState {
    protected int remainRounds;
    protected Image image;
    protected NotNormalRenderer renderer = new NotNormalRenderer();

    @Override
    public void roundOver(Role role) {
        remainRounds -= 1;
        if (remainRounds <= 0)
            role.setState(new Normal());
        return;
    }

    public void render(Graphics g, int index){
        renderer.render(image, g, index, remainRounds);
    }

    public boolean decidable(){
        return false;
    }
    public int decideMoveStep(Mover mover, AsyncMessageHandler messageHandler) throws InterruptedException{
        return -1;
    }

}
