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
    /*public void getShoot(WeaponModule weaponModule) {
        double shootArmorDamage = weaponModule.getActiveBarrelCount() * Math.min(weaponModule.getBarrelCaliber(), getActualArmor()) * ARMOR_DEGRADATION_PER_DAMAGE_UNIT;
        armorDamage += shootArmorDamage;
        BattleLogger.logShipMessage(this, "get armor damage " + shootArmorDamage + "; actual armor is " + getActualArmor());

        int barellEnduranceDamage = (int)Math.ceil(weaponModule.getBarrelCaliber() - getActualArmor());
        if (barellEnduranceDamage <= 0) {
            return;
        }

        int enduranceDamage = barellEnduranceDamage * weaponModule.getActiveBarrelCount();
        damage += enduranceDamage;
        BattleLogger.logShipMessage(this, "get endurance damage " + enduranceDamage + "; endurance damage is " + damage + " of " + endurance);
        if (damage >= endurance) {
            return;
        }

        for (int i = 0; i < weaponModule.getActiveBarrelCount(); i++) {
            double diceRoll = Math.random();
            if (diceRoll < MANEUVERABILITY_DAMAGE_CHANCE * barellEnduranceDamage) {
                maneuverabilityDamage++;
                BattleLogger.logShipMessage(this, "get maneuverability damage. Maneuverability damage is " + maneuverabilityDamage);
                return;
            }

            diceRoll = Math.random();
            if (diceRoll < AMMO_DETONATION_CHANCE * barellEnduranceDamage) {
                damage = endurance;
                BattleLogger.logShipMessage(this, "destroyed by ammo detonation");
                return;
            }

            diceRoll = Math.random();
            if (diceRoll < SPACE_ENGINES_DAMAGE_CHANCE * barellEnduranceDamage) {
                spaceEnginesDamage++;
                BattleLogger.logShipMessage(this, "get space engines damage. Space engines damage is " + spaceEnginesDamage);
                return;
            }

            for (WeaponModule weapon : weapons) {
                diceRoll = Math.random();
                if (diceRoll < WEAPON_MODULE_DAMAGE_CHANCE * barellEnduranceDamage) {
                    weapon.getDamage(barellEnduranceDamage);
                    return;
                }
            }
        }
    }*/

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
