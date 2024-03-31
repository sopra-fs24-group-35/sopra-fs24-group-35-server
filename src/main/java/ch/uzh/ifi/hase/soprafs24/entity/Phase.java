package ch.uzh.ifi.hase.soprafs24.entity;



import javax.persistence.*;
import java.io.Serializable;


@Embeddable
public class Phase implements Serializable{

    @Column(nullable = false, unique = false)
    private String currentPhase;

    // Getter and setter for currentPhase
    public String getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(String currentPhase) {
        this.currentPhase = currentPhase;
    }
    
}
