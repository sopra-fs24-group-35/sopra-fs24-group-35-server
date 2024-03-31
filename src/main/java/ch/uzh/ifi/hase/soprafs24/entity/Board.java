package ch.uzh.ifi.hase.soprafs24.entity;



import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Embeddable
public class Board implements Serializable {

    @Column(nullable = false, unique = false)
    private ArrayList<Continent> continents;

    @Column(nullable = false, unique = false)
    private ArrayList<Territory> territories;

    // Getter and setter for continents
    public ArrayList<Continent> getContinents() {
        return continents;
    }

    public void setContinents(ArrayList<Continent> continents) {
        this.continents = continents;
    }

    // Getter and setter for territories
    public ArrayList<Territory> getTerritories() {
        return territories;
    }

    public void setTerritories(ArrayList<Territory> territories) {
        this.territories = territories;
    }
}
