package Iliatar.ship.modules.fabrics;

import Iliatar.ship.modules.AimingModule;

import java.util.HashMap;
import java.util.Map;

public class AimingModuleFactory {
    public enum AimingModuleType {Light, Medium, Large}
    private record AimingModuleBlueprint(int endurance, int size, int mass, double armor, int aimSpeed) {}
    private static Map<AimingModuleType, AimingModuleBlueprint> blueprintLibrary;

    static {
        blueprintLibrary = new HashMap<>();
        blueprintLibrary.put(AimingModuleType.Light, new AimingModuleBlueprint(3, 1, 1, 0, 3));
        blueprintLibrary.put(AimingModuleType.Medium, new AimingModuleBlueprint(5, 2, 2, 1, 5));
        blueprintLibrary.put(AimingModuleType.Large, new AimingModuleBlueprint(10, 4, 3, 1, 8));
    }

    public static AimingModule getAimingModule(AimingModuleType aimingModuleType) {
        if (!blueprintLibrary.containsKey(aimingModuleType)) {
            throw new RuntimeException("AimingModuleFactory library does not contain module with name " + aimingModuleType);
        }
        AimingModuleBlueprint bp = blueprintLibrary.get(aimingModuleType);
        return new AimingModule(aimingModuleType.toString() + " aiming module", bp.endurance(), bp.size(), bp.mass(), bp.armor(), bp.aimSpeed());
    }
}
