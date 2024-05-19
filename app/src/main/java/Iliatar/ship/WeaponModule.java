package Iliatar.ship;

import Iliatar.battle.BattleManager;
import Iliatar.utils.Priority;
import Iliatar.utils.RandomSelector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WeaponModule {
    public record PotentialShipTarget (Ship targetShip, double priority) implements Priority {
        public double getPriority() {return priority; }
    }
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
    private final Ship parentShip = null;
    private int damage;
    private int damagedBarrelCount;
    private int damagedAiming;
    private Ship targetShip;
    private double aimProgress;
    private BattleManager battleManager;

    public WeaponModule(int barrelCount, int barrelCaliber, int endurance) {
        this.barrelCount = barrelCount;
        this.barrelCaliber = barrelCaliber;
        this.endurance = endurance;
        damage = 0;
        damagedBarrelCount = 0;
        damagedAiming = 0;
    }

    public void initiateForBattle(BattleManager battleManager) {
        this.battleManager = battleManager;
        targetShip = null;
        aimProgress = 0;
        selectTarget();
    }

    private void selectTarget() {
        List<Ship> ships = battleManager.getPotentialTargets(this);
        List<PotentialShipTarget> potentialTargets = new ArrayList<>(ships.size() + 1);

        //TODO отрефакторить этот блок
        for(Ship ship : ships) {
            double aimSpeed = getAimingSpeed(ship);
            double initialAimProgress = 0;
            double priority = (COMPLETE_AIM_PROGRESS - initialAimProgress) / aimSpeed;
            PotentialShipTarget potentialTarget = new PotentialShipTarget(ship, priority);
            potentialTargets.add(potentialTarget);
        }

        if (targetShip != null && targetShip.isActive()) {
            double aimSpeed = getAimingSpeed(targetShip);
            double priority = (COMPLETE_AIM_PROGRESS - RESIDUAL_AIM_PROGRESS) / aimSpeed;
            PotentialShipTarget potentialTarget = new PotentialShipTarget(targetShip, priority);
            potentialTargets.add(potentialTarget);
        }

        potentialTargets =  potentialTargets.stream()
                                            .sorted(Comparator.comparingDouble(PotentialShipTarget::priority))
                                            .limit(POTENTIAL_TARGETS_FINAL_COUNT)
                                            .toList();

        targetShip = RandomSelector.selectRandomByPriority(potentialTargets).targetShip();
    }

    public boolean isActive(){
        return damage < endurance && damagedBarrelCount < barrelCount && parentShip.isEnoughAmmo(this);
    }

    public void getDamage(int damageAmount) {
        damage += damageAmount;

        if (damage >= endurance) {
            return;
        }

        //TODO сделать реализоцию более гибкой
        double diceRoll = Math.random();
        if (diceRoll < DAMAGE_BARREL_CHANCE) {
            damagedBarrelCount++;
        }

        diceRoll = Math.random();
        if (diceRoll < RESET_AIMING_CHANCE) {
            aimProgress = 0;
        }

        diceRoll = Math.random();
        if (diceRoll < DAMAGE_AIMING_CHANCE) {
            damagedAiming++;
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
        parentShip.spendAmmo(this);
        targetShip.getShoot(this);
        selectTarget();
    }

    private double getAimingSpeed (Ship targetShip) {
        double aimingSpeed = BASE_AIM_SPEED * Math.sqrt(targetShip.getMass()) / (Math.sqrt(barrelCaliber) * targetShip.getManeuverability());
        double overlapping = battleManager.getBattleOverlap().getOverlap(parentShip, targetShip);
        aimingSpeed /= 1 + overlapping;
        aimingSpeed /= 1 + damagedAiming * DAMAGE_AIMING_SPEED_REDUCTION;
        return aimingSpeed;
    }
}
