package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Embeddable
public class TurnCycle implements Serializable {

    @Column(nullable = false, unique = false)
    private Player currentPlayer;

    @Column(nullable = false, unique = false)
    private ArrayList<Player> playerCycle;

    @Column(nullable = false, unique = false)
    private Phase currentPhase;

    @Column(nullable = false, unique = false)
    private int timeLeftForCycle;

    // Getter and setter for currentPlayer
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    // Getter and setter for playerCycle
    public ArrayList<Player> getPlayerCycle() {
        return playerCycle;
    }

    public void setPlayerCycle(ArrayList<Player> playerCycle) {
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
