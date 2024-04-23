package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.ArrayList;

import ch.uzh.ifi.hase.soprafs24.entity.Board;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.TurnCycle;

public class GameGetDTO {
    
    private Long gameId;
    private Board board;
    private ArrayList<Player> players;
    private TurnCycle turnCycle;
    private String diceResult;

    public Long getGameId() {
        return gameId;
    }
      
    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Board getBoard() {
        return board;
    }
      
    public void setBoard(Board board) {
        this.board = board;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
      
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

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

}
