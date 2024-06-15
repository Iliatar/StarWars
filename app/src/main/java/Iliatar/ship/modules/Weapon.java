package Iliatar.ship.modules;

import java.util.List;

public class Weapon extends ShipModule {
    private List<Barrell> barrellList;
    private AimingModule aimingModule;
    private AmmoLoader ammoLoader;

    public Weapon(int endurance, int size, int mass, AimingModule aimingModule, AmmoLoader ammoLoader, List<Barrell> barrellList) {
        super(endurance, size, mass);
        addChild(aimingModule);
        addChild(ammoLoader);
        barrellList.forEach(this::addChild);
    }

    public void calculateTurn(int deltaTime) {
        if (getStatus() != ShipModuleStatus.Active) return;

        aimingModule.calculateTurn(deltaTime);
        ammoLoader.calculateTurn(deltaTime);

        if (aimingModule.isAimed() && ammoLoader.isLoaded()) shoot();
    }

    private void shoot() {
        aimingModule.shoot();
        ammoLoader.shoot();
        barrellList.forEach(barrell -> barrell.shoot(aimingModule.getTarget()));
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
}
