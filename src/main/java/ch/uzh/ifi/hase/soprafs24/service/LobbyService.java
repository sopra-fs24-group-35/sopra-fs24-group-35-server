package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Random;

@Service
@Transactional
public class LobbyService{

    private final Logger log = LoggerFactory.getLogger(LobbyService.class);

    private final LobbyRepository lobbyRepository;

    Random rand = new Random();

  
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    public Lobby getLobbyByCode(String code){
        boolean exists = checkIfLobbyExistsCode(code, true);
        if(!exists){
            return null;
        }
        return this.lobbyRepository.findByCode(code);
    }

    public Lobby getLobbyById(Long lobby_id){
        boolean exists = checkIfLobbyExistsId(lobby_id, true);
        if (!exists){
            return null;
        }
        return this.lobbyRepository.getById(lobby_id);
    }

    public Lobby createLobby(Lobby newLobby){

        boolean alreadyExists = checkIfLobbyExistsId(newLobby.getId(), false);
        if (!alreadyExists){
            return null;
        }

        //Creator of Lobby is already in players

        //Set the Lobby join Code
        newLobby.setCode(String.valueOf(rand.nextInt(10000)));

        // add new Lobby to repository
        newLobby = lobbyRepository.save(newLobby);
        lobbyRepository.flush();

        return newLobby;
    }

    public Lobby updateLobby(Lobby playerInput){

        String code = playerInput.getCode();

        boolean exists = checkIfLobbyExistsCode(code, true);
        if(!exists){
            return null;
        }
        
        Lobby toUpdate = getLobbyByCode(code);
        //add new player
        for(Long player : playerInput.getPlayers()){
            toUpdate.addPlayers(player);
        }

        //update Repository
        toUpdate = lobbyRepository.save(toUpdate);
        lobbyRepository.flush();

        return toUpdate;
    }

    public Lobby removePlayer(Lobby playerInput, Long lobby_id){
        boolean exists = checkIfLobbyExistsId(lobby_id, true);
        if (!exists){
            return null;
        }

        Lobby toUpdate = getLobbyById(lobby_id);
        
        //remove Player
        for (Long player: playerInput.getPlayers()){
            toUpdate.removePlayers(player);
        }

        //save in repository
        toUpdate = lobbyRepository.save(toUpdate);
        lobbyRepository.flush();

        return toUpdate;
    }

    private boolean checkIfLobbyExistsId(Long lobby_id, boolean shouldExist){
        Lobby lobbyById = this.lobbyRepository.getById(lobby_id);
        // shouldExist: if true, an error is thrown if the user doesn't exist (and vice versa)
        if (shouldExist && lobbyById == null) {
            return false;
        } else if (!shouldExist && lobbyById != null) {
            return false;
        }
        return true;
    }

    private boolean checkIfLobbyExistsCode(String code, boolean shouldExist){
        Lobby lobbyByCode = lobbyRepository.findByCode(code);

        if (shouldExist && lobbyByCode == null) {
            return false;
        } else if (!shouldExist && lobbyByCode == null) {
            return false;
        }
        return true;
    }
}