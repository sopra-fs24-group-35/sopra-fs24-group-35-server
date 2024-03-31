package ch.uzh.ifi.hase.soprafs24.entity;



import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class Territory implements Serializable {
    
    @Column(nullable = false, unique = false)
    private String name;

    @Column(nullable = false, unique = false)
    private String owner;

    @Column(nullable = false, unique = false)
    private int troops;

     // Getter and setter for name
     public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for owner
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
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
