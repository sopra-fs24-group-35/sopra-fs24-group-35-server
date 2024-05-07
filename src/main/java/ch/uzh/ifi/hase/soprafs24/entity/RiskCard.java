package ch.uzh.ifi.hase.soprafs24.entity;



import javax.persistence.*;
import java.io.Serializable;

// Risk Card

@Entity
@Table(name = "RISKCARD")
public class RiskCard implements Serializable {

    @Id
    @GeneratedValue
    private Long cardId;

    @Column(nullable = false, unique = false)
    private String territoryName;

    @Column(nullable = true, unique = false)
    private int troops;

    @Column(nullable = true, unique = false)
    private boolean inStack;

    // Getter and setter for territoryName
    public String getTerritoryName() {
        return territoryName;
    }

    public void setTerritoryName(String territoryName) {
        this.territoryName = territoryName;
    }

    // Getter and setter for troops
    public int getTroops() {
        return troops;
    }

    public void setTroops(int troops) {
        this.troops = troops;
    }

    // Getter method
    public boolean isInStack() {
        return inStack;
    }

    // Setter method
    public void setInStack(boolean inStack) {
        this.inStack = inStack;
    }
    
}
