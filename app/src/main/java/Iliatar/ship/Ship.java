package Iliatar.ship;

import Iliatar.battle.BattleLogger;
import Iliatar.battle.BattleManager;

import java.util.List;

public class Ship {
    public static final int MAX_SHIP_RANK = 3;
    private static final double MANEUVERABILITY_DAMAGE_CHANCE = 0.1;
    private static final double MANEUVERABILITY_DAMAGE_REDUCTION = 0.1;
    private static final double AMMO_DETONATION_CHANCE = 0.001;
    private static final double WEAPON_MODULE_DAMAGE_CHANCE = 0.05;
    private static final double SPACE_ENGINES_DAMAGE_CHANCE = 0.05;
    private static final double SPACE_ENGINE_DAMAGE_SPEED_REDUCTION = 0.1;
    private static final double ARMOR_DEGRADATION_PER_DAMAGE_UNIT = 0.001;
    private final int rank;
    private final int endurance;
    private final double mass;
    private final double maneuverability;
    private final double spaceSpeed;
    private final double armor;
    private final String shipType;
    private int damage;
    private int maneuverabilityDamage;
    private int spaceEnginesDamage;
    private double armorDamage;
    private final List<WeaponModule> weapons;
    private final StorageModule ammoModule;


    public Ship (String shipType, int rank, int endurance, double mass, double maneuverability, double spaceSpeed, double armor, List<WeaponModule> weapons, int ammoStorageLimit) {
        this.shipType = shipType;
        this.rank = rank;
        this.endurance = endurance;
        damage = 0;
        this.mass = mass;
        this.maneuverability = maneuverability;
        maneuverabilityDamage = 0;
        this.spaceSpeed = spaceSpeed;
        spaceEnginesDamage = 0;
        this.armor = armor;
        armorDamage = 0;
        this.weapons = List.copyOf(weapons);
        this.weapons.stream().forEach(weapon -> weapon.setParentShip(this));
        ammoModule = new StorageModule(ammoStorageLimit);
    }

    public void processBattleTurn(double deltaTime) {
        weapons.forEach(weaponModule -> weaponModule.processBattleTurn(deltaTime));
    }
    public void getShoot(WeaponModule weaponModule) {
        double shootArmorDamage = weaponModule.getActiveBarrelCount() * Math.min(weaponModule.getBarrelCaliber(), getActualArmor()) * ARMOR_DEGRADATION_PER_DAMAGE_UNIT;
        armorDamage += shootArmorDamage;
        BattleLogger.logMessage("Ship " + shipType + " get armor damage " + shootArmorDamage + "; actual armor is " + getActualArmor());

        int barellEnduranceDamage = (int)Math.ceil(weaponModule.getBarrelCaliber() - getActualArmor());
        if (barellEnduranceDamage <= 0) {
            return;
        }

        int enduranceDamage = barellEnduranceDamage * weaponModule.getActiveBarrelCount();
        damage += enduranceDamage;
        BattleLogger.logMessage("Ship " + shipType + " get endurance damage " + enduranceDamage + "; endurance damage is " + damage + " of " + endurance);
        if (damage >= endurance) {
            return;
        }

        //TODO сделать реализоцию более гибкой
        for (int i = 0; i < weaponModule.getActiveBarrelCount(); i++) {
            double diceRoll = Math.random();
            if (diceRoll < MANEUVERABILITY_DAMAGE_CHANCE * barellEnduranceDamage) {
                maneuverabilityDamage++;
                BattleLogger.logMessage("Ship " + shipType + " get maneuverability damage. Maneuverability damage is " + maneuverabilityDamage);
                return;
            }

            diceRoll = Math.random();
            if (diceRoll < AMMO_DETONATION_CHANCE * barellEnduranceDamage) {
                damage = endurance;
                BattleLogger.logMessage("Ship " + shipType + " destroyed by ammo detonation");
                return;
            }

            diceRoll = Math.random();
            if (diceRoll < SPACE_ENGINES_DAMAGE_CHANCE * barellEnduranceDamage) {
                spaceEnginesDamage++;
                BattleLogger.logMessage("Ship " + shipType + " get space engines damage. Space engines damage is " + maneuverabilityDamage);
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
    }
    public void initiateForBattle(BattleManager battleManager) {
        weapons.forEach(weaponModule -> weaponModule.initiateForBattle(battleManager));
    }

    public void finalizeBattle() {
        weapons.forEach(weaponModule -> weaponModule.finalizeBattle());
    }

    public boolean isActive() {
        //System.out.println("Ship.isActive().isDestroyed() = " + isDestroyed() + "; weapons.stream().filter(WeaponModule::isActive).count() > 0 = "  + (weapons.stream().filter(WeaponModule::isActive).count() > 0));
        //System.out.println("weapons.size() = " + weapons.size());
        return !isDestroyed() && weapons.stream().filter(WeaponModule::isActive).count() > 0;
    }
    public boolean isDestroyed() {return damage >= endurance; }

    public StorageModule getAmmoModule() {
        return ammoModule;
    }
    public int getRank() {
        return rank;
    }
    public double getMass() { return mass; }
    public double getManeuverability() { return maneuverability; }
    public double getActualManeuverability() { return maneuverability * Math.max (1 - maneuverabilityDamage * MANEUVERABILITY_DAMAGE_REDUCTION, 0); }
    public double getArmor() {
        return armor;
    }
    public double getActualArmor() { return armor - armorDamage; }
    public double getSpaceSpeed() { return spaceSpeed; }
    public double getActualSpaceSpeed() { return spaceSpeed * Math.max(1 - spaceEnginesDamage * SPACE_ENGINE_DAMAGE_SPEED_REDUCTION, 0); }
    public int getEndurance() { return endurance; }
    public int getActualEndurance() { return endurance - damage; }
    public String getShipType() { return shipType; }
}
