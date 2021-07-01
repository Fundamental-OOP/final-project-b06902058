package model.card;

import model.role.Role;
import utils.ImageStateUtils;

public class Robbery extends ChooseRoleCard {
    int choosing;
    public Robbery(){
        super();
        image = ImageStateUtils.readImageFromString("assets/card/robbery.png");
    }

    @Override
    protected boolean affect(Role self, Role targetRole) {
        int money = targetRole.getMoney() / 5;
        self.addMoney(money);
        targetRole.subMoney(money);
        return false;
    }

}