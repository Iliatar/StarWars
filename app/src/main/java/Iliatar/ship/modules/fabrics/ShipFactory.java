package Iliatar.ship.modules.fabrics;

import Iliatar.ship.Ship;
import Iliatar.ship.ShipNameGenerator;
import Iliatar.ship.modules.ShipHull;
import Iliatar.ship.modules.ShipModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipFactory {
    public enum ShipType {Scout, Harpoon, Fighter, Destroyer, Cruiser}
    private record ShipBlueprint(int rank, ShipHullFactory.ShipHullType hullType, List<WeaponFactory.WeaponType> weaponTypeList, int ammoStorageLimit) {}
    private static Map<ShipType, ShipBlueprint> blueprintLibrary;

    static {
        blueprintLibrary = new HashMap<>();
        blueprintLibrary.put(ShipType.Scout, new ShipBlueprint(1, ShipHullFactory.ShipHullType.Tiny,
                List.of(WeaponFactory.WeaponType.Stinger), 30));
        blueprintLibrary.put(ShipType.Harpoon, new ShipBlueprint(1, ShipHullFactory.ShipHullType.Small,
                List.of(WeaponFactory.WeaponType.MediumCannon), 50));
        blueprintLibrary.put(ShipType.Fighter, new ShipBlueprint(1, ShipHullFactory.ShipHullType.Small,
                List.of(WeaponFactory.WeaponType.Stinger, WeaponFactory.WeaponType.Turret), 100));
        blueprintLibrary.put(ShipType.Destroyer, new ShipBlueprint(2, ShipHullFactory.ShipHullType.Medium,
                List.of(WeaponFactory.WeaponType.Turret, WeaponFactory.WeaponType.Turret,
                        WeaponFactory.WeaponType.MediumBattery, WeaponFactory.WeaponType.HeavyCannon), 250));
        blueprintLibrary.put(ShipType.Cruiser, new ShipBlueprint(3, ShipHullFactory.ShipHullType.Large,
                List.of(WeaponFactory.WeaponType.Turret, WeaponFactory.WeaponType.Turret,
                        WeaponFactory.WeaponType.Turret, WeaponFactory.WeaponType.Turret,
                        WeaponFactory.WeaponType.MediumBattery, WeaponFactory.WeaponType.MediumBattery,
                        WeaponFactory.WeaponType.HeavyCannon, WeaponFactory.WeaponType.PowerfulCannon), 400));

    }

    public static Ship getShip(ShipType shipType) {
        if (!blueprintLibrary.containsKey(shipType)) {
            throw new RuntimeException("ShipFactory library does not contain module with name " + shipType);
        }
        ShipBlueprint bp = blueprintLibrary.get(shipType);
        ShipHull hull = ShipHullFactory.getShipHull(bp.hullType());
        List<ShipModule> weaponList = new ArrayList<>();
        for (WeaponFactory.WeaponType weaponType : bp.weaponTypeList()) {
            weaponList.add(WeaponFactory.getWeapon(weaponType));
        }
        hull.addChild(weaponList);
        String shipName = ShipNameGenerator.getShipName();
        return new Ship(shipType.toString(), bp.rank(), shipName, hull, bp.ammoStorageLimit());
    }
}
