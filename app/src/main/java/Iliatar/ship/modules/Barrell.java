package Iliatar.ship.modules;

import Iliatar.ship.Ship;

public class Barrell extends ShipModule {
    private final int caliber;
    private final int armorPenetration;

    public Barrell(int endurance, int size, int mass, double armor, int caliber, int armorPenetration) {
        super(endurance, size, mass, armor);
        type = ShipModuleType.Barrel;
        this.caliber = caliber;
        this.armorPenetration = armorPenetration;
    }

    public void shoot(Ship targetShip) {

    }

    public int getCaliber() {
        return caliber;
    }

    public int getArmorPenetration() {
        return armorPenetration;
    }
}
