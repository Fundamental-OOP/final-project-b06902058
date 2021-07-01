package model.card;

import model.role.Role;
import utils.ColorUtils;
import view.GUI;

import java.awt.*;
import java.util.List;

public class ChooseRoleCardRenderer extends CardRenderer{
    ChooseRoleCard card;
    public ChooseRoleCardRenderer(ChooseRoleCard card){
        this.card = card;
    }

    @Override
    public void evaluatingRender(Image image, Graphics g, Role role) {
        List<Role> allRoles = role.getAliveRoles();
        int roleNum = allRoles.size();
        int columnCellSize = GUI.WIDTH / 16;
        int rowCellSize = GUI.HEIGHT / 9;
        int bodySize = Math.min(columnCellSize, rowCellSize);
        double startX = (16 - 1.5 * roleNum + 0.5) / 2.;
        for(int i = 0; i < roleNum; i++){
            g.setColor(Color.black);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(5));
            g.drawRect((int)(columnCellSize * (startX + i * 1.5)), 4 * rowCellSize, bodySize, bodySize);
            g.drawImage(allRoles.get(i).getAvatar(), (int)(columnCellSize * (startX + i * 1.5)), 4 * rowCellSize, bodySize, bodySize, null);
        }
        g.setColor(ColorUtils.CHOOSEN_COLOR);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));
        g.drawRect((int)(columnCellSize * (startX + card.getChoosing() * 1.5)), 4 * rowCellSize, bodySize, bodySize);
    }
}
