package ch.uzh.ifi.hase.soprafs24.entity;



import javax.persistence.*;

import org.dom4j.Branch;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "GAME")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long gameId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "boardId")
    private Board board;

    @OneToMany(targetEntity=Player.class,cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Player> players = new ArrayList<Player>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "turnCycleId")
    private TurnCycle turnCycle;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cardStackId")
    private CardStack cardStack;

    @Column(nullable = true, unique = false)
    private String diceResult;

    // Getter and setter for gameId
    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    // Getter and setter for board
    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    // Getter and setter for players
    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void addPlayers(Player player) {
        this.players.add(player);
    }

    // Getter and setter for turnCycle
    public TurnCycle getTurnCycle() {
        return turnCycle;
    }

    public void setTurnCycle(TurnCycle turnCycle) {
        this.turnCycle = turnCycle;
    }

    public String getDiceResult() {
        return diceResult;
    }

    public void setDiceResult(String diceResult) {
        this.diceResult = diceResult;
    }

    // Getter method for cardStack
    public CardStack getCardStack() {
        return cardStack;
    }

    // Setter method for cardStack
    public void setCardStack(CardStack cardStack) {
        this.cardStack = cardStack;
    }

    
}
