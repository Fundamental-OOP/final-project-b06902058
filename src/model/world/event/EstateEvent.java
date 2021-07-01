package model.world.event;

import view.GUI;
import model.role.Role;
import utils.*;
import middleware.AsyncMessageHandler;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class EstateEvent extends RichmanEventAbstract{
    private Role owner;
    private List<Image> buying;
    private List<Image> upgrading;
    private List<Image> imList;
    private List<Image> moneyFlying;
    private Image noEnoughMoney;
    private int fly_state = 0;
    private int fly_loc = 0;
    private Image noOwner;
    private int YNstate = 1;
    private int level = 0;
    private int price;
    private EstateState state = EstateState.NOTHING;

    private Role caster;
    enum EstateState {
        NOTHING,
        BUYING,
        AFTER_BUYING,
        UPGRADING,
        AFTER_UPGRADING,
        CANNOT_UPGRADING,
        PAY_OTHER, 
        NO_ENOUGH_MONEY,
    }

    public EstateEvent() {
        this.imList = new ArrayList<>();
        this.buying = new ArrayList<>();
        this.upgrading = new ArrayList<>();
        this.moneyFlying = new ArrayList<>();


        for(int i = 0; i < 9; i++){
            this.moneyFlying.add(ImageStateUtils.readImageFromString("assets/event/estate/information/moneyFly" + Integer.toString(i) + ".png"));
        }
        buying.add(ImageStateUtils.readImageFromString("assets/event/estate/information/buy_no.png"));
        buying.add(ImageStateUtils.readImageFromString("assets/event/estate/information/buy_yes.png"));
        buying.add(ImageStateUtils.readImageFromString("assets/event/estate/information/buy_success.png"));
        
        upgrading.add(ImageStateUtils.readImageFromString("assets/event/estate/information/upgrade_no.png"));
        upgrading.add(ImageStateUtils.readImageFromString("assets/event/estate/information/upgrade_yes.png"));
        upgrading.add(ImageStateUtils.readImageFromString("assets/event/estate/information/buy_success.png"));
        
        imList.add(ImageStateUtils.readImageFromString("assets/event/estate/estate/estate_level0.png"));
        imList.add(ImageStateUtils.readImageFromString("assets/event/estate/estate/estate_level1.png"));
        imList.add(ImageStateUtils.readImageFromString("assets/event/estate/estate/estate_level2.png"));
        imList.add(ImageStateUtils.readImageFromString("assets/event/estate/estate/estate_level3.png"));
        
        noOwner = ImageStateUtils.readImageFromString("assets/event/estate/label/no_buy.png");
    
        this.noEnoughMoney = ImageStateUtils.readImageFromString("assets/event/estate/information/noEnoughMoney.png");
    }

    @Override
    public void render(Point position, Graphics g) {
        //test
        int newX = 600 + (int) (0.92 * (position.getX() - position.getY()));
        int newY = 200 + (int) (0.32 * (position.getX() + position.getY()));

        newX = (int) ((double) newX * GUI.HEIGHT / 810);
        newY = (int) ((double) newY * GUI.WIDTH / 1440);
        int delta = (int) (13.5 * ((double)GUI.HEIGHT / 810 + (double)GUI.WIDTH / 1440 ));
        if(owner != null){
            switch(this.level){
                case 0:
                    g.drawImage(imList.get(0), newX + 10 *GUI.WIDTH/1440, newY - 50 * GUI.HEIGHT / 810, imList.get(0).getWidth(null) * 60 * GUI.WIDTH / (1440*imList.get(0).getHeight(null)), 60 * GUI.HEIGHT / 810, null);
                    break;
                case 1:
                    g.drawImage(imList.get(1), newX + 10 *GUI.WIDTH/1440, newY - 40 * GUI.HEIGHT / 810, imList.get(0).getWidth(null) * 60 * GUI.WIDTH / (1440*imList.get(0).getHeight(null)), 60 * GUI.HEIGHT / 810, null);
                    break;  
                case 2:
                    g.drawImage(imList.get(2), newX + 10 *GUI.WIDTH/1440, newY - 50 * GUI.HEIGHT / 810, imList.get(0).getWidth(null) * 60 * GUI.WIDTH / (1440*imList.get(0).getHeight(null)), 60 * GUI.HEIGHT / 810, null);
                    break;
                case 3:
                    g.drawImage(imList.get(3), newX + 10 *GUI.WIDTH/1440, newY - 48 * GUI.HEIGHT / 810, imList.get(0).getWidth(null) * 60 * GUI.WIDTH / (1440*imList.get(0).getHeight(null)), 60 * GUI.HEIGHT / 810, null);
                    break;
            }
            g.drawImage(this.owner.getBoard(), newX + 5*delta - 10 * GUI.WIDTH / 1440 , newY - 45 * GUI.HEIGHT / 810, 50 * GUI.WIDTH / 1440, 50 * GUI.HEIGHT / 810, null);
        }
        else{
            g.drawImage(noOwner, newX + 5*delta - 10 * GUI.WIDTH / 1440 , newY - 45 * GUI.HEIGHT / 810, 50 * GUI.WIDTH / 1440, 50 * GUI.HEIGHT / 810, null);

        }
    }

    public Role getOwner() {
        return owner;
    }

    public void stateRender(Graphics g) {
        int imageSize = Math.min(GUI.WIDTH * 2/16, GUI.HEIGHT * 2/9);
        switch (this.state) {
            case NOTHING:
                break;
            case BUYING:
                // TODO : 需要同時輸出要多少錢
                // int imageWidth = buying.get(YNstate).getWidth() * 80 * GUI.WIDTH / (1440*buying.get(YNstate).getHeight());
                // int imageHeight = 80 * GUI.HEIGHT / 810;
                g.drawImage(buying.get(YNstate),GUI.WIDTH * 14/16, 
                                                GUI.HEIGHT* 7/9, 
                                                imageSize, 
                                                imageSize, null);
                g.setColor(Color.BLACK);
                g.setFont(new Font("TimesRoman", Font.PLAIN, imageSize / 10));
                g.drawString(Integer.toString(this.price), (int)(GUI.WIDTH * 14.92/16), (int)(GUI.HEIGHT * 7.86 / 9));
                break;
            case AFTER_BUYING:
                g.drawImage(buying.get(2), GUI.WIDTH * 5 / 16, GUI.HEIGHT * 3 / 9, GUI.WIDTH * 6 / 16, GUI.HEIGHT * 3 / 9, null);
                break;
            case UPGRADING:
                g.drawImage(upgrading.get(YNstate), GUI.WIDTH * 14/16, GUI.HEIGHT* 7/9, imageSize, imageSize, null);
                g.setColor(Color.BLACK);
                g.setFont(new Font("TimesRoman", Font.PLAIN, imageSize / 10));
                g.drawString(Integer.toString(this.price * (this.level+2)),  (int)(GUI.WIDTH * 14.92/16), (int)(GUI.HEIGHT * 7.86 / 9));
                break;
            case AFTER_UPGRADING:
                g.drawImage(upgrading.get(2), GUI.WIDTH * 5 / 16, GUI.HEIGHT * 3 / 9, GUI.WIDTH * 6 / 16, GUI.HEIGHT * 3 / 9, null);
                break;
            case PAY_OTHER:

                g.drawImage(caster.getAvatar(), 3*GUI.WIDTH / 8 , GUI.HEIGHT / 2 - caster.getAvatar().getHeight(null) / 2, moneyFlying.get(fly_state).getWidth(null) * 100 * GUI.WIDTH / (1440*moneyFlying.get(fly_state).getHeight(null)), 100 * GUI.HEIGHT / 810, null);
                g.drawImage(owner.getAvatar(), 5*GUI.WIDTH / 8, GUI.HEIGHT / 2 - owner.getAvatar().getHeight(null) / 2, moneyFlying.get(fly_state).getWidth(null) * 100 * GUI.WIDTH / (1440*moneyFlying.get(fly_state).getHeight(null)), 100 * GUI.HEIGHT / 810, null);
                g.drawImage(moneyFlying.get(fly_state), (600 + fly_loc) * GUI.WIDTH / 1440, GUI.HEIGHT/2 - 50 * GUI.HEIGHT / 810, moneyFlying.get(fly_state).getWidth(null) * 100 * GUI.WIDTH / (1440*moneyFlying.get(fly_state).getHeight(null)), 100 * GUI.HEIGHT / 810, null);

                if(fly_loc %5 == 0)
                    fly_state = (fly_state + 1) % 9;
                fly_loc += 2;
                break;

            case NO_ENOUGH_MONEY:
                g.drawImage(noEnoughMoney, GUI.WIDTH * 5 / 16, GUI.HEIGHT * 3 / 9, GUI.WIDTH * 6 / 16, GUI.HEIGHT * 3 / 9, null);
                break;
            default:
                break;
        }
        
    }

    @Override
    public void trigger(Role caster, AsyncMessageHandler messageHandler, List<Role> castee) throws InterruptedException {
        this.caster = caster;
        if (this.owner == null) {
            this.state = EstateState.BUYING;
            messageHandler.clear();
            while(true){
                KeyEvent keyEvent = messageHandler.consume();
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_LEFT:
                        this.YNstate += 1;
                        this.YNstate %= 2;
                        break;
                    case KeyEvent.VK_ENTER:
                        if (this.YNstate == 1) {
                            if(caster.getMoney() >= this.price){
                                caster.subMoney(this.price);
                                this.owner = caster;
                                caster.addEstate(this);
                                this.state = EstateState.AFTER_BUYING;
                            }
                            else{
                                this.state = EstateState.NO_ENOUGH_MONEY;
                            }
                            Delay.delay(1000);
                        }
                        this.state = EstateState.NOTHING;
                        this.YNstate = 1;
                        return;
                }
            }
        } else if (this.owner.equals(caster)){
            if(this.level == 3)
                return;
            this.state = EstateState.UPGRADING;
            messageHandler.clear();
            while(true){
                KeyEvent keyEvent = messageHandler.consume();
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_LEFT:
                        this.YNstate += 1;
                        this.YNstate %= 2;
                        break;
                    case KeyEvent.VK_ENTER:
                        if (this.YNstate == 1) {
                            //TODO: check if money enough
                            if(caster.getMoney() < this.price*(this.level+2)){
                                this.state = EstateState.NO_ENOUGH_MONEY;
                            }
                            else{
                                this.level += 1;
                                caster.subMoney(this.price * (this.level+1));
                                this.state = EstateState.AFTER_UPGRADING;
                            }
                            
                            Delay.delay(1000);
                        }
                        
                        this.state = EstateState.NOTHING;
                        this.YNstate = 1;
                        return;
                }
            }
            
        } else {
            //punish this guy
            if(caster.getMoney() >= this.price * (this.level+1) * 2){
                caster.subMoney(this.price * (this.level+1) * 2);
                owner.addMoney(this.price * (this.level+1) * 2);
            }
            else{
                owner.addMoney(caster.getMoney());
                caster.subMoney(this.price * (this.level+1) * 2);
            }
            this.state = EstateState.PAY_OTHER;
            Delay.delay(1900);
            fly_loc = 0;
            this.state = EstateState.NOTHING;
        }
    }

    @Override
    public void setArgs(List<Integer> arguments) {
        this.price = arguments.get(0);
    }

    public void init() {
        this.level = 0;
        this.owner = null;
    }
}
