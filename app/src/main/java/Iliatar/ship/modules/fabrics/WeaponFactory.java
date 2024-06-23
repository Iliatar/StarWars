package Iliatar.ship.modules.fabrics;

import Iliatar.ship.modules.AimingModule;
import Iliatar.ship.modules.AmmoLoader;
import Iliatar.ship.modules.Barrell;
import Iliatar.ship.modules.Weapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeaponFactory {
    public enum WeaponType {Stinger, LightCannon, Turret, MediumCannon, HeavyCannon, MediumBattery, PowerfulCannon}
    private record WeaponBlueprint (int endurance, int size, int mass, double armor,
                                    AimingModuleFactory.AimingModuleType aimingModuleType,
                                    AmmoLoaderFactory.AmmoLoaderType ammoLoaderType,
                                    BarrellFactory.BarrellType barrellType, int barrellCount) {}

    private static Map<WeaponType, WeaponBlueprint> blueprintLibrary;

    static {
        blueprintLibrary = new HashMap<>();
        blueprintLibrary.put(WeaponType.Stinger, new WeaponBlueprint(8, 3, 2, 0,
                AimingModuleFactory.AimingModuleType.Light, AmmoLoaderFactory.AmmoLoaderType.Light, BarrellFactory.BarrellType.Mini, 1));
        blueprintLibrary.put(WeaponType.LightCannon, new WeaponBlueprint(15, 5, 3, 0,
                AimingModuleFactory.AimingModuleType.Light, AmmoLoaderFactory.AmmoLoaderType.Light, BarrellFactory.BarrellType.Light, 1));
        blueprintLibrary.put(WeaponType.Turret, new WeaponBlueprint(25, 10, 8, 1,
                AimingModuleFactory.AimingModuleType.Light, AmmoLoaderFactory.AmmoLoaderType.Light, BarrellFactory.BarrellType.Light, 3));
        blueprintLibrary.put(WeaponType.MediumCannon, new WeaponBlueprint(20, 7, 5, 0,
                AimingModuleFactory.AimingModuleType.Medium, AmmoLoaderFactory.AmmoLoaderType.Medium, BarrellFactory.BarrellType.Medium, 1));
        blueprintLibrary.put(WeaponType.HeavyCannon, new WeaponBlueprint(25, 12, 9, 0,
                AimingModuleFactory.AimingModuleType.Large, AmmoLoaderFactory.AmmoLoaderType.Large, BarrellFactory.BarrellType.Large, 1));
        blueprintLibrary.put(WeaponType.MediumBattery, new WeaponBlueprint(45, 20, 13, 2,
                AimingModuleFactory.AimingModuleType.Large, AmmoLoaderFactory.AmmoLoaderType.Large, BarrellFactory.BarrellType.Medium, 3));
        blueprintLibrary.put(WeaponType.PowerfulCannon, new WeaponBlueprint(50, 20, 20, 0,
                AimingModuleFactory.AimingModuleType.Large, AmmoLoaderFactory.AmmoLoaderType.Large, BarrellFactory.BarrellType.Powerful, 1));

    }

    public static Weapon getWeapon(WeaponType weaponType) {
        if (!blueprintLibrary.containsKey(weaponType)) {
            throw new RuntimeException("WeaponFactory library does not contain module with name " + weaponType);
        }
        WeaponBlueprint bp = blueprintLibrary.get(weaponType);
        AimingModule aimingModule = AimingModuleFactory.getAimingModule(bp.aimingModuleType());
        AmmoLoader ammoLoader = AmmoLoaderFactory.getAmmoLoader(bp.ammoLoaderType());
        List<Barrell> barrellList = new ArrayList<>();
        for (int i = 0; i < bp.barrellCount(); i++) {
            barrellList.add(BarrellFactory.getBarrell(bp.barrellType()));
        }
        return new Weapon("Weapon " + weaponType.toString(), bp.endurance(), bp.size(), bp.mass(), bp.armor(), aimingModule, ammoLoader, barrellList);
    }
}
