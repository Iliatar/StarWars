package Iliatar.ship.modules.fabrics;

import Iliatar.ship.modules.AimingModule;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AimingModuleFactory {
    private record AimingModuleBlueprint(String name, int endurance, int size, int mass, double armor, int aimSpeed) {}
    private static List<AimingModuleBlueprint> blueprintLibrary;

    static {
        File file = new File("src/main/resources/aimingmodules.json");
        try {
            blueprintLibrary = new ObjectMapper().readValue(file, new TypeReference<List<AimingModuleBlueprint>>() {});

        } catch (IOException e) {
            System.out.println("Error while parsing json from " + file.getPath() + ": " + e.toString());
        }
    }

    public static AimingModule getAimingModule(String name) {
        AimingModuleBlueprint bp = blueprintLibrary.stream()
                .filter(bluePrint -> bluePrint.name().equals(name))
                .findFirst()
                .orElseThrow(()->new RuntimeException("AimingModuleFactory library does not contain module with name " + name));
        return new AimingModule(bp.name(), bp.endurance(), bp.size(), bp.mass(), bp.armor(), bp.aimSpeed());
    }
}
