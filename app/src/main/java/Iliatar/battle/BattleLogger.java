package Iliatar.battle;

//TODO убрать статику
public class BattleLogger {
    static BattleManager battleManager;

    public static void initiate(BattleManager battleManager) {
        BattleLogger.battleManager = battleManager;
    }
    public static void logMessage(String message) {
        System.out.println(battleManager.getTurnNumber() + ": " + message);
    }
}
