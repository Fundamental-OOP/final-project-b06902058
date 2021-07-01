package utils;

import java.util.Random;

public class RandomUtil {
    private static Random rand = new Random();

    public static int getRandomInt(int bound){
        return rand.nextInt(bound);
    }
}
