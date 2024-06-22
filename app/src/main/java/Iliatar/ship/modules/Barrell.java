package Iliatar.ship.modules;

import Iliatar.ship.Ship;
import Iliatar.utils.randomSelector.Priority;
import Iliatar.utils.randomSelector.PriorityItem;
import Iliatar.utils.randomSelector.RandomSelector;

import java.util.ArrayList;
import java.util.List;

public class Barrell extends ShipModule {
    private final int caliber;
    private final int armorPenetration;

    public Barrell(String name, int endurance, int size, int mass, double armor, int caliber, int armorPenetration) {
        super(name, endurance, size, mass, armor);
        type = ShipModuleType.Barrel;
        this.caliber = caliber;
        this.armorPenetration = armorPenetration;
    }

    public void shoot(Ship targetShip) {
        int undeployedDamage = caliber;
        int undeployedArmorPenetrarion = armorPenetration;


        ShipModule currentModule = null;
        ShipModule nextModule = targetShip.getRootModule();

        do {
            currentModule = nextModule;
            int currentModuleArmor = (int) currentModule.getCurrentArmor();
            int armorDamage = Math.min(currentModuleArmor, undeployedArmorPenetrarion);
            undeployedArmorPenetrarion -= armorDamage;
            if (currentModuleArmor > armorDamage) {
                int caliberDeployedArmorPenetration = Math.min(undeployedDamage, currentModuleArmor - armorDamage);
                armorDamage += caliberDeployedArmorPenetration;
                undeployedDamage -= caliberDeployedArmorPenetration;
            }
            currentModule.takeArmorDamage(armorDamage);

            if (undeployedDamage <= 0) return;
            if (currentModule.getChildModules().isEmpty()) { continue; }

            List<Priority<ShipModule>> modulesPriorityItems = new ArrayList<>();
            modulesPriorityItems.add(new PriorityItem<ShipModule>(currentModule, currentModule.getSize()));
            for (ShipModule childModule : currentModule.getChildModules()) {
                modulesPriorityItems.add(new PriorityItem<ShipModule>(childModule, childModule.getSize()));
            }

            nextModule = RandomSelector.selectRandomByPriority(modulesPriorityItems);
        } while (nextModule != currentModule);

        currentModule.takeDamage(undeployedDamage);
    }

    public int getCaliber() {
        return caliber;
    }

    public int getArmorPenetration() {
        return armorPenetration;
    }
}
