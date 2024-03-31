package ch.uzh.ifi.hase.soprafs24.entity;



import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
@Table(name = "GAME")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long gameId;

    @Column(nullable = false, unique = false)
    private Board board;

    @Column(nullable = false, unique = false)
    private ArrayList<Player> players;

    @Column(nullable = false, unique = false)
    private TurnCycle turnCycle;

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
    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    // Getter and setter for turnCycle
    public TurnCycle getTurnCycle() {
        return turnCycle;
    }

    public void setTurnCycle(TurnCycle turnCycle) {
        this.turnCycle = turnCycle;
    }

    
}
