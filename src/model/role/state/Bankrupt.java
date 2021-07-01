package model.role.state;

import model.role.Role;
import model.mover.Mover;
import utils.ImageStateUtils;
import view.GUI;
import middleware.AsyncMessageHandler;

import java.awt.*;

public class Bankrupt implements RoleState {
    Image image = ImageStateUtils.readImageFromString("assets/state/bankrupt.png");

    @Override
    public void evaluate(Role role) {
        return;
    }

    @Override
    public void roundOver(Role role) {
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

    @Override
    public void render(Graphics g, int index) {
        int columnCellSize = GUI.WIDTH / 16;
        int rowCellSize = GUI.HEIGHT / 9;
        g.drawImage(image, columnCellSize * 14, rowCellSize * index, columnCellSize * 2, rowCellSize, null);
        return;
    }

    @Override
    public boolean decidable() {
        return false;
    }

}
