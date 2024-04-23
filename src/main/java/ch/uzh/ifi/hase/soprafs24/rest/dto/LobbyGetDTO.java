package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class LobbyGetDTO {

    private Long id;
    private String code;
    private Long ownerId;
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

    public Long getOwnerId(){
        return ownerId;
    }

    public void setOwnerId(Long ownerId){
        this.ownerId = ownerId;
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
