package Iliatar.battle;

import Iliatar.ship.Ship;

import java.util.Arrays;

public class BattleOverlap {
    Fleet fleet1;
    Fleet fleet2;
    double[][] overlapMatrix;

    public BattleOverlap(Fleet fleet1, Fleet fleet2) {
        this.fleet1 = fleet1;
        this.fleet2 = fleet2;
        overlapMatrix = new double[Ship.MAX_SHIP_RANK][Ship.MAX_SHIP_RANK];
        refreshOverlap();
    }

    public double getOverlap(Ship sourceShip, Ship targetShip) {
        if (fleet1.getShips().contains(sourceShip) && fleet2.getShips().contains(targetShip)) {
            return overlapMatrix[sourceShip.getRank() - 1][targetShip.getRank() - 1];
        } else if (fleet2.getShips().contains(sourceShip) && fleet1.getShips().contains(targetShip)) {
            return overlapMatrix[targetShip.getRank() - 1][sourceShip.getRank() - 1];
        } else {
            throw new IllegalArgumentException("sourceShip and targetShip are from one fleet or unknown");
        }

    }

    public void refreshOverlap() {
        var shipRankMatrix = new double[Ship.MAX_SHIP_RANK][Ship.MAX_SHIP_RANK];
        var fleet1ShipRankVector = getShipRankVector(fleet1);
        var fleet2ShipRankVector = getShipRankVector(fleet2);

        for (int i = 0; i < overlapMatrix.length; i++) {
            for (int j = 0; j < overlapMatrix[i].length; j++) {
                if (i == 0 && j == 0) {
                    shipRankMatrix[i][j] = fleet1ShipRankVector[i] / 2 + fleet2ShipRankVector[j] / 2;
                } else if (j == 0) {
                    shipRankMatrix[i][j] = shipRankMatrix[i - 1][j] + (fleet1ShipRankVector[i] + fleet1ShipRankVector[i - 1]) / 2;
                } else {
                    shipRankMatrix[i][j] = shipRankMatrix[i][j-1] + (fleet2ShipRankVector[j] + fleet2ShipRankVector[j - 1]) / 2;
                }
                overlapMatrix[i][j] = Math.sqrt(shipRankMatrix[i][j]) / 100;
                //System.out.println("shipRankMatrix[" + i + "][" + j + "] = " + shipRankMatrix[i][j]);
            }
        }
    }

    private double[] getShipRankVector(Fleet fleet) {
        var shipRankVector = new double[Ship.MAX_SHIP_RANK];
        for (Ship ship : fleet.getShips()) {
            int shipRank = ship.getRank();
            shipRankVector[shipRank - 1] += shipRank;
        }
        return shipRankVector;
    }
}