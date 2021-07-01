package model.role.state;

import middleware.AsyncMessageHandler;
import model.mover.Mover;
import model.role.Role;

import java.awt.*;

public interface RoleState {
    void evaluate(Role role);
    void roundOver(Role role);
    boolean actionable();
    int decideMoveStep(Mover mover, AsyncMessageHandler messageHandler) throws InterruptedException;
    boolean decidable();
    void render(Graphics g, int index);
}
