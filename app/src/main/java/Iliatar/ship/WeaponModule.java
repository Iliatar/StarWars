package Iliatar.ship;

import Iliatar.battle.BattleLogger;
import Iliatar.battle.BattleManager;
import Iliatar.utils.randomSelector.Priority;
import Iliatar.utils.randomSelector.PriorityItem;
import Iliatar.utils.randomSelector.RandomSelector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WeaponModule {
    private final static double COMPLETE_AIM_PROGRESS = 100;
    private final static double RESIDUAL_AIM_PROGRESS = 60;
    private final static double BASE_AIM_SPEED = 10;
    private final static double DAMAGE_BARREL_CHANCE = 0.05;
    private final static double RESET_AIMING_CHANCE = 0.25;
    private final static double DAMAGE_AIMING_CHANCE = 0.1;
    private final static double DAMAGE_AIMING_SPEED_REDUCTION = 0.2;
    private final static int POTENTIAL_TARGETS_INITIAL_COUNT = 10;
    private final static int POTENTIAL_TARGETS_FINAL_COUNT = 3;
    private final int barrelCount;
    private final int barrelCaliber;
    private final int endurance;
    private final String name;
    private Ship parentShip;
    private int damage;
    private int damagedBarrelCount;
    private int damagedAiming;
    private Ship targetShip;
    private double aimProgress;
    private BattleManager battleManager;

    public WeaponModule(String name, int barrelCount, int barrelCaliber, int endurance) {
        this.barrelCount = barrelCount;
        this.barrelCaliber = barrelCaliber;
        this.endurance = endurance;
        this.name = name;
        damage = 0;
        damagedBarrelCount = 0;
        damagedAiming = 0;
    }
    public void setParentShip(Ship parentShip) {
        this.parentShip = parentShip;
    }

    public void initiateForBattle(BattleManager battleManager) {
        this.battleManager = battleManager;
        targetShip = null;
        aimProgress = 0;
        selectTarget();
    }
    public void finalizeBattle() {
        battleManager = null;
        targetShip = null;
    }

    private void selectTarget() {
        List<Ship> ships = battleManager.getPotentialTargets(this.parentShip, POTENTIAL_TARGETS_INITIAL_COUNT);
        List<Priority<Ship>> potentialTargets = new ArrayList<>(ships.size() + 1);

        //TODO отрефакторить этот блок
        for(Ship ship : ships) {
            double aimSpeed = getAimingSpeed(ship);
            double initialAimProgress = 0;
            double priority = (COMPLETE_AIM_PROGRESS - initialAimProgress) / aimSpeed;
            PriorityItem<Ship> potentialTarget = new PriorityItem(ship, priority);
            potentialTargets.add(potentialTarget);
        }

        if (targetShip != null && targetShip.isActive()) {
            double aimSpeed = getAimingSpeed(targetShip);
            double priority = (COMPLETE_AIM_PROGRESS - RESIDUAL_AIM_PROGRESS) / aimSpeed;
            PriorityItem<Ship> potentialTarget = new PriorityItem(targetShip, priority);
            potentialTargets.add(potentialTarget);
        }

        if (potentialTargets.isEmpty()) {
            BattleLogger.logWeaponMessage(this, "select target: potential targets list is empty");
            return;
        }

        potentialTargets =  potentialTargets.stream()
                                            .sorted(Comparator.comparingDouble(Priority::getPriority))
                                            .limit(POTENTIAL_TARGETS_FINAL_COUNT)
                                            .toList();

        Ship newTargetShip = RandomSelector.selectRandomByPriority(potentialTargets);
        if (newTargetShip == targetShip) {
            aimProgress = RESIDUAL_AIM_PROGRESS;
        } else {
            aimProgress = 0;
        }
        targetShip = newTargetShip;

        BattleLogger.logWeaponMessage(this, "select target: " + targetShip.getShipType() + " " + targetShip.getName());
        BattleLogger.logMessage("Calculated aiming speed = " + getAimingSpeed(targetShip) + "; initial aim progress = " + aimProgress);
    }

    public boolean isActive(){
        //System.out.println("WeaponModule.isActive(). damage < endurance = " + (damage < endurance) + "; damagedBarrelCount < barrelCount = " + (damagedBarrelCount < barrelCount) + "; parentShip.getAmmoModule().isEnoughAmount(barrelCaliber * getActiveBarrelCount()) = " + parentShip.getAmmoModule().isEnoughAmount(barrelCaliber * getActiveBarrelCount()));
        return damage < endurance && damagedBarrelCount < barrelCount && parentShip.getAmmoModule().isEnoughAmount(barrelCaliber * getActiveBarrelCount());
    }

    public void getDamage(int damageAmount) {
        damage += damageAmount;
        BattleLogger.logMessage(name + " of " + parentShip.getShipType() + " get damage " + damageAmount + "; endurance damage is " + damage + " of " + endurance);

        if (damage >= endurance) {
            return;
        }

        //TODO сделать реализоцию более гибкой
        double diceRoll = Math.random();
        if (diceRoll < DAMAGE_BARREL_CHANCE) {
            damagedBarrelCount++;
            BattleLogger.logMessage(name + " of " + parentShip.getShipType() + " barrel damaged. Damaged barrel count is " + damagedBarrelCount);
        }

        diceRoll = Math.random();
        if (diceRoll < RESET_AIMING_CHANCE) {
            BattleLogger.logMessage(name + " of " + parentShip.getShipType() + " aiming reseted");
            aimProgress = 0;
        }

        diceRoll = Math.random();
        if (diceRoll < DAMAGE_AIMING_CHANCE) {
            damagedAiming++;
            BattleLogger.logMessage(name + " of " + parentShip.getShipType() + " aiming damaged. Aiming damage is " + damagedAiming);
        }
    }

    public void processBattleTurn(double deltaTime) {
        if (!isActive()) {
            return;
        }

        if (!targetShip.isActive()) {
            selectTarget();
        }

        aimProgress += getAimingSpeed(targetShip);

        if (aimProgress >= COMPLETE_AIM_PROGRESS) {
            shoot();
        }
    }

    private void shoot() {
        if (parentShip.getAmmoModule().spend(barrelCaliber * getActiveBarrelCount())) {
            BattleLogger.logWeaponMessage(this, "shoots to " + targetShip.getShipType());
            BattleLogger.logShipMessage(parentShip, "ammo storage: " + parentShip.getAmmoModule().getAmount());
            targetShip.getShoot(this);
            aimProgress = 0;
            selectTarget();
        }
    }

    private double getAimingSpeed (Ship targetShip) {
        double aimingSpeed = BASE_AIM_SPEED * Math.sqrt(targetShip.getMass()) / (Math.sqrt(barrelCaliber * barrelCount) * targetShip.getActualManeuverability());
        double overlapping = battleManager.getBattleOverlap().getOverlap(parentShip, targetShip);
        aimingSpeed /= 1 + overlapping;
        aimingSpeed /= 1 + damagedAiming * DAMAGE_AIMING_SPEED_REDUCTION;
        return aimingSpeed;
    }
    public int getBarrelCount() {
        return barrelCount;
    }
    public int getActiveBarrelCount() {return barrelCount - damagedBarrelCount; }
    public int getBarrelCaliber() {
        return barrelCaliber;
    }
    public String getName() { return name; }
    public int getEndurance() { return endurance; }
    public int getDamage() { return damage; }
    public Ship getParentShip() { return parentShip; }
}
