package alex.common;

import java.util.ArrayList;
import java.util.Random;

public class AlexChoose {
    public static String getFromList(Random random, ArrayList<String> affirmations) {
        return affirmations.get(random.nextInt(affirmations.size()));
    }
}
