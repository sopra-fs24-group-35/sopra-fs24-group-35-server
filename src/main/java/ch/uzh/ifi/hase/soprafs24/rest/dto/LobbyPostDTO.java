package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.ArrayList;


public class LobbyPostDTO {
    private ArrayList<Long> players = new ArrayList<Long>();

    public ArrayList<Long> getPlayers(){
        return players;
    }

    public void setPlayers(ArrayList<Long> players){
        this.players = players;
    }
}
