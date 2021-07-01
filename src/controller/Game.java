package controller;

import model.world.World;
import model.role.Role;

import java.util.List;

public class Game extends GameLoop {

    private final World world;
    private final List<Role> roles;

    public Game(World world, List<Role> roles) {
        this.world = world;
        this.roles = roles;
    }

    @Override
    protected void gameLoop() throws InterruptedException {
        world.waitForPress();
        while (running) {
            for (Role role : roles) {
                if (role.isWinner()) {
                    world.setWinner(role);
                    break;
                }
                if(role.isAlive()){
                    role.takeAction();
                }
                
            }
        }
    }

    @Override
    protected World getWorld() {
        return this.world;
    }

    @Override
    protected List<Role> getRoles() {
        return this.roles;
    }

}
