package Iliatar.ship;

public class Ship {
    public static final int MAX_SHIP_RANK = 3;
    private final int rank;

    public Ship(int rank) {
        this.rank = rank;
    }
    public void getShoot(WeaponModule weaponModule) {

    }

    public int getRank() {
        return rank;
    }
}
