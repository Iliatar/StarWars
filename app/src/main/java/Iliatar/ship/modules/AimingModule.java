package Iliatar.ship.modules;

import Iliatar.battle.BattleManager;
import Iliatar.ship.Ship;
import Iliatar.utils.randomSelector.Priority;
import Iliatar.utils.randomSelector.PriorityItem;
import Iliatar.utils.randomSelector.RandomSelector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AimingModule extends ShipModule {
    private final static int AIM_COMPLETE_PROGRESS = 100;
    private final static int RESIDUAL_AIM_PROGRESS = 50;
    private final static int AIM_PROGRESS_DAMAGE_LOSS = 30;
    private final static int POTENTIAL_TARGETS_INITIAL_COUNT = 10;
    private final static int POTENTIAL_TARGETS_FINAL_COUNT = 3;
    private final static double WEAPON_MASS_COEFFICIENT = 30;
    private final int aimSpeed;
    private int aimProgress;
    private Ship target;
    private BattleManager battleManager;

    public AimingModule(int endurance, int size, int mass, double armor, int aimSpeed) {
        super(endurance, size, mass, armor);
        type = ShipModuleType.AimingModule;
        this.aimSpeed = aimSpeed;
    }

    public void calculateTurn(int deltaTime) {
        if (isAimed() || getStatus() != ShipModuleStatus.Active) return;

        if (target == null || !(target.getStatus() == ShipModuleStatus.Active)) {
            selectTarget();
        }

        aimProgress += getAimingSpeed(target);
    }

    public void getDamage(int damageAmount) {
        super.getDamage(damageAmount);
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
            double aimEfficiency = getAimingSpeed(ship);
            double initialAimProgress = ship == target ? RESIDUAL_AIM_PROGRESS : 0;
            double priority = (AIM_COMPLETE_PROGRESS - initialAimProgress) / aimEfficiency;
            PriorityItem<Ship> potentialTarget = new PriorityItem(ship, priority);
            potentialTargets.add(potentialTarget);
        }

        if (potentialTargets.isEmpty()) return;

        potentialTargets =  potentialTargets.stream()
                .sorted(Comparator.comparingDouble(Priority::getPriority))
                .limit(POTENTIAL_TARGETS_FINAL_COUNT)
                .toList();

        Ship newTargetShip = RandomSelector.selectRandomByPriority(potentialTargets);
        aimProgress = newTargetShip == target ? RESIDUAL_AIM_PROGRESS : 0;
    }

    public int getAimingSpeed(Ship targetShip) {
        double maneuverabilityCoeff = Math.min(1, getParentShip().getManeuverability() / targetShip.getManeuverability());
        double actualBaseAimSpeed = aimSpeed * (1 - damage / endurance);
        double sizeAndMassCoeff = Math.sqrt(targetShip.getSize() / (getParentModule().getTotalMass() * WEAPON_MASS_COEFFICIENT));
        int aimingSpeed = (int)(actualBaseAimSpeed * maneuverabilityCoeff * sizeAndMassCoeff);
        return aimingSpeed;
    }

    public boolean isAimed() {
        return aimProgress >= AIM_COMPLETE_PROGRESS;
    }

    public Ship getTarget() { return target; }
}
