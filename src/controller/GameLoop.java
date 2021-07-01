package controller;

import model.role.Role;
import model.world.World;
import utils.Delay;

import java.util.List;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public abstract class GameLoop {
    protected boolean running;
    protected Role winner;
    private View view;

    public void setView(View view) {
        this.view = view;
    }

    public void start() {
        new Thread(this::renderLoop).start();
        new Thread(() -> {
            try {
                gameLoop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void renderLoop() {
        running = true;
        while (running) {
            World world = getWorld();
            world.update();
            view.render(world);
            Delay.delay(15);
        }
    }

    protected abstract void gameLoop() throws InterruptedException;

    protected abstract World getWorld();

    protected abstract List<Role> getRoles();

    public void stop() {
        running = false;
    }

    public interface View {
        void render(World world);
    }
}