package ship;

import java.util.List;

public class WeaponModule {
    private final static double COMPLETE_AIM_PROGRESS = 100;
    private final static double BASE_AIM_SPEED = 10;
    private final int barrelCount;
    private final int barrelCaliber;
    private final int endurance;
    private final Ship parentShip;
    private int damage;
    private Ship target;
    private double aimProgress;

    public void selectTarget(List<Ship> ships, OverlapData overlapData) {

    }

    public boolean isActive(){
        return endurance > damage;
    }

    public void getDamage(WeaponModule weaponModule) {

    }

    public void processAiming(double deltaTime, OverlapData overlapData) {

    }

    private void shoot() {

    }
}
