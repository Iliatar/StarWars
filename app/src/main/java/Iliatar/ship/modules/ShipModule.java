package Iliatar.ship.modules;

import Iliatar.battle.BattleManager;
import Iliatar.ship.Ship;

import java.util.ArrayList;
import java.util.List;

public abstract class ShipModule {
    public enum ShipModuleType {Weapon, Barrel, AimingModule, AmmoLoader, ShipHull}
    public enum ShipModuleStatus {Active, Damaged, Destroyed}
    protected ShipModuleType type;
    private ShipModule parentModule;
    protected final int endurance;
    protected int damage;
    private int size;
    private int mass;
    private List<ShipModule> childModules;

    public ShipModule(int endurance, int size, int mass) {
        this.endurance = endurance;
        this.size = size;
        this.mass = mass;
        this.childModules = new ArrayList<ShipModule>();
        this.damage = 0;
    }

    public void calculateTurn(int deltaTime) {
        childModules.forEach(child -> child.calculateTurn(deltaTime));
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
        childModule.setParentModule(this);
    }

    public void addChild(List<ShipModule> childModules) {
        childModules.forEach(this::addChild);
    }

    public void setParentModule(ShipModule parentModule) {
        this.parentModule = parentModule;
    }

    public void initiateForBattle(BattleManager battleManager) {
        for (ShipModule child : childModules) {
            initiateForBattle(battleManager);
        }
    }

    public void finalizeBattle() {
        for (ShipModule child : childModules) {
            finalizeBattle();
        }
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

    public Ship getParentShip() { return parentModule.getParentShip(); }
}
