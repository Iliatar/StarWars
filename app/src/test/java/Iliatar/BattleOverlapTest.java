package Iliatar;

import Iliatar.battle.BattleOverlap;
import Iliatar.battle.Fleet;
import Iliatar.ship.Ship;
import Iliatar.ship.ShipFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class BattleOverlapTest {
    @Test
    public void testOverlapLogic() {
        Fleet fleet1 = new Fleet(new ArrayList<Ship>());
        fleet1.addShips(ShipFactory.getShips("Scout", 4));
        fleet1.addShips(ShipFactory.getShips("Destroyer", 2));
        fleet1.addShips(ShipFactory.getShips("Cruiser", 3));

        Fleet fleet2 = new Fleet(new ArrayList<Ship>());
        fleet2.addShips(ShipFactory.getShips("Scout", 6));
        fleet2.addShips(ShipFactory.getShips("Destroyer", 4));
        fleet2.addShips(ShipFactory.getShips("Cruiser", 5));

        BattleOverlap overlap = new BattleOverlap(fleet1, fleet2);
        var actual = overlap.getOverlap(fleet1.getShips().get(0), fleet2.getShips().get(0));
        var expected = Math.sqrt(5) / 100;
        assertEquals(expected, actual);

        actual = overlap.getOverlap(fleet1.getShips().get(5), fleet2.getShips().get(5));
        expected = Math.sqrt(9) / 100;
        assertEquals(expected, actual);

        actual = overlap.getOverlap(fleet2.getShips().get(5), fleet1.getShips().get(5));
        expected = Math.sqrt(9) / 100;
        assertEquals(expected, actual);

        actual = overlap.getOverlap(fleet1.getShips().get(fleet1.getShips().size() - 1), fleet2.getShips().get(fleet2.getShips().size() - 1));
        expected = Math.sqrt(34) / 100;
        assertEquals(expected, actual);
    }

    @Test
    public void testOverlapThrowns() {
        Fleet fleet1 = new Fleet(new ArrayList<>());
        fleet1.addShips(ShipFactory.getShips("Scout", 2));

        Fleet fleet2 = new Fleet(new ArrayList<>());
        fleet2.addShips(ShipFactory.getShips("Scout", 4));

        BattleOverlap overlap = new BattleOverlap(fleet1, fleet2);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> overlap.getOverlap(fleet1.getShips().get(0), fleet1.getShips().get(1)));
        assertEquals("sourceShip and targetShip are from one fleet or unknown", exception.getMessage());
    }
}

