package model.card;

import model.role.Role;
import utils.ImageStateUtils;

public class Teleportation extends ChooseRoleCard {
    int choosing;
    public Teleportation(){
        super();
        image = ImageStateUtils.readImageFromString("assets/card/teleportation.png");
    }

    @Override
    protected boolean affect(Role self, Role targetRole) {
        targetRole.setGrid(targetRole.getWorld().randomGetGrid());
        return false;
    }

}