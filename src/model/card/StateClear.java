package model.card;

import model.role.Role;
import model.role.state.Normal;
import utils.ImageStateUtils;

public class StateClear extends ChooseRoleCard {
    int choosing;
    public StateClear(){
        super();
        image = ImageStateUtils.readImageFromString("assets/card/stateclear.png");
    }

    @Override
    protected boolean affect(Role self, Role targetRole) {
        targetRole.setState(new Normal());
        return false;
    }

}