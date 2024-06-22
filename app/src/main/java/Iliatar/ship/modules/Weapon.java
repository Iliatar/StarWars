package Iliatar.ship.modules;

import java.util.ArrayList;
import java.util.List;

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

    public List<Barrell> getBarrellList() {
        return new ArrayList<>(barrellList);
    }
}
