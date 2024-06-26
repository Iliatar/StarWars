package Iliatar.ship;

import java.util.ArrayList;
import java.util.List;

public class ShipNameGenerator {
    static List<String> nameList;
    static long namelessCounter = 1;

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
        nameList.add("Persey");
        nameList.add("Kingman");
        nameList.add("Lightbringer");
        nameList.add("Galactic Queen");
        nameList.add("Hammer");
        nameList.add("Bastion");
        nameList.add("NightWolf");
        nameList.add("Piranha");
        nameList.add("DoubleTrouble");
        nameList.add("Seeker");
        nameList.add("Scorpion");
        nameList.add("PunchCrunch");
        nameList.add("Torn");
        nameList.add("Panzer");
        nameList.add("Voideater");
    }

    public static String getShipName() {
        if (nameList.size() > 0) {
            int index = (int) (Math.random() * nameList.size());
            //System.out.println("index = " + index + "; nameList.size() = " + nameList.size());
            String result = nameList.get(index);
            nameList.remove(index);
            return result;
        }
        return "Ship" + namelessCounter++;
    }
}
