package Iliatar.ship.modules.fabrics;

import Iliatar.ship.modules.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShipModuleUniversalFactory {
    private interface ShipModuleFactory<T extends ShipModule> {
        T getModule(String name);
    }
    private interface ModuleConstructor<T extends ShipModule> {
        T getModule(ShipModuleBlueprint blueprint);
    }

    private static class ModuleFactory<T extends ShipModule> implements ShipModuleFactory {
        private String filePath;
        ModuleConstructor<T> moduleConstructor;

        @Override
        public T getModule(String name) {
            File file = new File(filePath);
            ShipModuleBlueprint bp = null;
            try {
                List<ShipModuleBlueprint> blueprintLibrary = new ObjectMapper().readValue(file, new TypeReference<>() {});
                bp = blueprintLibrary.stream()
                        .filter(bluePrint -> bluePrint.getName().equals(name))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("library " + filePath + " does not contain module with name " + name));
            } catch (IOException e) {
                System.out.println("Error while parsing json from " + file.getPath() + ": " + e.toString());
            }
            return moduleConstructor.getModule(bp);
        }

        ModuleFactory(String filePath, ModuleConstructor moduleConstructor) {
            this.filePath = filePath;
            this.moduleConstructor = moduleConstructor;
        }
    }
    private static ShipModuleFactory getFactory(ShipModule.ShipModuleType type) {
        ShipModuleFactory result = null;

        switch (type) {

            case Weapon -> {
                result = new ModuleFactory<Weapon>("src/main/resources/weapons.json", (bp) -> {
                    AimingModule aimingModule = getModule(ShipModule.ShipModuleType.AimingModule, bp.getAimingModuleName());
                    AmmoLoader ammoLoader = getModule(ShipModule.ShipModuleType.AmmoLoader, bp.getAmmoLoaderName());
                    List<Barrell> barrellList = new ArrayList<>();
                    for (int i = 0; i < bp.getBarrellCount(); i++) {
                        barrellList.add(getModule(ShipModule.ShipModuleType.Barrel, bp.getBarrellName()));
                    }
                    return new Weapon(bp.getName(), bp.getEndurance(), bp.getSize(), bp.getMass(), bp.getArmor(), aimingModule, ammoLoader, barrellList);
                });
            }

            case Barrel -> {
                result = new ModuleFactory<Barrell>("src/main/resources/barrels.json",
                        (bp) -> new Barrell(bp.getName(), bp.getEndurance(), bp.getSize(), bp.getMass(), bp.getArmor(), bp.getCaliber(), bp.getArmorPenetration()));
            }

            case AimingModule -> {
                result = new ModuleFactory<AimingModule>("src/main/resources/aimingmodules.json",
                        (bp) -> new AimingModule(bp.getName(), bp.getEndurance(), bp.getSize(), bp.getMass(), bp.getArmor(), bp.getFunctionSpeed()));
            }

            case AmmoLoader -> {
                result = new ModuleFactory<AmmoLoader>("src/main/resources/ammoloadermodules.json", (bp) -> {
                    return  new AmmoLoader(bp.getName(), bp.getEndurance(), bp.getSize(), bp.getMass(), bp.getArmor(), bp.getFunctionSpeed());
                });
            }

            case ShipHull -> {
                result = new ModuleFactory<ShipHull>("src/main/resources/shiphulls.json", (bp) -> {
                   return new ShipHull(bp.getName(), bp.getEndurance(), bp.getSize(), bp.getMass(), bp.getArmor());
                });
            }
            default -> throw new RuntimeException("Unknown ship module type");
        }

        return result;
    }

    public static <T extends ShipModule> T getModule (ShipModule.ShipModuleType type, String name) {
        ShipModuleFactory<T> factory = getFactory(type);
        return factory.getModule(name);
    }
}
