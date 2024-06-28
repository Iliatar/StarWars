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

        if (new Random().nextInt(50) == 1) {
            logFullStatus();
        }
    }

    private void shoot() {
        barrellList.forEach(barrell -> barrell.shoot(aimingModule.getTarget()));
        aimingModule.shoot();
        ammoLoader.shoot();
    }

    private void logFullStatus() {
        String logMessage = "full log \ncurrent target: " + aimingModule.getTarget().getShipType() + " " + aimingModule.getTarget().getName() + "\n"
                + "aim progress: " + aimingModule.getAimProgress() + " ( +" + aimingModule.getAimingSpeed() + ")"
                + " / " + aimingModule.AIM_COMPLETE_PROGRESS + "\n"
                + "load progress: " + ammoLoader.getLoadProgress() + " ( +" + ammoLoader.getLoadSpeed() + ")"
                + " / " + ammoLoader.LOAD_COMPLETE_PROGRESS;
        for (ShipModule module : getChildModules()) {
            logMessage += "\n" + module.getName() + " Mass: " + module.getTotalMass() + " Size: "  + module.getTotalSize()
                    + " Endurance: " + module.getEndurance() + " Damage: " + module.getDamage() + " Current armor: " + module.getCurrentArmor();
        }
        BattleLogger.logModuleMessage(this, logMessage);
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
