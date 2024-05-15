package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "PLAYER")
public class Player implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = true, unique = false)
    private Long playerId;

    @Column(nullable = true, unique = false)
    private String username;

    @Column(nullable = true, unique = false)
    private Boolean ready;

    @Column(nullable = true, unique = false)
    private int troopBonus;

    @Column(nullable = true, unique = false)
    private int cardBonus;

    @Column(nullable = true, unique = false)
    private Boolean awaitsCard;

    @Column(nullable = true)
    private int avatarId;

    @OneToMany(targetEntity=RiskCard.class, cascade=CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<RiskCard> riskCards = new ArrayList<RiskCard>();

    //getter and setter for id
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

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

    // Getter method for cardBonus
    public int getCardBonus() {
        return cardBonus;
    }

    // Setter method for cardBonus
    public void setCardBonus(int cardBonus) {
        this.cardBonus = cardBonus;
    }

    // Getter for awaitsCard
    public Boolean getAwaitsCard() {
        return awaitsCard;
    }

    // Setter for awaitsCard
    public void setAwaitsCard(Boolean awaitsCard) {
        this.awaitsCard = awaitsCard;
    }

    public int getAvatarId() {
        return this.avatarId;
    }
    
    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }
    
}
