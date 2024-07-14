package Iliatar.ship;

import Iliatar.ship.Ship;
import Iliatar.ship.ShipNameGenerator;
import Iliatar.ship.modules.ShipHull;
import Iliatar.ship.modules.ShipModule;
import Iliatar.ship.modules.fabrics.ShipModuleUniversalFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipFactory {
    private record ShipBlueprint(String shipType, int rank, String hullType, List<String> weaponNameList, int ammoStorageLimit) {}
    private static List<ShipBlueprint> blueprintLibrary;

    static {
        File file = new File("src/main/resources/ships.json");
        try{
            blueprintLibrary = new ObjectMapper().readValue(file, new TypeReference<>() {});
        } catch (IOException e) {
            System.out.println("Error while parsing json from " + file.getPath() + ": " + e.toString());
        }
    }

    public static Ship getShip(String shipType) {
        ShipBlueprint bp = null;

        for (ShipBlueprint libraryBP : blueprintLibrary) {
            if (libraryBP.shipType().equals(shipType)) {
                bp = libraryBP;
                break;
            }
        }

        if (bp == null) {
            throw new RuntimeException("ShipFactory library does not contain module with name " + shipType);
        }

        ShipHull hull = ShipModuleUniversalFactory.getModule(ShipModule.ShipModuleType.ShipHull, bp.hullType());

        List<ShipModule> weaponList = new ArrayList<>();
        for (String weaponName : bp.weaponNameList()) {
            weaponList.add(ShipModuleUniversalFactory.getModule(ShipModule.ShipModuleType.Weapon, weaponName));
        }
        hull.addChild(weaponList);

        String shipName = ShipNameGenerator.getShipName();
        return new Ship(shipType.toString(), bp.rank(), shipName, hull, bp.ammoStorageLimit());
    }

    public static List<Ship> getShips(String shipType, int count) {
        List<Ship> ships = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            ships.add(getShip(shipType));
        }
        return ships;
    }
}
