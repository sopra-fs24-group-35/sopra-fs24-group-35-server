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
    
}
