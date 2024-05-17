package battle;

import ship.Ship;

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
        if (fleet1.getShips().contains(sourceShip)) {
            return overlapMatrix[sourceShip.getRank() - 1][targetShip.getRank() - 1];
        }

        return overlapMatrix[targetShip.getRank() - 1][sourceShip.getRank() - 1];
    }

    public void refreshOverlap() {
        Arrays.fill(overlapMatrix, 0);
        var fleet1ShipRankVector = getShipRankVector(fleet1);
        var fleet2ShipRankVector = getShipRankVector(fleet2);

        for (int i = 0; i < overlapMatrix.length; i++) {
            for (int j = 0; j < overlapMatrix[i].length; j++) {
                if (i == 0 && j == 0) {
                    overlapMatrix[i][j] = (double) fleet1ShipRankVector[i] / 2 + (double) fleet2ShipRankVector[j] / 2;
                } else if (j == 0) {
                    overlapMatrix[i][j] = overlapMatrix[i - 1][j] + (double)(fleet1ShipRankVector[i] + fleet1ShipRankVector[i - 1]) / 2;
                } else {
                    overlapMatrix[i][j] = overlapMatrix[i][j-1] + (double)(fleet2ShipRankVector[j] + fleet2ShipRankVector[j - 1]) / 2;
                }
            }
        }

        for (int i = 0; i < overlapMatrix.length; i++) {
            for (int j = 0; j < overlapMatrix[i].length; j++) {
                overlapMatrix[i][j] = Math.sqrt(overlapMatrix[i][j]) / 100;
            }
        }
    }

    private int[] getShipRankVector(Fleet fleet) {
        var shipRankVector = new int[Ship.MAX_SHIP_RANK];
        for (Ship ship : fleet.getShips()) {
            int shipRank = ship.getRank();
            shipRankVector[shipRank - 1] += shipRank;
        }
        return shipRankVector;
    }
}