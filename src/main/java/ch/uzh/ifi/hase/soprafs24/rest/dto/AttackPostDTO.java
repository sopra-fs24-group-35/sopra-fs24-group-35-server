package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class AttackPostDTO {
    
    private String attackingTerritory;
    private String defendingTerritory;
    private int troopsAmount;
    private int repeats;

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

    // Getter for repeats
    public int getRepeats() {
        return repeats;
    }

    // Setter for repeats
    public void setRepeats(int repeats) {
        this.repeats = repeats;
    }
}
