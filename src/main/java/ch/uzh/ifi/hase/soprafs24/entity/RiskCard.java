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

    @Column(nullable = true, unique = false)
    private String type;

    // Getter and setter for type
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
