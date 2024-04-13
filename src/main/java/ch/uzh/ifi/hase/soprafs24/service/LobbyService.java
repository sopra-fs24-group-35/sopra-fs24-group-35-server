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

import java.util.Random;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Transactional
public class LobbyService{

    private final LobbyRepository lobbyRepository;

    Random rand = new Random();

    @PersistenceContext
    private EntityManager manager;

  
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

    public void checkAuthorization(Long lobby_id, String token) {
        Lobby lobbyById = lobbyRepository.getById(lobby_id);
        if (lobbyById == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            String.format("The Lobby with the ID %s doesn't exist.", lobby_id));
        } else if (!lobbyById.getToken().equals(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
            "Authorization failed. The user is not allowed to access this Lobby.");
        }
        return;
    }

    public void checkIfExists(long lobbyId) {
        if (checkIfLobbyExistsId(lobbyId, true) == false) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            "No lobby with this ID exists to add a game to.");
        }
    }

    public void addGameToLobby(long lobbyId, long gameId) {

        //get lobby from repository
        Lobby toUpdate = getLobbyById(lobbyId);

        //add the game to the lobby
        toUpdate.setGameId(gameId);

        //update Repository
        toUpdate = lobbyRepository.save(toUpdate);
        lobbyRepository.flush();

    }

    public void removeGameFromLobby(long lobbyId) {

        //get lobby from repository
        Lobby toUpdate = getLobbyById(lobbyId);

        //remove the game from the lobby
        toUpdate.setGameId(null);

        //update Repository
        toUpdate = lobbyRepository.save(toUpdate);
        lobbyRepository.flush();

    }

    public Lobby createLobby(Lobby newLobby){

        //Throw error if there is no players list with at least one player ID
        try {
            newLobby.getPlayers().get(0);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Lobby creation failed. No player id could be found in the players list of the request DTO, so the lobby owner can't be set.");
        }

        //Set Creator of Lobby as owner
        newLobby.setOwnerId(newLobby.getPlayers().get(0));

        //Set the token of the Lobby
        newLobby.setToken(UUID.randomUUID().toString());

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
        toUpdate.addPlayers(playerInput.getPlayers().get(0));

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
        
        Long playerId = playerInput.getPlayers().get(0);
        //remove Player
        toUpdate.removePlayers(playerId);

        //Check if player is LobbyOwner
        if (toUpdate.getOwnerId().equals(playerId)){
            //If no more players are in Lobby, delete Lobby
            if (toUpdate.getPlayers().isEmpty()) {
                manager.remove(toUpdate);
                return null;
            } //Else make next Player LobbyOwner
            else {
                toUpdate.setOwnerId(toUpdate.getPlayers().get(0));
            }
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