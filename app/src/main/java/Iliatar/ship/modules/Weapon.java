package Iliatar.ship.modules;

import Iliatar.battle.BattleLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Weapon extends ShipModule {
    private List<Barrell> barrellList;
    private AimingModule aimingModule;
    private AmmoLoader ammoLoader;

    public Weapon(String name, int endurance, int size, int mass, double armor, AimingModule aimingModule, AmmoLoader ammoLoader, List<Barrell> barrellList) {
        super(name, endurance, size, mass, armor);
        addChild(aimingModule);
        addChild(ammoLoader);
        barrellList.forEach(this::addChild);
        this.barrellList = barrellList;
        this.aimingModule = aimingModule;
        this.ammoLoader = ammoLoader;
        type = ShipModuleType.Weapon;
    }

    public void calculateTurn(int deltaTime) {
        if (getStatus() != ShipModuleStatus.Active) return;

        aimingModule.calculateTurn(deltaTime);
        ammoLoader.calculateTurn(deltaTime);

        if (aimingModule.isAimed() && ammoLoader.isLoaded()) shoot();

        BattleLogger.logModuleMessage(this, () -> {
            String logMessage = "full log \ncurrent target: ";
            if (aimingModule.getTarget() == null) {
                logMessage += "not selected";
            } else {
                logMessage += aimingModule.getTarget().getShipType() + " " + aimingModule.getTarget().getName();
            }
            logMessage += "\naim progress: " + aimingModule.getAimProgress() + " ( +" + aimingModule.getAimingSpeed() + ")"
                    + " / " + aimingModule.AIM_COMPLETE_PROGRESS + "\n"
                    + "load progress: " + ammoLoader.getLoadProgress() + " ( +" + ammoLoader.getLoadSpeed() + ")"
                    + " / " + ammoLoader.LOAD_COMPLETE_PROGRESS;

            for (ShipModule module : getChildModules()) {
                logMessage += "\n" + module.getName() + " Mass: " + module.getTotalMass() + " Size: "  + module.getTotalSize()
                        + " Endurance: " + module.getEndurance() + " Damage: " + module.getDamage() + " Current armor: " + module.getCurrentArmor();
            }
            return logMessage;
        }, 0.01);
    }

    private void shoot() {
        barrellList.forEach(barrell -> barrell.shoot(aimingModule.getTarget()));
        aimingModule.shoot();
        ammoLoader.shoot();
    }

    public ShipModuleStatus getStatus() {
        if (damage >= endurance) {
            return ShipModuleStatus.Destroyed;
        }

        if (ammoLoader.getStatus() == ShipModuleStatus.Destroyed
        || aimingModule.getStatus() == ShipModuleStatus.Destroyed
        || !barrellList.stream().anyMatch(barrell -> barrell.getStatus() == ShipModuleStatus.Active)) {
            return ShipModuleStatus.Damaged;
        }

        return ShipModuleStatus.Active;
    }

    public List<Barrell> getBarrellList() {
        return new ArrayList<>(barrellList);
    }
}
