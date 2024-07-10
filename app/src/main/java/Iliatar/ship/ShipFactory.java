package Iliatar.ship;

import Iliatar.ship.Ship;
import Iliatar.ship.ShipNameGenerator;
import Iliatar.ship.modules.ShipHull;
import Iliatar.ship.modules.ShipModule;
import Iliatar.ship.modules.fabrics.ShipModuleUniversalFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipFactory {
    public enum ShipType {Scout, Harpoon, Fighter, Destroyer, Cruiser}
    private record ShipBlueprint(int rank, String hullType, List<String> weaponNameList, int ammoStorageLimit) {}
    private static Map<ShipType, ShipBlueprint> blueprintLibrary;

    static {
        blueprintLibrary = new HashMap<>();
        blueprintLibrary.put(ShipType.Scout, new ShipBlueprint(1, "Tiny",
                List.of("Stinger"), 30));
        blueprintLibrary.put(ShipType.Harpoon, new ShipBlueprint(1, "Small",
                List.of("MediumCannon"), 80));
        blueprintLibrary.put(ShipType.Fighter, new ShipBlueprint(1, "Small",
                List.of("Stinger", "Turret"), 200));
        blueprintLibrary.put(ShipType.Destroyer, new ShipBlueprint(2, "Medium",
                List.of("Turret", "Turret", "MediumBattery", "HeavyCannon"), 500));
        blueprintLibrary.put(ShipType.Cruiser, new ShipBlueprint(3, "Large",
                List.of("Turret", "Turret", "Turret", "Turret",
                        "MediumBattery", "MediumBattery", "HeavyCannon", "PowerfulCannon"), 750));

    }

    public static Ship getShip(ShipType shipType) {
        if (!blueprintLibrary.containsKey(shipType)) {
            throw new RuntimeException("ShipFactory library does not contain module with name " + shipType);
        }
        ShipBlueprint bp = blueprintLibrary.get(shipType);

        ShipHull hull = ShipModuleUniversalFactory.getModule(ShipModule.ShipModuleType.ShipHull, bp.hullType());

        List<ShipModule> weaponList = new ArrayList<>();
        for (String weaponName : bp.weaponNameList()) {
            weaponList.add(ShipModuleUniversalFactory.getModule(ShipModule.ShipModuleType.Weapon, weaponName));
        }
        hull.addChild(weaponList);

        String shipName = ShipNameGenerator.getShipName();
        return new Ship(shipType.toString(), bp.rank(), shipName, hull, bp.ammoStorageLimit());
    }

    public static List<Ship> getShips(ShipType shipType, int count) {
        List<Ship> ships = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            ships.add(getShip(shipType));
        }
        return ships;
    }
}
