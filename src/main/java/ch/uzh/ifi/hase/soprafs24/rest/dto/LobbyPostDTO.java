package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class LobbyPostDTO {

    private String code;
    private List<Long> players = new ArrayList<Long>();

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
}
