package Iliatar.ship.modules.fabrics;

import Iliatar.ship.modules.Barrell;

import java.util.HashMap;
import java.util.Map;

public class BarrellFactory {
    public enum BarrellType {Mini, Light, Medium, Large, Powerful}
    private record BarrellBlueprint(int endurance, int size, int mass, double armor, int caliber, int armorPenetration) {}
    private static Map<BarrellType, BarrellBlueprint> blueprintLibrary;

    static {
        blueprintLibrary = new HashMap<>();
        blueprintLibrary.put(BarrellType.Mini, new BarrellBlueprint(3, 2, 1, 0, 1, 0));
        blueprintLibrary.put(BarrellType.Light, new BarrellBlueprint(5, 3, 2, 0, 3, 0));
        blueprintLibrary.put(BarrellType.Medium, new BarrellBlueprint(10, 5, 4, 0, 5, 1));
        blueprintLibrary.put(BarrellType.Large, new BarrellBlueprint(20, 12, 10, 0, 8, 3));
        blueprintLibrary.put(BarrellType.Powerful, new BarrellBlueprint(30, 18, 20, 0, 12, 5));
    }

    public static Barrell getBarrell(BarrellType barrellType) {
        if (!blueprintLibrary.containsKey(barrellType)) {
            throw new RuntimeException("BarrellFactory library does not contain module with name " + barrellType);
        }
        BarrellBlueprint bp = blueprintLibrary.get(barrellType);
        return new Barrell(barrellType.toString() + " barrell", bp.endurance(), bp.size(), bp.mass(), bp.armor(), bp.caliber(), bp.armorPenetration());
    }
}
