package ch.uzh.ifi.hase.soprafs24.entity;

public class Attack {
    private String attackingTerritory;
    private String defendingTerritory;
    private int troopsAmount;

    // Getter for attackingTerritory
    public String getAttackingTerritory() {
        return attackingTerritory;
    }

    // Setter for attackingTerritory
    public void setAttackingTerritory(String attackingTerritory) {
        this.attackingTerritory = attackingTerritory;
    }

    // Getter for defendingTerritory
    public String getDefendingTerritory() {
        return defendingTerritory;
    }

    // Setter for defendingTerritory
    public void setDefendingTerritory(String defendingTerritory) {
        this.defendingTerritory = defendingTerritory;
    }

    // Getter for troopsAmount
    public int getTroopsAmount() {
        return troopsAmount;
    }

    // Setter for troopsAmount
    public void setTroopsAmount(int troopsAmount) {
        this.troopsAmount = troopsAmount;
    }
}
