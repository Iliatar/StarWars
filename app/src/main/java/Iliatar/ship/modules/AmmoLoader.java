package Iliatar.ship.modules;

import Iliatar.battle.BattleManager;

public class AmmoLoader extends  ShipModule {
    private final static int LOAD_COMPLETE_PROGRESS = 100;
    private final int loadSpeed;
    private int loadProgress;

    public AmmoLoader(int endurance, int size, int mass, int reloadSpeed) {
        super(endurance, size, mass);
        type = ShipModuleType.AmmoLoader;
        this.loadSpeed = reloadSpeed;
        loadProgress = 0;
    }

    public void calculateTurn(int deltaTime) {
        if (isLoaded()) return;

        loadProgress += loadSpeed * deltaTime;
        if (isLoaded()) {
            //TODO здесь должно быть списание зарядов из запаса корабля
        }
    }

    @Override
    public void initiateForBattle(BattleManager battleManager) {
        loadProgress = 0;
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
        loadProgress = 0;
    }
}
