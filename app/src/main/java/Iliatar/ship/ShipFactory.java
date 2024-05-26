package Iliatar.ship;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipFactory {
    record ShipBlueprint(int rank, int endurance, double mass, double maneuverability, double spaceSpeed, double armor, List<WeaponModule> weapons, int ammoStorageLimit) {}

    private static Map<String, ShipBlueprint> blueprintLibrary;

    static {
        blueprintLibrary = new HashMap<>();
    }

    public static Ship getWeapon(String shipTypeName) {
        if (!blueprintLibrary.containsKey(shipTypeName)) {
            throw new IllegalArgumentException("ShipFactory library does not contain module with name " + shipTypeName);
        }
        ShipBlueprint blueprint = blueprintLibrary.get(shipTypeName);
        return new Ship(shipTypeName, blueprint.rank, blueprint.endurance, blueprint.mass, blueprint.maneuverability, blueprint.spaceSpeed, blueprint.armor,
                blueprint.weapons, blueprint.ammoStorageLimit());
    }
}
