package com.debbugeando_ideas.best_travel.util;

import java.time.LocalDateTime;
import java.util.Random;

public class BestTravelUtil {
    private static final Random random = new Random();

    public static LocalDateTime getRandomSoon(){
        var randomHours = random.nextInt(5 - 2)+2;
        var now = LocalDateTime.now();
        return now.plusHours(randomHours);
    }

    public static LocalDateTime getRandomLatter(){
        var randomeHours = random.nextInt(24 - 12 ) + 12;
        var now = LocalDateTime.now();
        return now.plusHours(randomeHours);
    }
}
