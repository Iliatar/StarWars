package Iliatar;

import Iliatar.battle.BattleManager;
import Iliatar.battle.Fleet;
import Iliatar.ship.Ship;
import Iliatar.ship.ShipFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class BattleManagerTest {
    @Test
    public void testBattleManager() {
        Fleet fleet1 = new Fleet("Tarmidons", new ArrayList<Ship>());
        fleet1.addShips(ShipFactory.getShips(ShipFactory.ShipType.Scout, 1));
        fleet1.addShips(ShipFactory.getShips(ShipFactory.ShipType.Harpoon, 4));
        fleet1.addShips(ShipFactory.getShips(ShipFactory.ShipType.Destroyer, 1));
        fleet1.addShips(ShipFactory.getShips(ShipFactory.ShipType.Cruiser, 0));

        Fleet fleet2 = new Fleet("Reloids", new ArrayList<Ship>());
        fleet2.addShips(ShipFactory.getShips(ShipFactory.ShipType.Scout, 0));
        fleet2.addShips(ShipFactory.getShips(ShipFactory.ShipType.Harpoon, 1));
        fleet2.addShips(ShipFactory.getShips(ShipFactory.ShipType.Destroyer, 0));
        fleet2.addShips(ShipFactory.getShips(ShipFactory.ShipType.Cruiser, 1));

        BattleManager battleManager = new BattleManager();
        battleManager.initiateBattle(fleet1, fleet2);
    }
}
