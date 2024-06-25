package Iliatar.ship;

import Iliatar.battle.BattleManager;
import Iliatar.battle.Fleet;
import Iliatar.ship.modules.ShipHull;
import Iliatar.ship.modules.ShipModule;

public class Ship {
    public static final int MAX_SHIP_RANK = 3;
    private final int rank;
    private final String shipType;
    private final String name;
    private final StorageModule ammoModule;
    private Fleet fleet;
    private final ShipHull rootModule;


    public Ship (String shipType, int rank, String name, ShipHull rootModule, int ammoStorageLimit) {
        this.shipType = shipType;
        this.rank = rank;
        this.name = name;
        this.rootModule = rootModule;
        rootModule.setParentShip(this);
        ammoModule = new StorageModule(ammoStorageLimit);
    }

    public void processBattleTurn(int deltaTime) {
        rootModule.calculateTurn(deltaTime);
    }

    public void initiateForBattle(BattleManager battleManager) {
        rootModule.initiateForBattle(battleManager);
    }

    public void finalizeBattle() {
        rootModule.finalizeBattle();
    }

    public ShipModule.ShipModuleStatus getStatus() {
        return rootModule.getStatus();
    }

    public void setFleet(Fleet fleet) { this.fleet = fleet; }

    public StorageModule getAmmoModule() {
        return ammoModule;
    }
    public int getRank() {
        return rank;
    }

    public double getMass() { return rootModule.getTotalMass(); }
    public double getSize() { return rootModule.getTotalSize(); }
    public double getManeuverability() { return 500 / getMass(); }
    public String getShipType() { return shipType; }
    public Fleet getFleet() { return fleet; }
    public String getName() { return name; }
    public ShipModule getRootModule() { return rootModule; }
}
