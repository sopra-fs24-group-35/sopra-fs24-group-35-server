package ch.uzh.ifi.hase.soprafs24.entity;


import javax.persistence.*;
import java.io.Serializable;

//Territory entity
@Entity
@Table(name = "TERRITORY")
public class Territory implements Serializable {

    @Id
    @GeneratedValue
    private Long territoryId;
    
    @Column(nullable = true, unique = false)
    private String name;

    @Column(nullable = true, unique = false)
    private Long owner;

    @Column(nullable = true, unique = false)
    private int troops;

    public Long getTerritoryId() {
        return territoryId;
    }

    public void setTerritoryId(Long territoryId) {
        this.territoryId = territoryId;
    }

     // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for owner
    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    // Getter and setter for troops
    public int getTroops() {
        return troops;
    }

    public void setTroops(int troops) {
        this.troops = troops;
    }
}
