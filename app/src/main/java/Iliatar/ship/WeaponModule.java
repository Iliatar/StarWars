package Iliatar.ship;

import Iliatar.battle.BattleOverlap;

import java.util.List;

public class WeaponModule {
    private final static double COMPLETE_AIM_PROGRESS = 100;
    private final static double BASE_AIM_SPEED = 10;
    private final int barrelCount = 0;
    private final int barrelCaliber = 0;
    private final int endurance = 0;
    private final Ship parentShip = null;
    private int damage;
    private Ship target;
    private double aimProgress;

    public void selectTarget(List<Ship> ships, BattleOverlap overlapData) {

    }

    public boolean isActive(){
        return endurance > damage;
    }

    public void getDamage(WeaponModule weaponModule) {

    }

    public void processAiming(double deltaTime, BattleOverlap overlapData) {

    }

    private void shoot() {

    }
}
