package Iliatar.ship.modules.fabrics;

import Iliatar.ship.modules.ShipHull;

import java.util.HashMap;
import java.util.Map;

public class ShipHullFactory {
    public enum ShipHullType {Tiny, Small, Medium, Large, Huge}
    private record ShipHullBlueprint(int endurance, int size, int mass, double armor) {}
    private static Map<ShipHullType, ShipHullBlueprint> blueprintLibrary;

    static {
        blueprintLibrary = new HashMap<>();
        blueprintLibrary.put(ShipHullType.Tiny, new ShipHullBlueprint(30, 20, 20, 0));
        blueprintLibrary.put(ShipHullType.Small, new ShipHullBlueprint(40, 35, 32, 1));
        blueprintLibrary.put(ShipHullType.Medium, new ShipHullBlueprint(60, 48, 48, 3));
        blueprintLibrary.put(ShipHullType.Large, new ShipHullBlueprint(90, 70, 75, 5));
        blueprintLibrary.put(ShipHullType.Huge, new ShipHullBlueprint(150, 120, 140, 5));
    }

    public static ShipHull getShipHull(ShipHullType shipHullType) {
        if (!blueprintLibrary.containsKey(shipHullType)) {
            throw new RuntimeException("ShipHullFactory library does not contain module with name " + shipHullType);
        }
        ShipHullBlueprint bp = blueprintLibrary.get(shipHullType);
        return new ShipHull(shipHullType.toString() + " hull", bp.endurance(), bp.size(), bp.mass(), bp.armor());
    }
}
