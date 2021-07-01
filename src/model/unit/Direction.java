package model.unit;

import model.world.Grid;

import java.awt.*;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public static Direction getDirection(Grid grid, Grid nextGrid) {
        int deltaX = (int)nextGrid.getPosition().getX() - (int)grid.getPosition().getX();
        int deltaY = (int)nextGrid.getPosition().getY() - (int)grid.getPosition().getY();
        
        int x = (deltaX == 0)? 0: (deltaX / Math.abs(deltaX));
        int y = (deltaY == 0)? 0: (deltaY / Math.abs(deltaY));
        Dimension dimension = new Dimension(x, y);
        if(dimension.equals(new Dimension(0, -1)))
            return UP;
        else if(dimension.equals(new Dimension(0, 1)))
            return DOWN;
        else if(dimension.equals(new Dimension(-1, 0)))
            return LEFT;
        else if(dimension.equals(new Dimension(1, 0)))
            return RIGHT;
        throw new IllegalStateException("Impossible");
    }

    public static String toString(Direction direction) {
        switch (direction) {
            case UP:
                return "up";
            case DOWN:
                return "down";
            case LEFT:
                return "left";
            case RIGHT:
                return "right";
            default:
                throw new IllegalStateException("Impossible");
        }
    }

}