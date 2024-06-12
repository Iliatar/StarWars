package Iliatar.battle;

import Iliatar.ship.Ship;
import Iliatar.utils.randomSelector.Priority;
import Iliatar.utils.randomSelector.PriorityItem;
import Iliatar.utils.randomSelector.RandomSelector;

import java.util.ArrayList;
import java.util.List;

public class BattleManager {
    private static final double TURN_DELTA_TIME = 1d;
    private static final int TURN_NUMBER_LIMIT = 2500;
    Fleet fleet1;
    Fleet fleet2;
    int turnNumber;
    private BattleOverlap battleOverlap;

    public int getTurnNumber() {
        return turnNumber;
    }

    public void initiateBattle(Fleet fleet1, Fleet fleet2) {
        this.fleet1 = fleet1;
        this.fleet2 = fleet2;
        BattleLogger.initiate(this);
        battleOverlap = new BattleOverlap(fleet1, fleet2);
        turnNumber = 0;
        BattleLogger.logMessage("start battle");
        BattleLogger.logMessage("fleet 1 with " + fleet1.getShips().size() + " ships");
        BattleLogger.logMessage("fleet 2 with " + fleet2.getShips().size() + " ships");
        fleet1.initiateBattle(this);
        fleet2.initiateBattle(this);
        processBattle();
    }

    public void processBattle() {
        while (!fleet1.getActiveShips().isEmpty() && !fleet2.getActiveShips().isEmpty() && turnNumber < TURN_NUMBER_LIMIT) {
            turnNumber++;
            battleOverlap.refreshOverlap();
            fleet1.getActiveShips().forEach(ship -> ship.processBattleTurn(TURN_DELTA_TIME));
            fleet2.getActiveShips().forEach(ship -> ship.processBattleTurn(TURN_DELTA_TIME));
        }

        finalizeBattle();
    }
    public List<Ship> getPotentialTargets(Ship sourceShip, int targetsCount) {
        Fleet targetFleet;
        if (fleet1.getShips().contains(sourceShip)) {
            targetFleet = fleet2;
        } else if (fleet2.getShips().contains(sourceShip)) {
            targetFleet = fleet1;
        } else {
            throw new IllegalArgumentException("sourceShip is from unknown fleet");
        }

        if (targetsCount > targetFleet.getActiveShips().size()) {
            return List.copyOf(targetFleet.getActiveShips());
        }

        List<Ship> result = new ArrayList<>(targetsCount);

        List<Priority<Ship>> priorityTargets = targetFleet.getActiveShips().stream().map(ship -> (Priority<Ship>)new PriorityItem<Ship>(ship, 1)).toList();

        while (result.size() < targetsCount) {
            Ship selectedTarget = RandomSelector.selectRandomByPriority(priorityTargets);
            result.add(selectedTarget);
            priorityTargets = priorityTargets.stream().filter(item -> item.getObject() != selectedTarget).toList();
        }

        return result;
    }

    private void finalizeBattle() {
        fleet1.finalizeBattle();
        fleet2.finalizeBattle();
        BattleLogger.logMessage("finish battle");
        BattleLogger.logMessage("fleet "+ fleet1.getName() +" with " + fleet1.getShips().size() + " ships (" + fleet1.getActiveShips().size() + " active)");
        BattleLogger.logMessage("fleet "+ fleet2.getName() +" with " + fleet2.getShips().size() + " ships (" + fleet2.getActiveShips().size() + " active)");
    }

    public BattleOverlap getBattleOverlap() { return battleOverlap; }
}
