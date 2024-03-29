package ch.uzh.ifi.hase.soprafs24.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "LOBBY")
public class Lobby implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue
    private Long id;

    @GeneratedValue
    private String code;

    @GeneratedValue
    private List<Long> players = new ArrayList<Long>();

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

    public List<Long> getPlayers(){
        return players;
    }

    public void setPlayers(List<Long> players){
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
