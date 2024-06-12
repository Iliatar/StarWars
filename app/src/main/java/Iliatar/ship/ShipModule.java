package Iliatar.ship;

import java.util.ArrayList;
import java.util.List;

public abstract class ShipModule {
    public enum ShipModuleType {Weapon, Barrel, AimingModule, ReloadModule, ShipHull}
    public enum ShipModuleStatus {Active, Damaged, Destroyed}
    private final ShipModuleType type;
    private ShipModule parentModule;
    private final int endurance;
    private int damage;
    private int size;
    private int mass;
    List<ShipModule> childModules;

    public ShipModule(ShipModuleType type, int endurance, int size, int mass) {
        this.type = type;
        this.endurance = endurance;
        this.size = size;
        this.mass = mass;
        this.childModules = new ArrayList<ShipModule>();
        this.damage = 0;
    }

    public void calculateTurn() {
        childModules.forEach(ShipModule::calculateTurn);
    }

    public void getDamage(int damageAmount) {
        damage += damageAmount;
    }

    public ShipModuleStatus getStatus() {
        if (damage >= endurance) {
            return ShipModuleStatus.Destroyed;
        }
        return ShipModuleStatus.Active;
    }

    public void addChild(ShipModule childModule) {
        childModules.add(childModule);
        childModule.SetParentModule(this);
    }

    public void SetParentModule(ShipModule parentModule) {
        this.parentModule = parentModule;
    }

    public ShipModuleType getType() {
        return type;
    }

    public ShipModule getParentModule() {
        return parentModule;
    }

    public int getEndurance() {
        return endurance;
    }

    public int getDamage() {
        return damage;
    }

    public int getSize() {
        return size;
    }

    public int getTotalSize() {
        int result = size;
        for (ShipModule child : childModules) {
            result += child.getTotalSize();
        }
        return result;
    }

    public int getMass() {
        return mass;
    }

    public int getTotalMass() {
        int result = mass;
        for (ShipModule child : childModules) {
            result += child.getTotalMass();
        }
        return result;
    }

    public List<ShipModule> getChildModules() {
        return new ArrayList<>(childModules);
    }
}
