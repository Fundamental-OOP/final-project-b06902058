package model.card;

import utils.RandomUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class CardGenerator {
    List<Card> allCard = new ArrayList<>();
    HashMap<String, Card> nameToCardMapper = new HashMap<>();
    public CardGenerator(HashMap<String, Card> nameToCardMapper){
        Scanner cardReader;
        File cardConfigFile = new File("assets/card/config.txt");
        try {
            cardReader = new Scanner(cardConfigFile);
            while (cardReader.hasNextLine()) {
                String line = cardReader.nextLine().strip();
                if (nameToCardMapper.containsKey(line)){
                    Card card = nameToCardMapper.get(line);
                    allCard.add(card);
                    this.nameToCardMapper.put(line, card);
                }else {
                    System.err.printf("[CardGenerator] Unknown line for card config file %s : %s\n", cardConfigFile.getName(), line);
                }
            }
            cardReader.close();
        }catch (FileNotFoundException e) {
            System.err.println("[CardGenerator] card config file not found");
            e.printStackTrace();
        }
    }

    public Card getSpecificCard(String name) throws CloneNotSupportedException {
        return nameToCardMapper.get(name).clone();
    }

    public Card getRandomCard() throws CloneNotSupportedException{
        return allCard.get(RandomUtil.getRandomInt(allCard.size())).clone();
    }

    public List<Card> getRandomCards(int num) throws CloneNotSupportedException{
        List<Card> cards = new ArrayList<>();
        for(int i = 0; i < num; i++){
            cards.add(allCard.get(RandomUtil.getRandomInt(allCard.size())).clone());
        }
        return cards;
    }
}
