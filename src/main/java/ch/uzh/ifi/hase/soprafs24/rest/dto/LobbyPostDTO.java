package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.ArrayList;


public class LobbyPostDTO {

    private String code;
    private ArrayList<Long> players = new ArrayList<Long>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
    this.code = code;
    }

    public ArrayList<Long> getPlayers(){
        return players;
    }

    public void setPlayers(ArrayList<Long> players){
        this.players = players;
    }
}
