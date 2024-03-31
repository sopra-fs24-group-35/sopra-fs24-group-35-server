package ch.uzh.ifi.hase.soprafs24.entity;



import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Embeddable
public class Continent implements Serializable {

    @Column(nullable = false, unique = false)
    private ArrayList<Territory> territories;

    @Column(nullable = false, unique = false)
    private String name;

    @Column(nullable = false, unique = false)
    private int additionalTroopBonus;

    // Getters and setters for territories
    public ArrayList<Territory> getTerritories() {
        return territories;
    }

    public void setTerritories(ArrayList<Territory> territories) {
        this.territories = territories;
    }

    // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for additionalTroopBonus
    public int getAdditionalTroopBonus() {
        return additionalTroopBonus;
    }

    public void setAdditionalTroopBonus(int additionalTroopBonus) {
        this.additionalTroopBonus = additionalTroopBonus;
    }
    
}
