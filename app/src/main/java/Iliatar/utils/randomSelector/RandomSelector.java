package Iliatar.utils.randomSelector;

import java.util.List;

public class RandomSelector {
    public static <T>  T selectRandomByPriority (List<Priority<T>> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("input list is null or is empty");
        }

        double totalPriority = 0;
        for (Priority<T> priorityItem : list) {
            totalPriority += priorityItem.getPriority();
        }

        double diceRoll = Math.random() * totalPriority;

        double accumulatedPriority = 0;

        for (Priority<T> priorityItem : list) {
            accumulatedPriority += priorityItem.getPriority();
            if (accumulatedPriority >= diceRoll) {
                return priorityItem.getObject();
            }
        }

        return list.get(0).getObject();
    }
}
