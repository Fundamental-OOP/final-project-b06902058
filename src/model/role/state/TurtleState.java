package model.role.state;

import model.role.Role;
import utils.ImageStateUtils;
import middleware.AsyncMessageHandler;
import model.mover.Mover;

public class TurtleState extends DecideMove {

    public TurtleState(int remainRounds) {
        this.remainRounds = remainRounds;
        image = ImageStateUtils.readImageFromString("assets/state/turtle.png");
    }

    @Override
    public void evaluate(Role role) {
        return;
    }

    @Override
    public boolean actionable() {
        return true;
    }

    @Override
    public int decideMoveStep(Mover mover, AsyncMessageHandler messageHandler) {
        return 1;
    }
}
