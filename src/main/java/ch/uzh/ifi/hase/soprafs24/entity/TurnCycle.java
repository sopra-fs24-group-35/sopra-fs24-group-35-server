package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;

import ch.uzh.ifi.hase.soprafs24.constant.Phase;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "TURNCYCLE")
public class TurnCycle implements Serializable {

    @Id
    @GeneratedValue
    private Long turnCycleId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "playerId")
    private Player currentPlayer;

    @OneToMany(targetEntity=Player.class,cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Player> playerCycle;

    @Column(nullable = true, unique = false)
    private Phase currentPhase;

    @Column(nullable = true, unique = false)
    private int timeLeftForCycle;

    // Getter and setter for currentPlayer
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    // Getter and setter for playerCycle
    public List<Player> getPlayerCycle() {
        return playerCycle;
    }

    public void setPlayerCycle(List<Player> playerCycle) {
        this.playerCycle = playerCycle;
    }

    // Getter and setter for currentPhase
    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(Phase currentPhase) {
        this.currentPhase = currentPhase;
    }

    // Getter and setter for timeLeftForCycle
    public int getTimeLeftForCycle() {
        return timeLeftForCycle;
    }

    public void setTimeLeftForCycle(int timeLeftForCycle) {
        this.timeLeftForCycle = timeLeftForCycle;
    }
    
}
