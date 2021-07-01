package view;

import controller.Game;
import controller.GameLoop;
import middleware.AsyncMessageHandler;
import model.role.Role;
import model.world.World;
import utils.*;

import javax.print.attribute.standard.Media;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame {
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static int HEIGHT = screenSize.height;
    public static int WIDTH = screenSize.width;
    private final Canvas canvas = new Canvas();
    private final Game game;
    private final AsyncMessageHandler messageHandler;

    public GUI(Game game, AsyncMessageHandler messageHandler) throws HeadlessException {
        this.game = game;
        this.messageHandler = messageHandler;
        game.setView(canvas);
    }

    public void launch() {
        // GUI Stuff
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(canvas);
        setSize(WIDTH, HEIGHT);
        setContentPane(canvas);
        setVisible(true);
        
        // Keyboard listener
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                messageHandler.produce(keyEvent);
            }
        });
        detectWindow();
    }

    private void detectWindow(){
        while(true){
            Rectangle realR = this.getContentPane().getBounds();
            HEIGHT = realR.height;
            WIDTH = realR.width;
            Rectangle r = this.getBounds();
            if (HEIGHT / 16 > WIDTH / 9) {
                setSize(r.width, r.width * 9 / 16);
            } else {
                setSize(r.height * 16 / 9, r.height);
            }
            Delay.delay(100);
        }
    }


    public static class Canvas extends JPanel implements GameLoop.View {
        private World world;
        @Override
        public void render(World world) {
            this.world = world;
            repaint();
        }
        private Image background = ImageStateUtils.readImageFromString("assets/background/background.png");
        private Image victory = ImageStateUtils.readImageFromString("assets/background/victoryBackground.png");

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (world.getWinner() != null) {
                g.drawImage(victory, 0, 0, GUI.WIDTH, GUI.HEIGHT, null);
                world.getWinner().winningRender(g);
            } else {
                g.drawImage(background, 0, 0, GUI.WIDTH, GUI.HEIGHT, null);
                world.render(g);
            }
        }
    }
}