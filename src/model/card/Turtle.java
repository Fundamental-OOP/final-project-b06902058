package model.card;

import model.role.Role;
import model.role.state.TurtleState;
import utils.ImageStateUtils;

public class Turtle extends ChooseRoleCard {
    int choosing;
    public Turtle(){
        super();
        image = ImageStateUtils.readImageFromString("assets/card/turtle.png");
    }

    @Override
    protected boolean affect(Role self, Role targetRole) {
        targetRole.setState(new TurtleState(3));
        return false;
    }

}