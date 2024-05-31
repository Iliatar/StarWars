package Iliatar.battle;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

//TODO убрать статику
public class BattleLogger {
    static BattleManager battleManager;
    private static final Logger logger = LogManager.getLogger(BattleLogger.class);

    public static void initiate(BattleManager battleManager) {
        BattleLogger.battleManager = battleManager;
    }
    public static void logMessage(String message) {
        logger.info(battleManager.getTurnNumber() + ":  " + message);
    }
}
