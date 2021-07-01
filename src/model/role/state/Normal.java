package model.role.state;

import model.role.Role;
import model.mover.Mover;
import middleware.AsyncMessageHandler;

import java.awt.Graphics;

public class Normal implements RoleState {

    @Override
    public void evaluate(Role role) {
        return;
    }

    @Override
    public void roundOver(Role role) {
        return;
    }

    @Override
    public boolean actionable() {
        return true;
    }

    @Override
    public int decideMoveStep(Mover mover, AsyncMessageHandler messageHandler) {
        return -1;
    }

    
    @Override
    public void render(Graphics g, int index) {
        return;
    }

    @Override
    public boolean decidable() {
        return false;
    }

}
