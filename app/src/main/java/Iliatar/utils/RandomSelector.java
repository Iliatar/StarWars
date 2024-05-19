package Iliatar.utils;

import java.util.List;
import java.util.Map;

public class RandomSelector {
    public static <T extends Priority>  T selectRandomByPriority (List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("input list is null or is empty");
        }

        double totalPriority = 0;
        for (T item : list) {
            totalPriority += item.getPriority();
        }

        double diceRoll = Math.random() * totalPriority;

        double accumulatedPriority = 0;

        for (T item : list) {
            accumulatedPriority += item.getPriority();
            if (accumulatedPriority >= diceRoll) {
                return item;
            }
        }

        return list.get(0);
    }
}
