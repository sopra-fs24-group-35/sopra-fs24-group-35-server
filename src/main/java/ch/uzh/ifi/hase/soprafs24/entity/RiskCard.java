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
    private boolean handedOut;

    @Column(nullable = true, unique = false)
    private boolean isNew;

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
    public boolean isHandedOut() {
        return handedOut;
    }

    // Setter method
    public void setHandedOut(boolean handedOut) {
        this.handedOut = handedOut;
    }

    // Getter for isNew
    public boolean isNew() {
        return isNew;
    }

    // Setter for isNew
    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }
    
}
