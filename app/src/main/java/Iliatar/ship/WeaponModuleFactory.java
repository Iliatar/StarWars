package Iliatar.ship;

import java.util.HashMap;
import java.util.Map;

public class WeaponModuleFactory {
    record WeaponBlueprint(int barrelCount, int barrelCaliber, int endurance) {}
    private static Map<String, WeaponBlueprint> blueprintLibrary;

    static {
        blueprintLibrary = new HashMap();
        blueprintLibrary.put("Light cannon", new WeaponBlueprint(1, 1, 5));
        blueprintLibrary.put("Cannon", new WeaponBlueprint(1, 3, 12));
        blueprintLibrary.put("Heavy Cannon", new WeaponBlueprint(1, 6, 25));
        blueprintLibrary.put("Rotary Cannon", new WeaponBlueprint(4, 3, 18));
        blueprintLibrary.put("Medium Turret", new WeaponBlueprint(2, 4, 30));
        blueprintLibrary.put("Light Turret", new WeaponBlueprint(2, 2, 20));
        blueprintLibrary.put("Heavy Turret", new WeaponBlueprint(2, 8, 45));
        blueprintLibrary.put("Minigun", new WeaponBlueprint(5, 2, 20));
        blueprintLibrary.put("Main Caliber", new WeaponBlueprint(1, 12, 30));

    }
    public static WeaponModule getWeapon(String moduleName) {
        if (!blueprintLibrary.containsKey(moduleName)) {
            throw new IllegalArgumentException("WeaponModuleFactory library does not contain module with name " + moduleName);
        }
        WeaponBlueprint blueprint = blueprintLibrary.get(moduleName);
        return new WeaponModule(moduleName, blueprint.barrelCount(), blueprint.barrelCount(), blueprint.endurance());
    }
}
