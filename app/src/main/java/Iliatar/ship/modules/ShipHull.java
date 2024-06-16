package Iliatar.ship.modules;

import Iliatar.ship.Ship;

public class ShipHull extends ShipModule {
    private Ship parentShip;

    public ShipHull(int endurance, int size, int mass, double armor) {
        super(endurance, size, mass, armor);
        type = ShipModuleType.ShipHull;
    }

    public void setParentShip(Ship parentShip) { this.parentShip = parentShip; }
    public Ship getParentShip() { return parentShip; }
}
