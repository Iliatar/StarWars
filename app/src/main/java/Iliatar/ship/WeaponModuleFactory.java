package Iliatar.ship;

import java.util.HashMap;
import java.util.Map;

public class WeaponModuleFactory {
    record WeaponBlueprint(int barrelCount, int barrelCaliber, int endurance) {}
    private static Map<String, WeaponBlueprint> weaponLibrary;

    static {
        weaponLibrary = new HashMap();
        weaponLibrary.put("Light cannon", new WeaponBlueprint(1, 1, 5));
        weaponLibrary.put("Cannon", new WeaponBlueprint(1, 3, 12));
        weaponLibrary.put("Heavy Cannon", new WeaponBlueprint(1, 6, 25));
        weaponLibrary.put("Rotary Cannon", new WeaponBlueprint(4, 3, 18));
        weaponLibrary.put("Medium Turret", new WeaponBlueprint(2, 4, 30));
        weaponLibrary.put("Light Turret", new WeaponBlueprint(2, 2, 20));
        weaponLibrary.put("Heavy Turret", new WeaponBlueprint(2, 8, 45));
        weaponLibrary.put("Minigun", new WeaponBlueprint(5, 2, 20));
        weaponLibrary.put("Main Caliber", new WeaponBlueprint(1, 12, 30));

    }
    public static WeaponModule getWeapon(String moduleName) {
        if (!weaponLibrary.containsKey(moduleName)) {
            throw new IllegalArgumentException("WeaponModuleFactory library does not contain module with name " + moduleName);
        }
        WeaponBlueprint blueprint = weaponLibrary.get(moduleName);
        return new WeaponModule(moduleName, blueprint.barrelCount(), blueprint.barrelCount(), blueprint.endurance());
    }
}
