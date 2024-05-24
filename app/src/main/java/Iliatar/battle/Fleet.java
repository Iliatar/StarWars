package Iliatar.battle;

import Iliatar.ship.Ship;

import java.util.ArrayList;
import java.util.List;

public class Fleet {
    private List<Ship> ships;

    public Fleet(List<Ship> ships) {
        this.ships = ships;
    }
    public Fleet() {ships = new ArrayList<>();}

    public void addShip(Ship ship) {
        ships.add(ship);
    }
    public void removeShip(Ship ship) {
        if (ship == null || !ships.contains(ship)) {
            return;
        }

        ships.remove(ship);
    }

    public List<Ship> getShips() {
        return ships;
    }
    public List<Ship> getActiveShips() {
        return ships.stream().filter(Ship::isActive).toList();
    }

    public void initiateBattle() {

    }

    public void finalizeBattle() {
        ships = ships.stream().filter(ship -> !ship.isDestroyed()).toList(); //удаляем уничтоженные корабли
    }
}
