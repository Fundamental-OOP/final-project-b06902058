package model.role.state;

import model.mover.Mover;
import model.role.Role;
import utils.ImageStateUtils;
import middleware.AsyncMessageHandler;

public class InHospital extends NotNormal {

    public InHospital(int remainRounds) {
        this.remainRounds = remainRounds;
        image = ImageStateUtils.readImageFromString("assets/state/hospital.png");
    }

    @Override
    public void evaluate(Role role) {
        return;
    }

    @Override
    public boolean actionable() {
        return false;
    }

    @Override
    public int decideMoveStep(Mover mover, AsyncMessageHandler messageHandler) {
        return -1;
    }
}
