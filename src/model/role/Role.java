package model.role;

import middleware.AsyncMessageHandler;
import model.card.*;
import model.mover.Mover;
import model.role.state.Bankrupt;
import model.role.state.Normal;
import model.role.state.RoleState;
import model.world.Grid;
import model.world.event.EstateEvent;
import model.unit.*;
import utils.*;
import view.GUI;
import static utils.ImageStateUtils.imageStatesFromFolder;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;



public class Role extends Sprite{
    private String name;
    private int money;
    private List<Card> cards = new CopyOnWriteArrayList<>();
    private final SpriteShape shape;
    private State idleState;
    private State walkingState;
    private State currentState;
    private double nextGridPercentage;
    private AsyncMessageHandler messageHandler;
    private Direction direction;
    private Grid previousGrid;
    private Grid nextGrid;
    private HashMap<Direction, State> directionToIdleState = new HashMap<>();
    private HashMap<Direction, State> directionToWalkingState = new HashMap<>();
    private List<Role> allRoles;
    private Mover mover;
    private int index;
    private State winningWalkingState;

    private Image avatar;
    private boolean moving = false;
    private AvatarRenderer avatarRenderer = new AvatarRenderer();

    private RoleState state;
    private int cardChoosing;
    private Image board;
    private List<EstateEvent> estates;


    public Role(String name, int money, Grid grid, List<Card> cards, AsyncMessageHandler messageHandler, Mover mover) {
        super();
        this.name = name;
        this.money = money;
        this.grid = grid;
        this.messageHandler = messageHandler;
        this.mover = mover;
        this.cards.addAll(cards);
        this.avatar = ImageStateUtils.readImageFromString(Paths.get("assets", "roles", name, "avatar", "avatar.png").toString());
        this.board = ImageStateUtils.readImageFromString(Paths.get("assets", "roles", name,  "board" , "board.png").toString());
        this.state = new Normal();
        this.estates = new ArrayList<>();
        shape = new SpriteShape(new Dimension(64, 64), new Dimension(0, 0), new Dimension(64, 64));
        setImageState(name);
        initializeForMove();
    }

    public void setState(RoleState state) {
        this.state = state;
    }

    public void setAllRoles(List<Role> allRoles) {
        this.allRoles = allRoles;
    }

    public List<Role> getAliveRoles(){
        List<Role> aliveRoles = new ArrayList<>();
        for(Role role: allRoles){
            if(role.isAlive()){
                aliveRoles.add(role);
            }
        }
        return aliveRoles;
    }

    public boolean isWinner(){
        if (!this.isAlive()) {
            return false;
        }
        for(Role role: allRoles){
            if(role != this && role.isAlive()){
                return false;
            }
        }
        return true;
    }
    
    public boolean isAlive(){
        return money >= 0;
    }

    public void setIndex(int index){
        this.index = index;
    }

    private void setImageState(String name){
        RoleImageRenderer imageRenderer = new RoleImageRenderer(this);
        for(Direction direction: Direction.values()){
            String idlePath = Paths.get("assets", "roles", name, "idle", Direction.toString(direction)).toString();
            directionToIdleState.put(direction, new Idle(imageStatesFromFolder(idlePath, imageRenderer)));
            String walkingPath = Paths.get("assets", "roles", name, "walking", Direction.toString(direction)).toString();
            directionToWalkingState.put(direction, new WaitingPerFrame(8, new Walking(this, imageStatesFromFolder(walkingPath, imageRenderer))));
        }
        WinningRenderer winningRenderer = new WinningRenderer(this);
        String walkingPath = Paths.get("assets", "roles", name, "walking", Direction.toString(Direction.RIGHT)).toString();
        winningWalkingState = new WaitingPerFrame(8, new Walking(this, imageStatesFromFolder(walkingPath, winningRenderer)));
    }

    private void initializeForMove(){
        nextGridPercentage = 0;
        List<Grid> neighbors = grid.getNeighbors();
        previousGrid = neighbors.get(RandomUtil.getRandomInt(neighbors.size()));
        nextGrid = getNextGrid();
        direction = Direction.getDirection(grid, nextGrid);
        idleState = directionToIdleState.get(direction);
        walkingState = directionToWalkingState.get(direction);
        currentState = idleState;
    }

    public void takeAction() throws InterruptedException {
        
        startMove();
        if (!state.actionable()) {
            Delay.delay(1000);
            finishMove();
            return;
        }
        while(true){
            KeyEvent keyEvent = messageHandler.consume();
            int keyCode = keyEvent.getKeyCode();
            if(keyCode == KeyEvent.VK_SPACE){
                int steps;
                if(state.decidable())
                    steps = state.decideMoveStep(mover, messageHandler);
                else
                    steps = mover.decideMoveStep(messageHandler);
                moveSteps(steps);
                this.grid.trigger(this, this.messageHandler, allRoles);
                finishMove();
                return;
            }
            else if(keyCode == KeyEvent.VK_ENTER){
                int cardIndex = cardChoosing;
                if(cards.size() > cardIndex){
                    boolean done = cards.get(cardIndex).evaluate(this, messageHandler);
                    cards.remove(cardIndex);
                    if(done){
                        this.grid.trigger(this, this.messageHandler, allRoles);
                        finishMove();
                        return;
                    }else{
                        takeAction();
                        return;
                    }
                }
            }
            else if(keyCode == KeyEvent.VK_LEFT){
                if(cards.size() != 0)
                    cardChoosing = ((cardChoosing - 1) + cards.size()) % cards.size();
            }
            else if(keyCode == KeyEvent.VK_RIGHT){
                if(cards.size() != 0)
                    cardChoosing = (cardChoosing + 1) % cards.size();
            }
        }
    }

