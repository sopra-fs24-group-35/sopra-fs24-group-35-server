package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "PLAYER")
public class Player implements Serializable {

    @Id
    @GeneratedValue
    private Long playerId;

    @Column(nullable = true, unique = false)
    private String username;

    @Column(nullable = true, unique = false)
    private Boolean ready;

    @OneToMany(targetEntity=RiskCard.class,cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<RiskCard> riskCards;

    // Getter and setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and setter for ready
    public Boolean getReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    // Getter and setter for riskCards
    public List<RiskCard> getRiskCards() {
        return riskCards;
    }

    public void setRiskCards(List<RiskCard> riskCards) {
        this.riskCards = riskCards;
    }
    
}