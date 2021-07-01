package model.card;

import model.role.Role;
import middleware.AsyncMessageHandler;

import java.awt.event.KeyEvent;
import java.util.List;

public abstract class ChooseRoleCard extends Card {
    int choosing;
    public ChooseRoleCard(){
        super();
        renderer = new ChooseRoleCardRenderer(this);
        choosing = 0;
    }
    @Override
    public  boolean evaluate(Role role, AsyncMessageHandler messageHandler) throws InterruptedException{
        using();
        Role targetRole = decideRole(role, messageHandler);
        boolean done = affect(role, targetRole);
        return done;
    }

    protected abstract boolean affect(Role self, Role targetRole);

    protected Role decideRole(Role role, AsyncMessageHandler messageHandler) throws InterruptedException{
        List<Role> allRoles = role.getAliveRoles();
        int roleNum = allRoles.size();
        evaluating = true;
        messageHandler.clear();
        while(true){
            KeyEvent keyEvent = messageHandler.consume();
            int keyCode = keyEvent.getKeyCode();
            switch(keyCode){
                case KeyEvent.VK_LEFT:
                    choosing = ((choosing - 1) + roleNum) % roleNum;
                    break;
                case KeyEvent.VK_RIGHT:
                    choosing = (choosing + 1) % roleNum;
                    break;
                case KeyEvent.VK_ENTER:
                    allRoles.get(choosing);
                    evaluating = false;
                    return allRoles.get(choosing);
            }
        }
    }

    public int getChoosing(){
        return this.choosing;
    }

    @Override
    public ChooseRoleCard clone() throws CloneNotSupportedException {
        ChooseRoleCard card = (ChooseRoleCard) super.clone();
        card.renderer = new ChooseRoleCardRenderer(card);
        return card;
    }
}