package Iliatar.ship.modules.fabrics;

import Iliatar.ship.modules.AimingModule;
import Iliatar.ship.modules.AmmoLoader;

import java.util.HashMap;
import java.util.Map;

public class AmmoLoaderFactory {
    public enum AmmoLoaderType {Light, Medium, Large}
    private record AmmoLoaderBlueprint(int endurance, int size, int mass, double armor, int loadSpeed) {}
    private static Map<AmmoLoaderType, AmmoLoaderBlueprint> blueprintLibrary;

    static {
        blueprintLibrary = new HashMap<>();
        blueprintLibrary.put(AmmoLoaderType.Light, new AmmoLoaderBlueprint(5, 2, 1, 0, 3));
        blueprintLibrary.put(AmmoLoaderType.Medium, new AmmoLoaderBlueprint(8, 3, 2, 0, 5));
        blueprintLibrary.put(AmmoLoaderType.Large, new AmmoLoaderBlueprint(12, 4, 3, 0, 8));
    }

    public static AmmoLoader getAmmoLoader(AmmoLoaderType ammoLoaderType) {
        if (!blueprintLibrary.containsKey(ammoLoaderType)) {
            throw new RuntimeException("AmmoLoaderFactory library does not contain module with name " + ammoLoaderType);
        }
        AmmoLoaderBlueprint bp = blueprintLibrary.get(ammoLoaderType);
        return new AmmoLoader(ammoLoaderType.toString() + " aiming module", bp.endurance(), bp.size(), bp.mass(), bp.armor(), bp.loadSpeed());
    }
}
