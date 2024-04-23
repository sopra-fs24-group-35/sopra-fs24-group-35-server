package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "PLAYER")
public class Player implements Serializable {

    @Id
    //@GeneratedValue
    private Long playerId;

    @Column(nullable = true, unique = false)
    private String username;

    @Column(nullable = true, unique = false)
    private Boolean ready;

    @Column(nullable = true, unique = false)
    private int troopBonus;

    @OneToMany(targetEntity=RiskCard.class,cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<RiskCard> riskCards = new ArrayList<RiskCard>();

    //Getter and setter for playerId
    public Long getPlayerId(){
        return playerId;
    }

    public void setPlayerId(Long playerId){
        this.playerId = playerId;
    }

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

    // Getter for troopBonus
    public int getTroopBonus() {
        return troopBonus;
    }

    // Setter for troopBonus
    public void setTroopBonus(int troopBonus) {
        this.troopBonus = troopBonus;
    }
    
}
