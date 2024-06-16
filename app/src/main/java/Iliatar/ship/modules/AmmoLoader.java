package Iliatar.ship.modules;

import Iliatar.battle.BattleManager;
import Iliatar.ship.StorageModule;
import Iliatar.ship.WeaponModule;

import java.util.List;

public class AmmoLoader extends  ShipModule {
    private final static int LOAD_COMPLETE_PROGRESS = 100;
    private final int loadSpeed;
    private int loadProgress;
    private boolean isAmmoGet;

    public AmmoLoader(int endurance, int size, int mass, double armor, int reloadSpeed) {
        super(endurance, size, mass, armor);
        type = ShipModuleType.AmmoLoader;
        this.loadSpeed = reloadSpeed;
        loadProgress = 0;
        isAmmoGet = false;
    }

    public void calculateTurn(int deltaTime) {
        if (isLoaded()) return;
        if (!isAmmoGet) getAmmo();
        if (!isAmmoGet) return;

        loadProgress += loadSpeed * deltaTime;
    }

    private void getAmmo() {
        StorageModule ammoModule = getParentShip().getAmmoModule();
        if (ammoModule.spend(getAmmoAmount())) {
            isAmmoGet = true;
        }
    }

    @Override
    public void initiateForBattle(BattleManager battleManager) {
        clearAmmo();
    }

    @Override
    public void finalizeBattle() {
        if (isAmmoGet) {
            StorageModule ammoModule = getParentShip().getAmmoModule();
            ammoModule.load(getAmmoAmount());
        }

        clearAmmo();
    }

    public int getLoadSpeed() {
        return loadSpeed;
    }
    public int getLoadProgress() {
        return loadProgress;
    }
    public boolean isLoaded() {
        return loadProgress >= LOAD_COMPLETE_PROGRESS;
    }

    public void shoot() {
        clearAmmo();
    }

    private void clearAmmo() {
        loadProgress = 0;
        isAmmoGet = false;
    }

    private int getAmmoAmount() {
        List<Barrell> barrellList = ((Weapon) getParentModule()).getBarrellList();
        int result = 0;
        for (Barrell barrell : barrellList) {
            if (barrell.getStatus() == ShipModuleStatus.Active) {
                result += barrell.getCaliber();
            }
        }
        
        return result;
    }
}
