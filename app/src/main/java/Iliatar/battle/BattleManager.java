package Iliatar.battle;

import Iliatar.ship.Ship;
import Iliatar.utils.randomSelector.Priority;
import Iliatar.utils.randomSelector.PriorityItem;
import Iliatar.utils.randomSelector.RandomSelector;

import java.util.ArrayList;
import java.util.List;

public class BattleManager {
    private static final double TURN_DELTA_TIME = 1d;
    Fleet fleet1;
    Fleet fleet2;
    private BattleOverlap battleOverlap;

    public void initiateBattle(Fleet fleet1, Fleet fleet2) {
        this.fleet1 = fleet1;
        this.fleet2 = fleet2;
        fleet1.initiateBattle();
        fleet2.initiateBattle();
        battleOverlap = new BattleOverlap(fleet1, fleet2);
        processBattle();
    }

    public void processBattle() {
        while (!fleet1.getActiveShips().isEmpty() && !fleet2.getActiveShips().isEmpty()) {
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

        List<Priority<Ship>> ppriorityTargets = targetFleet.getActiveShips().stream().map(ship -> (Priority<Ship>)new PriorityItem<Ship>(ship, 1)).toList();

        while (result.size() < targetsCount) {
            Ship selectedTarget = RandomSelector.selectRandomByPriority(ppriorityTargets);
            result.add(selectedTarget);
            ppriorityTargets = ppriorityTargets.stream().filter(item -> item.getObject() != selectedTarget).toList();
        }

        return result;
    }

    private void finalizeBattle() {
        fleet1.finalizeBattle();
        fleet2.finalizeBattle();
    }

    public BattleOverlap getBattleOverlap() { return battleOverlap; }
}
