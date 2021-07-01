package model.role;

import model.unit.CyclicSequence;
import model.unit.ImageState;

import java.util.List;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Walking extends CyclicSequence {
    private final Role role;

    public Walking(Role role, List<ImageState> states) {
        super(states);
        this.role = role;
    }

    @Override
    public void update() {
        if (role.isAlive()) {
            super.update();
            // TODO
            // role.getWorld().move(role); // when role move there may be collision which the world should handle
        }
    }

    @Override
    public String toString() {
        return "Walking";
    }
}

