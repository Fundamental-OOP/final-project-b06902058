package model.card;

import model.role.Role;
import model.role.state.SpeedupState;
import utils.ImageStateUtils;

public class Speedup extends ChooseRoleCard {
    int choosing;
    public Speedup(){
        super();
        image = ImageStateUtils.readImageFromString("assets/card/speedup.png");
    }

    @Override
    protected boolean affect(Role self, Role targetRole) {
        targetRole.setState(new SpeedupState(3));
        return false;
    }

}