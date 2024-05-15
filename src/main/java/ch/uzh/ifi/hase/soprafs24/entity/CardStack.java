package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "CARDSTACK")
public class CardStack {

    @Id
    @GeneratedValue
    private Long cardStackId;
    
    @OneToMany(targetEntity=RiskCard.class,cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<RiskCard> riskCards = new ArrayList<RiskCard>();

    // Getter method
    public List<RiskCard> getRiskCards() {
        return riskCards;
    }

    // Setter method
    public void setRiskCards(List<RiskCard> riskCards) {
        this.riskCards = riskCards;
    }


}
