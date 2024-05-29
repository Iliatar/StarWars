package Iliatar.ship;

public class StorageModule {
    private int amount;
    private final int limit;

    public StorageModule(int ammoLimit) {
        this.limit = ammoLimit;
        this.amount = ammoLimit;
    }

    public boolean spend(int spendedAmount) {
        if (isEnoughAmount(spendedAmount)) {
            amount -= spendedAmount;
            return true;
        }
        return false;
    }

    public void load(int amount) {
        this.amount = Math.min(this.amount + amount, limit);
    }

    public boolean isEnoughAmount (int checkedAmount) {
        return amount >= checkedAmount;
    }
    public int getAmount() { return amount; }
}
