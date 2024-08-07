package Iliatar.battle;

import Iliatar.ship.Ship;
import Iliatar.ship.modules.ShipModule;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.Random;
import java.util.function.Supplier;

//TODO убрать статику
public class BattleLogger {
    static BattleManager battleManager;
    private static final Logger logger = LogManager.getLogger(BattleLogger.class);
    private static Random random = new Random();

    public static void initiate(BattleManager battleManager) {
        BattleLogger.battleManager = battleManager;
    }
    public static void logMessage(String message) {
        logger.info(battleManager.getTurnNumber() + ":  " + message);
    }

    public static void logShipMessage(Ship ship, String message) {
        logMessage(ship.getShipType() + " " + ship.getName() + " of " + ship.getFleet().getName() + " " + message);
    }

    public static void logModuleMessage(ShipModule module, String message) {
        logMessage(module.getName() + " of " + module.getParentShip().getShipType() + " "
                + module.getParentShip().getName() + " of " +  module.getParentShip().getFleet().getName() + " " + message);
    }

    public static void logModuleMessage(ShipModule module, Supplier<String> messageSupplier, double probality) {
        if (random.nextDouble() < probality) {
            logModuleMessage(module, messageSupplier.get());
        }
    }
}
