package ch.uzh.ifi.hase.soprafs24.entity;



import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "PHASE")
public class Phase implements Serializable{

    @Id
    @GeneratedValue
    private Long phaseId;

    @Column(nullable = true, unique = false)
    private String currentPhase;

    // Getter and setter for currentPhase
    public String getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(String currentPhase) {
        this.currentPhase = currentPhase;
    }
    
}
