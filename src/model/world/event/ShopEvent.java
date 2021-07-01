package model.world.event;

import middleware.AsyncMessageHandler;
import model.card.Card;
import model.card.CardGenerator;
import model.role.Role;
import utils.*;
import view.GUI;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ShopEvent extends RichmanEventAbstract{
    private List<Image> buying;
    private int chooseIndex = 0;
    private List<Image> imList;
    private List<Card> cardList;
    private List<Integer> cardPrice = new ArrayList<>();
    private ShopState state = ShopState.BUYING.NOTHING;
    private CardGenerator cardGenerator;
    enum ShopState {
        NOTHING,
        BUYING,
        AFTER_BUYING,
        TOO_MANY_CARD,
    }

    public ShopEvent(CardGenerator cardGenerator) {
        this.cardGenerator = cardGenerator;
        this.imList = new ArrayList<>();
        this.buying = new ArrayList<>();

        imList.add(ImageStateUtils.readImageFromString("assets/event/shop/shopEvent.png"));
        imList.add(ImageStateUtils.readImageFromString("assets/shop/shop.png"));
        imList.add(ImageStateUtils.readImageFromString("assets/shop/buySuccess.png"));
        imList.add(ImageStateUtils.readImageFromString("assets/shop/tooMuchCard.png"));

    }

    @Override
    public void render(Point position, Graphics g) {
        Render3DUtils.drawRole(imList.get(0), g, (int)position.getX(), (int)position.getY(), 70, 70);
    }

    public void stateRender(Graphics g) {
        switch (this.state) {
            case NOTHING:
                break;
            case BUYING:
                // g.drawImage(imList.get(1), 0, 0, GUI.WIDTH, GUI.HEIGHT * 2 / 3, null);
                g.drawImage(imList.get(1), GUI.WIDTH / 6, 0, GUI.WIDTH / 3 * 2, GUI.HEIGHT * 2 / 3, null);
                for (int i = 0; i < cardList.size(); i++) {
                    cardList.get(i).renderAtShop(g, i, (i == chooseIndex), cardPrice.get(i));
                }
                break;
            case AFTER_BUYING:
                g.drawImage(imList.get(2), GUI.WIDTH * 5 / 16, GUI.HEIGHT * 3 / 9, GUI.WIDTH * 6 / 16, GUI.HEIGHT * 3 / 9, null);
                break;
            case TOO_MANY_CARD:
                g.drawImage(imList.get(3), GUI.WIDTH * 5 / 16, GUI.HEIGHT * 3 / 9, GUI.WIDTH * 6 / 16, GUI.HEIGHT * 3 / 9, null);
                break;
        }
    }

    @Override
    public void trigger(Role caster, AsyncMessageHandler messageHandler, List<Role> castee) throws InterruptedException {
        try{
            this.cardList = cardGenerator.getRandomCards(4);
            for (int i = 0; i < 4; i++) {
                cardPrice.add((RandomUtil.getRandomInt(20) + 10) * 100);
            }
        }catch(CloneNotSupportedException e){
            System.out.println("Get Random Cards Crash");
        }

        this.state = ShopState.BUYING;
        messageHandler.clear();
        int buyCount = 0;
        while(true) {
            if (caster.getCardNum() >= 8) {
                this.state = ShopState.TOO_MANY_CARD;
                Delay.delay(1000);
                this.state = ShopState.NOTHING;
                return;
            }
            if (buyCount >= 3) {
                this.state = ShopState.AFTER_BUYING;
                Delay.delay(1000);
                this.state = ShopState.NOTHING;
                return;
            }
            KeyEvent keyEvent = messageHandler.consume();
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    this.chooseIndex += 1;
                    this.chooseIndex %= cardList.size();
                    break;

                case KeyEvent.VK_LEFT:
                    this.chooseIndex += cardList.size()-1;
                    this.chooseIndex %= cardList.size();
                    break;

                case KeyEvent.VK_ENTER:
                    if (caster.getMoney() < cardPrice.get(chooseIndex)) {
                        break;
                    }
                    buyCount++;
                    caster.addCard(cardList.get(chooseIndex));
                    caster.subMoney(cardPrice.get(chooseIndex));
                    cardList.remove(chooseIndex);
                    cardPrice.remove(chooseIndex);
                    if (cardList.size() == 0) {
                        this.state = ShopState.NOTHING;
                        return;
                    }
                    this.state = ShopState.BUYING;
                    chooseIndex = 0;
                    break;
                    
                case KeyEvent.VK_ESCAPE:
                    this.state = ShopState.NOTHING;
                    return;
            }
        }
    }

    @Override
    public void setArgs(List<Integer> arguments) {
    }

}
