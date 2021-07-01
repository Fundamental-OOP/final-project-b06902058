package model.world;

import model.mover.Mover;
import model.role.Role;
import model.world.event.RichmanEvent;
import utils.RandomUtil;
import view.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

import middleware.AsyncMessageHandler;

public class World {
    private List<Grid> grids;
    private List<Role> roles = new CopyOnWriteArrayList<>();
    private Mover mover;
    private Role winner;
    private boolean starting = true;
    private BufferedImage background;
    private List<BufferedImage> entry;
    private int cnt;
    private AsyncMessageHandler messageHandler;
    private static WorldFileReader worldFileReader;

    public World(File worldFile, HashMap<String, RichmanEvent> nameToEventMapper, AsyncMessageHandler messageHandler) {
        World.worldFileReader = new WorldFileReader(nameToEventMapper);
        this.grids = worldFileReader.generateWorldFromFile(worldFile);
        this.messageHandler = messageHandler;
        entry = new ArrayList<>();
        try{
            background = ImageIO.read(new File("assets/entry/start.png"));
            for(int i = 0; i < 10; i++){
                entry.add(ImageIO.read(new File("assets/entry/" + Integer.toString(i) + ".png")));
            }
            for(int i = 8; i > 0; i--){
                entry.add(entry.get(i));
            }
        }
        catch(Exception e){}
    }

    public void setWinner(Role winner) {
        this.winner = winner;
    }

    public Role getWinner() {
        return this.winner;
    }

    public void render(Graphics g) {
        for (Grid grid : grids) {
            grid.render(g);
        }
        Role movingRole = null;
        for (Role role: roles) {
            if(!role.isMoving())
                role.render(g);
            else{
                movingRole = role;
            }
        }
        if(movingRole != null)
            movingRole.render(g);
        mover.render(g);
        for (Grid grid: grids){
            grid.stateRender(g);
        }
        if(starting){
            entryRender(g);
        }
    }

    public void update() {
        for (Role role: roles) {
            role.update();
        }
        mover.update();
    }

    public void addSprite(Role role) {
        roles.add(role);
        role.setWorld(this);
    }
    
    public static class WorldFileReader {
        HashMap<Character, String> charToGridNameMapper = new HashMap<>();
        HashMap<Character, List<Integer>> charToArgumentMapper = new HashMap<>();
        HashMap<String, RichmanEvent> nameToEventMapper;
        HashMap<Point, Grid> positionToGrid;
        Scanner worldReader;
        List<Grid> grids = new ArrayList<>();
        private static final int maxMapSize = 100;
        WorldFileReader(HashMap<String, RichmanEvent> nameToEventMapper){
            this.nameToEventMapper = nameToEventMapper;
            this.positionToGrid = new HashMap<>();
        }
        List<Grid> generateWorldFromFile(File wordFile) {
            try {
                worldReader = new Scanner(wordFile);
                while (worldReader.hasNextLine()) {
                    String line = worldReader.nextLine();
                    if (line.startsWith("//") || line.length() == 0) {
                        continue;
                    } else if (line.equals("START_OF_THE_DEFINITION")) {
                        readDefinitions();
                    } else if (line.equals("START_OF_THE_MAP")) {
                        readMap();
                    } else {
                        System.err.printf("[WorldFileReader] Unknown line for worldFile %s : %s\n", wordFile.getName(), line);
                    }
                }
                worldReader.close();
            } catch (FileNotFoundException | CloneNotSupportedException e) {
                System.err.println("[WorldFileReader] map file not found");
                e.printStackTrace();
            }
            for(int i = 0; i < grids.size(); i++){
                grids.get(i).getPositionToGridMapper(positionToGrid);
            }
            return grids;
        }

        private void readDefinitions() {
            while (worldReader.hasNextLine()) {
                String line = worldReader.nextLine();
                if (line.equals("END_OF_THE_DEFINITION")) {

                    return;
                } else {
                    String[] lineArray = line.split(" ");
                    Character character = lineArray[0].charAt(0);
                    String gridName = lineArray[1];
                    List<Integer> arguments = new ArrayList<>();
                    for (int i = 2; i < lineArray.length; i++) {
                        arguments.add(Integer.parseInt(lineArray[2]));
                    }
                    charToGridNameMapper.put(character, gridName);
                    charToArgumentMapper.put(character, arguments);
                }
            }
        }

