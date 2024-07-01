package Iliatar.ship.modules;

import Iliatar.battle.BattleLogger;
import Iliatar.battle.BattleManager;
import Iliatar.ship.Ship;
import Iliatar.utils.randomSelector.Priority;
import Iliatar.utils.randomSelector.PriorityItem;
import Iliatar.utils.randomSelector.RandomSelector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AimingModule extends ShipModule {
    public final static int AIM_COMPLETE_PROGRESS = 100;
    private final static int RESIDUAL_AIM_PROGRESS = 50;
    private final static int AIM_PROGRESS_DAMAGE_LOSS = 30;
    private final static int POTENTIAL_TARGETS_INITIAL_COUNT = 10;
    private final static int POTENTIAL_TARGETS_FINAL_COUNT = 3;
    private final static double WEAPON_MASS_COEFFICIENT = 30;
    private final int aimSpeed;
    private int aimProgress;
    private Ship target;
    private BattleManager battleManager;

    public AimingModule(String name, int endurance, int size, int mass, double armor, int aimSpeed) {
        super(name, endurance, size, mass, armor);
        type = ShipModuleType.AimingModule;
        this.aimSpeed = aimSpeed;
    }

    public void calculateTurn(int deltaTime) {
        if (isAimed() || getStatus() != ShipModuleStatus.Active) return;

        if (target == null || !(target.getStatus() == ShipModuleStatus.Active)) {
            selectTarget();
        }

        if (target == null) return;

        aimProgress += getAimingSpeed(target);
    }

    public void takeDamage(int damageAmount) {
        super.takeDamage(damageAmount);
        aimProgress -= damageAmount * AIM_PROGRESS_DAMAGE_LOSS;
        if (aimProgress < 0) aimProgress = 0;
    }

    public void shoot() {
        selectTarget();
    }

    @Override
    public void initiateForBattle(BattleManager battleManager) {
        this.battleManager = battleManager;
        aimProgress = 0;
    }

    @Override
    public void finalizeBattle() {
        battleManager = null;
    }

    private void selectTarget() {
        List<Ship> ships = battleManager.getPotentialTargets(getParentShip(), POTENTIAL_TARGETS_INITIAL_COUNT);
        if (target != null && target.getStatus() == ShipModuleStatus.Active && !ships.contains(target)) {
            ships.add(target);
        }
        List<Priority<Ship>> potentialTargets = new ArrayList<>(ships.size());

        for(Ship ship : ships) {
            int aimSpeed = getAimingSpeed(ship);
            if (aimSpeed >= 1) {
                double initialAimProgress = ship == target ? RESIDUAL_AIM_PROGRESS : 0;
                double priority = (AIM_COMPLETE_PROGRESS - initialAimProgress) / aimSpeed;
                PriorityItem<Ship> potentialTarget = new PriorityItem(ship, priority);
                potentialTargets.add(potentialTarget);
            }
        }

        if (potentialTargets.isEmpty()) {
            BattleLogger.logModuleMessage(this, "potentialTargets is empty");
            return;
        }

        potentialTargets =  potentialTargets.stream()
                .sorted(Comparator.comparingDouble(Priority::getPriority))
                .limit(POTENTIAL_TARGETS_FINAL_COUNT)
                .toList();

        Ship newTargetShip = RandomSelector.selectRandomByPriority(potentialTargets);
        aimProgress = newTargetShip == target ? RESIDUAL_AIM_PROGRESS : 0;
        target = newTargetShip;
        BattleLogger.logModuleMessage(this, "select target " + newTargetShip.getShipType() + " " + newTargetShip.getName() +
                "\nAim progress = " + aimProgress + "; Aim speed = " + getAimingSpeed(newTargetShip));
    }

    public int getAimingSpeed(Ship targetShip) {
        if (targetShip == null) return 0;

        int sizeEffect = (int) Math.log10(targetShip.getSize());
        int massEffect = - (int) (Math.log(getParentModule().getTotalMass()) / 2);
        double maneuverabiltyDiff = Math.max(targetShip.getManeuverability() - getParentShip().getManeuverability(), 1);
        int maneuverabiltyEffect = - (int) Math.log(maneuverabiltyDiff);
        int damageEffect = 0;
        if (damage / endurance > 0.75) {
            damageEffect = -2;
        } else if (damage / endurance > 0.5) {
            damageEffect = -1;
        }

        int aimingSpeed = aimSpeed + sizeEffect + massEffect + maneuverabiltyEffect + damageEffect;
        if (aimingSpeed < 0) aimingSpeed = 0;

        int aimingSpeedLambda = aimingSpeed;
        int damageEffectLambda = damageEffect;

        BattleLogger.logModuleMessage(this, () -> {
            return "Aiming speed parameters:"
                    + "\nbase AimSpeed: " + aimSpeed
                    + "\ntarget: " + targetShip.getShipType() + " " + targetShip.getName()
                    + "\ntarget size: " + targetShip.getSize()
                    + "\nsizeEffect = " + sizeEffect
                    + "\nweapon total mass: " + getParentModule().getTotalMass()
                    + "\nmassEffect = " + massEffect
                    + "\nship maneuverability: " + getParentShip().getManeuverability()
                    + "\ntarget maneuverability: " + targetShip.getManeuverability()
                    + "\nmaneuverabiltyEffect = " + maneuverabiltyEffect
                    + "\nmodule damage: " + damage + "; endurance: " + endurance
                    + "\ndamageEffect = " + damageEffectLambda
                    + "\naimingSpeed = " + aimingSpeedLambda;
        }, 0.01);

        return aimingSpeed;
    }

    public boolean isAimed() {
        return aimProgress >= AIM_COMPLETE_PROGRESS;
    }

    public Ship getTarget() { return target; }
    public int getAimProgress() { return aimProgress; }
    public int getAimingSpeed() { return getAimingSpeed(target); }
}
