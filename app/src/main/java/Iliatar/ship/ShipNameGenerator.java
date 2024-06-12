package Iliatar.ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShipNameGenerator {
    static List<String> nameList;

    static {
        nameList = new ArrayList<>();
        nameList.add("Vlaer");
        nameList.add("Inquisitor");
        nameList.add("Pollux");
        nameList.add("Bastion");
        nameList.add("Rexanta");
        nameList.add("Pharaoh");
        nameList.add("Caesar");
        nameList.add("Explorer");
        nameList.add("Dominator");
        nameList.add("Thunderbolt");
        nameList.add("Trident");
        nameList.add("Patriarch");
        nameList.add("Wespa");
        nameList.add("Morningstar");
    }

    public static String getShipName() {
        int index = (int) (Math.random() * nameList.size());
        String result = nameList.get(index);
        nameList.remove(index);
        return result;
    }
}
