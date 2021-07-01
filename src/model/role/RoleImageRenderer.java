package model.role;

import model.unit.ImageRenderer;
import utils.Render3DUtils;

import java.awt.*;

public class RoleImageRenderer implements ImageRenderer{

    protected Role role;

    public RoleImageRenderer(Role role) {
        this.role = role;
    }

    @Override
    public void render(Image image, Graphics g) {
        Rectangle range = role.getRange();
        Render3DUtils.drawRole(image, g, range.x, range.y, range.width, range.height);
    }
}
