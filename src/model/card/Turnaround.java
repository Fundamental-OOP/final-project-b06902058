package model.card;

import model.role.Role;
import utils.ImageStateUtils;

public class Turnaround extends ChooseRoleCard {
    public Turnaround(){
        super();
        image = ImageStateUtils.readImageFromString("assets/card/turnaround.png");
    }

    @Override
    protected boolean affect(Role self, Role targetRole) {
        targetRole.turnaround();
        return false;
    }

}