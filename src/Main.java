import controller.Game;
import middleware.AsyncMessageHandler;
import model.card.*;
import model.mover.*;
import model.role.Role;
import model.world.World;
import model.world.event.*;
import view.GUI;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException, IOException{
        List<Role> roles = new ArrayList<>();
        AsyncMessageHandler messageHandler = new AsyncMessageHandler();
        HashMap<String, Card> nameToCardMapper = cardConfig();
        CardGenerator cardGenerator = new CardGenerator(nameToCardMapper);
        HashMap<String, RichmanEvent> nameToEventMapper = eventConfig(cardGenerator);
        World world = new World(new File("assets/maps/basicMap.txt"), nameToEventMapper, messageHandler);

        Mover mover = new WheelMover();
        roles.add(new Role("Spiderman", 30000, world.randomGetGrid(), cardGenerator.getRandomCards(6), messageHandler, mover));
        roles.add(new Role("Satoshi", 30000, world.randomGetGrid(), cardGenerator.getRandomCards(6), messageHandler, mover));
        roles.add(new Role("Gengar", 30000, world.randomGetGrid(), cardGenerator.getRandomCards(6), messageHandler, mover));

        for(int i = 0; i < roles.size(); i++){
            Role role = roles.get(i);
            world.addSprite(role);
            role.setAllRoles(roles);
            role.setIndex(i);
        }
        world.setMover(mover);
        Game game = new Game(world, roles);
        GUI gui = new GUI(game, messageHandler);
        
        game.start();
        gui.launch();

    }

    private static HashMap<String, RichmanEvent> eventConfig(CardGenerator cardGenerator) {
        HashMap<String, RichmanEvent> nameToEventMapper = new HashMap<>();
        nameToEventMapper.put("null", new NullEvent());
        nameToEventMapper.put("estate", new EstateEvent());
        nameToEventMapper.put("hospital", new HospitalEvent());
        nameToEventMapper.put("dingo", new DingoEvent());
        nameToEventMapper.put("thug", new ThugEvent());       
        nameToEventMapper.put("shop", new ShopEvent(cardGenerator));
        return nameToEventMapper;
    }

    private static HashMap<String, Card> cardConfig() {
        HashMap<String, Card> nameToCardMapper = new HashMap<>();
        nameToCardMapper.put("stay", new Stay());
        nameToCardMapper.put("turnaround", new Turnaround());
        nameToCardMapper.put("wheelcontrol", new WheelControl());
        nameToCardMapper.put("turtle", new Turtle());
        nameToCardMapper.put("stateclear", new StateClear());
        nameToCardMapper.put("robbery", new Robbery());
        nameToCardMapper.put("teleportation", new Teleportation());
        nameToCardMapper.put("speedup", new Speedup());
        return nameToCardMapper;
    }
}
