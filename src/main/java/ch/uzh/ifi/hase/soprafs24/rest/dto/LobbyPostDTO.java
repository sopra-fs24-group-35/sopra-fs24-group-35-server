package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class LobbyPostDTO {
    
    private List<Long> players = new ArrayList<Long>();

    public List<Long> getPlayers(){
        return players;
    }

    public void setPlayers(List<Long> players){
        this.players = players;
    }
}
