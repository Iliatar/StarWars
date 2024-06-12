package Iliatar.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipFactory {
    record ShipBlueprint(int rank, int endurance, double mass, double maneuverability, double spaceSpeed, double armor, List<WeaponModule> weapons, int ammoStorageLimit) {}

    private static Map<String, ShipBlueprint> blueprintLibrary;

    static {
        blueprintLibrary = new HashMap<>();
        List<WeaponModule> weapons = new ArrayList<>();

        weapons.add(WeaponModuleFactory.getWeapon("Light cannon"));
        weapons.add(WeaponModuleFactory.getWeapon("Light cannon"));
        ShipBlueprint scoutBlueprint = new ShipBlueprint(1, 15, 3, 8, 5, 1, weapons, 30);
        blueprintLibrary.put("Scout", scoutBlueprint);

        weapons = new ArrayList<>();
        weapons.add(WeaponModuleFactory.getWeapon("Heavy Cannon"));
        ShipBlueprint harpoonBlueprint = new ShipBlueprint(1, 22, 4, 6, 4, 2, weapons, 64);
        blueprintLibrary.put("Harpoon", harpoonBlueprint);

        weapons = new ArrayList<>();
        weapons.add(WeaponModuleFactory.getWeapon("Light cannon"));
        weapons.add(WeaponModuleFactory.getWeapon("Light cannon"));
        weapons.add(WeaponModuleFactory.getWeapon("Cannon"));
        ShipBlueprint fighterBlueprint = new ShipBlueprint(1, 22, 4, 6, 4, 2, weapons, 55);
        blueprintLibrary.put("Fighter", fighterBlueprint);

        weapons = new ArrayList<>();
        weapons.add(WeaponModuleFactory.getWeapon("Light Turret"));
        weapons.add(WeaponModuleFactory.getWeapon("Minigun"));
        weapons.add(WeaponModuleFactory.getWeapon("Heavy Cannon"));
        weapons.add(WeaponModuleFactory.getWeapon("Heavy Turret"));
        ShipBlueprint destroyerBlueprint = new ShipBlueprint(2, 60, 14, 3, 4, 5, weapons, 150);
        blueprintLibrary.put("Destroyer", destroyerBlueprint);

        weapons = new ArrayList<>();
        weapons.add(WeaponModuleFactory.getWeapon("Light Turret"));
        weapons.add(WeaponModuleFactory.getWeapon("Light Turret"));
        weapons.add(WeaponModuleFactory.getWeapon("Rotary Cannon"));
        weapons.add(WeaponModuleFactory.getWeapon("Rotary Cannon"));
        weapons.add(WeaponModuleFactory.getWeapon("Heavy Turret"));
        weapons.add(WeaponModuleFactory.getWeapon("Main Caliber"));
        weapons.add(WeaponModuleFactory.getWeapon("Main Caliber"));
        ShipBlueprint cruiserBlueprint = new ShipBlueprint(3, 150, 40, 1, 3, 6, weapons, 500);
        blueprintLibrary.put("Cruiser", cruiserBlueprint);
    }

    public static Ship getShip(String shipTypeName) {
        if (!blueprintLibrary.containsKey(shipTypeName)) {
            throw new IllegalArgumentException("ShipFactory library does not contain module with name " + shipTypeName);
        }
        ShipBlueprint blueprint = blueprintLibrary.get(shipTypeName);
        String shipName = ShipNameGenerator.getShipName();
        return new Ship(shipTypeName, blueprint.rank, shipName, blueprint.endurance, blueprint.mass, blueprint.maneuverability, blueprint.spaceSpeed, blueprint.armor,
                List.copyOf(blueprint.weapons), blueprint.ammoStorageLimit());
    }

    public static List<Ship> getShips(String shipTypeName, int count) {
        List<Ship> ships = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            ships.add(getShip(shipTypeName));
        }

        return ships;
    }
}