        private void readMap() throws CloneNotSupportedException {
            char[][] mapArray = new char[maxMapSize][maxMapSize];

            int nowAtLine = 0;
            while (worldReader.hasNextLine()) {
                String line = worldReader.nextLine();
                System.out.println(line);
                if (line.equals("END_OF_THE_MAP")) {
                    break;
                } else {
                    for (int i = 0; i < line.length(); i++) {
                        mapArray[nowAtLine][i] = line.charAt(i);
                    }
                    nowAtLine += 1;
                }
            }

            Grid[][] gridArray = mapArrayToGridArray(mapArray);
            for (int x = 0; x < maxMapSize; x++) {
                for (int y = 0; y < maxMapSize; y++) {
                    if (gridArray[x][y] == null) continue;
                    addNeighbor(gridArray, x, y, x+1, y);
                    addNeighbor(gridArray, x, y, x-1, y);
                    addNeighbor(gridArray, x, y, x, y+1);
                    addNeighbor(gridArray, x, y, x, y-1);

                    grids.add(gridArray[x][y]);
                }
            }
        }

        private Grid[][] mapArrayToGridArray(char[][] mapArray) throws CloneNotSupportedException {
            Grid[][] gridArray = new Grid[maxMapSize][maxMapSize];
            for (int x = 0; x < maxMapSize; x++) {
                for (int y = 0; y < maxMapSize; y++) {
                    if (!charToGridNameMapper.containsKey(mapArray[x][y])) {
                        continue;
                    }

                    RichmanEvent event;
                    if (this.nameToEventMapper.containsKey(this.charToGridNameMapper.get(mapArray[x][y]))){
                        event = nameToEventMapper.get(this.charToGridNameMapper.get(mapArray[x][y])).clone();
                    }
                    else{
                        event = nameToEventMapper.get("nullevent").clone();
                    }

                    event.setArgs(this.charToArgumentMapper.get(mapArray[x][y]));
                    
                    gridArray[x][y] = new Grid(
                            new Point(100 * x, 100 * y),
                            event,
                            charToGridNameMapper.get(mapArray[x][y])
                    );
                    Point position = new Point(x,y);
                    this.positionToGrid.put(position, gridArray[x][y]);
                }
            }
            return gridArray;
        }

        private void addNeighbor(Grid[][] gridArray, int x, int y, int nx, int ny) {
            if (outOfBound(x, y) || outOfBound(nx, ny)) {
                return;
            } else if (gridArray[x][y] == null || gridArray[nx][ny] == null) {
                return;
            }

            gridArray[x][y].addNeighbor(gridArray[nx][ny]);
        }

        private boolean outOfBound(int x, int y) {
            if (x < 0 || y < 0 || x >= maxMapSize || y >= maxMapSize) {
                return true;
            }
            return false;
        }
    }

    public Grid randomGetGrid() {
        int index = RandomUtil.getRandomInt(grids.size());
        Grid grid = grids.get(index);
        return grid;
    }

    public void setMover(Mover mover) {
        this.mover = mover;
    }

    public void waitForPress() throws InterruptedException {
        messageHandler.clear();
        while(true){
            KeyEvent keyEvent = messageHandler.consume();
            switch (keyEvent.getKeyCode()) {
                default:
                    this.starting = false;
                    break;
            }
            break;
        }
    }
    private void entryRender(Graphics g) {
        g.drawImage(background, 0, 0, GUI.WIDTH, GUI.HEIGHT, null);
        BufferedImage text = entry.get(cnt/4);
        int x = GUI.WIDTH/2 - text.getWidth()/2;
        int y = GUI.HEIGHT - text.getHeight();
        g.drawImage(text, x, y, text.getWidth(), text.getHeight(), null);
        cnt += 1;
        cnt %= entry.size();
    }
}


