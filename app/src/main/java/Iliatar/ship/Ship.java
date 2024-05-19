package Iliatar.ship;

public class Ship {
    public static final int MAX_SHIP_RANK = 3;
    private final int rank;
    private final double mass;

    private double maneuverability;

    public Ship(int rank) {
        this.rank = rank;
    }
    public void getShoot(WeaponModule weaponModule) {

    }

    public void spendAmmo(WeaponModule weaponModule) {

    }

    public boolean isEnoughAmmo (WeaponModule weaponModule) {

    }

    public boolean isActive() {

    }

    public int getRank() {
        return rank;
    }

    public double getMass() { return mass; }

    public double getManeuverability() { return maneuverability; }
}
