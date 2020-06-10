package piociek.supply.app.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ItemHelper {

    public String getCurrentTimeStamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    public String getRandomSeed() {
        final int max = 9000;
        final int min = 1000;
        return String.valueOf(new Random().nextInt(max) + min);
    }
}
