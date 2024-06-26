package ch.uzh.ifi.hase.soprafs24.entity;

import java.util.ArrayList;
import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "LOBBY")
public class Lobby implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = true)
    private ArrayList<Long> players = new ArrayList<Long>();

    @Column(nullable = true, unique = true)
    private Long gameId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getOwnerId(){
        return ownerId;
    }

    public void setOwnerId(Long ownerId){
        this.ownerId = ownerId;
    }

    public ArrayList<Long> getPlayers(){
        return players;
    }

    public void setPlayers(ArrayList<Long> players){
        this.players = players;
    }

    public void addPlayers(Long playerId){
        this.players.add(playerId);
    }

    public void removePlayers(Long playerId){
        this.players.remove(playerId);
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}
