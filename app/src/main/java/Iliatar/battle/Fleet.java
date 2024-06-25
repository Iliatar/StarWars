package Iliatar.battle;

import Iliatar.ship.Ship;
import Iliatar.ship.modules.ShipModule;

import java.util.ArrayList;
import java.util.List;

public class Fleet {
    private List<Ship> ships;
    private String name;

    public Fleet(String name, List<Ship> ships) {
        this.name = name;
        this.ships = ships;
    }
    public Fleet() {ships = new ArrayList<>();}

    public void addShip(Ship ship) {
        ships.add(ship);
        ship.setFleet(this);
    }
    public void addShips(List<Ship> ships) {
        ships.forEach(this::addShip);
    }
    public void removeShip(Ship ship) {
        if (ship == null || !ships.contains(ship)) {
            return;
        }

        ships.remove(ship);
        ship.setFleet(null);
    }

    public List<Ship> getShips() {
        return ships;
    }
    public List<Ship> getActiveShips() {
        return ships.stream().filter(ship -> ship.getStatus() == ShipModule.ShipModuleStatus.Active).toList();
    }
    public String getName() {return name;}

    public void initiateBattle(BattleManager battleManager) {
        ships.forEach(ship -> ship.initiateForBattle(battleManager));
    }

    public void finalizeBattle() {
        ships.forEach(ship -> ship.finalizeBattle());
        ships.stream().filter(ship -> ship.getStatus() == ShipModule.ShipModuleStatus.Destroyed).toList().forEach(this::removeShip); //удаляем уничтоженные корабли
    }
}