    private void startMove(){
        cardChoosing = 0;
        mover.setRendering(true);
        moving = true;
        state.evaluate(this);
        messageHandler.clear();
    }

    private void finishMove(){
        if(money < 0){
            state = new Bankrupt();
            for(int i = 0; i < this.estates.size(); i++){
                this.estates.get(i).init();      
            }
            this.estates.clear();
        }
        mover.setRendering(false);
        mover.reset();
        moving = false;
        state.roundOver(this);
        messageHandler.clear();
    }

    public void moveSteps(int steps){
        currentState = walkingState;
        for(int i = 0; i < steps; i++){
            while(nextGridPercentage < 1.){
                nextGridPercentage += 0.01;
                Delay.delay(5);
            }
            reachNewGrid();
            currentState = walkingState;
        }
        currentState = idleState;
    }

    private Grid getNextGrid() {
        List<Grid> neighbors = grid.getNeighbors();
        if(neighbors.size() == 1){
            return neighbors.get(0);
        }else{
            neighbors.remove(previousGrid);
            return neighbors.get(RandomUtil.getRandomInt(neighbors.size()));
        }
    }

    private void reachNewGrid() {
        nextGridPercentage = 0;
        previousGrid = grid;
        grid = nextGrid;
        nextGrid = getNextGrid();
        updateDirection();
    }

    public void update() {
        currentState.update();
    }

    @Override
    public void render(Graphics g) {
        currentState.render(g);
        state.render(g, index);
        statusRender(g);
        if(moving){
            for(int i = 0; i < cards.size(); i++){
                cards.get(i).render(g, i, i == cardChoosing, this);
            }
            avatarRenderer.render(avatar, g);
        }
    }

    public void winningRender(Graphics g) {
        winningWalkingState.render(g);
        winningWalkingState.update();
    }


    public void statusRender(Graphics g){
        int columnCellSize = GUI.WIDTH / 16;
        int rowCellSize = GUI.HEIGHT / 9;
        g.setColor(ColorUtils.STATUS_COLOR);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));
        g.drawRect(columnCellSize * 14, rowCellSize * index, columnCellSize * 2, rowCellSize);
        g.setColor(Color.BLACK);
        int bodySize = Math.min(columnCellSize, rowCellSize) * 2 / 3;
        g.drawImage(avatar, columnCellSize * 14, rowCellSize * index + rowCellSize / 6, bodySize, bodySize, null);
        g.setFont(g.getFont().deriveFont((float)(columnCellSize * 0.15)));
        g.drawString(name, columnCellSize * 14 + columnCellSize * 2 / 3, rowCellSize * index + rowCellSize / 3);
        g.drawString(Integer.toString(money), columnCellSize * 14 + columnCellSize * 2 / 3, rowCellSize * index + rowCellSize * 2 / 3);
    }

    @Override
    public Rectangle getRange() {
        return new Rectangle(getPosition(), shape.size);
    }

    private Point getPosition() {
        Point position = new Point(
            (int)((1 - nextGridPercentage) * grid.getPosition().getX() + nextGridPercentage * nextGrid.getPosition().getX()),
            (int)((1 - nextGridPercentage) * grid.getPosition().getY() + nextGridPercentage * nextGrid.getPosition().getY())
        );
        return position;
    }
    @Override
    public Dimension getBodyOffset() {
        return shape.bodyOffset;
    }

    @Override
    public Dimension getBodySize() {
        return shape.bodySize;
    }

    public void subMoney(int money) {
        this.money -= money;
    }
    
    public void addMoney(int money){
        this.money += money;
    }

    public String getName() {
        return this.name;
    }

    public int getMoney() {
        return this.money;
    }


    public Image getAvatar(){
        return this.avatar;
    }

    private void updateDirection(){
        direction = Direction.getDirection(grid, nextGrid);
        idleState = directionToIdleState.get(direction);
        walkingState = directionToWalkingState.get(direction);
        currentState = idleState;
    }

    public void turnaround(){
        previousGrid = getNextGrid();
        nextGrid = getNextGrid();
        updateDirection();
    }
    
    public void setGrid(Grid grid){
        this.grid = grid;
        initializeForMove();
    }

    public Mover getMover(){
        return mover;
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public int getCardNum() {
        return this.cards.size();
    }

    public void setBoard(Image board) {
        this.board = board;
    }
    public Image getBoard(){
        return this.board;
    }
    public boolean isMoving(){
        return moving;
    }

    public void addEstate(EstateEvent estateEvent) {
        this.estates.add(estateEvent);
    }

}

