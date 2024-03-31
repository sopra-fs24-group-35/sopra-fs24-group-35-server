package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class Player implements Serializable {

    @Column(nullable = false, unique = false)
    private String username;

    @Column(nullable = false, unique = false)
    private Boolean ready;

    @Column(nullable = false, unique = false)
    private RiskCard riskCards;

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
    public RiskCard getRiskCards() {
        return riskCards;
    }

    public void setRiskCards(RiskCard riskCards) {
        this.riskCards = riskCards;
    }
    
}
