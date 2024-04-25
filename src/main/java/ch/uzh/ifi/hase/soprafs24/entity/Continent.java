package ch.uzh.ifi.hase.soprafs24.entity;



import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;


@Entity
@Table(name = "CONTINENT")
public class Continent implements Serializable {

    @Id
    @GeneratedValue
    private Long continentId;

    @OneToMany(targetEntity=Territory.class,cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Territory> territories = new ArrayList<Territory>();

    @Column(nullable = true, unique = false)
    private String name;

    @Column(nullable = true, unique = false)
    private int additionalTroopBonus;

    // Getters and setters for territories
    public List<Territory> getTerritories() {
        return territories;
    }

    public void setTerritories(List<Territory> territories) {
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
