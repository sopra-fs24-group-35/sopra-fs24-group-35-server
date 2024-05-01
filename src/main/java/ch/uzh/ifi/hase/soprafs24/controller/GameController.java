package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.Territory;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Attack;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TerritoryGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.AttackPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;



@RestController
public class GameController {

    private final GameService gameService;

        
    GameController(GameService gameService) {
        this.gameService = gameService;
    }
    
    // get game information
    @GetMapping("/lobbies/{lobbyId}/game/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    public GameGetDTO getGameById(@PathVariable("lobbyId") Long lobbyId, @PathVariable("gameId") Long gameId,
        @RequestHeader(name = "Authorization", required = true, defaultValue = "") String token) {
        // check if request is authorized
        gameService.checkAuthorization(lobbyId, token);
        // fetch user in the internal representation
        Game game = gameService.getGameById(gameId);
        // convert user to the API representation
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    @GetMapping("/lobbies/{lobbyId}/game/{gameId}/territory/{territoryName}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TerritoryGetDTO getTerritoryByName(@PathVariable("lobbyId") Long lobbyId, @PathVariable("gameId") Long gameId,
        @PathVariable("territoryName") String territoryName,
        @RequestHeader(name = "Authorization", required = true, defaultValue = "") String token) {

        // check if request is authorized
        gameService.checkAuthorization(lobbyId, token);

        //fetch the territory
        Territory territory = gameService.getTerritory(gameId, territoryName);

        return DTOMapper.INSTANCE.convertEntityToTerritoryGetDTO(territory);
    }

    // create new game
    @PostMapping("/lobbies/{lobbyId}/game")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameGetDTO createGame(@PathVariable("lobbyId") Long lobbyId,
        @RequestBody GamePostDTO gamePostDTO, @RequestHeader(name = "Authorization", required = true, defaultValue = "") String token) {
        // convert API game to internal representation
        Game gameInput = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);
        // check if request is authorized
        gameService.checkAuthorization(lobbyId, token);
        // check if lobby exists
        gameService.checkIfLobbyExists(lobbyId);
        // create game
        Game createdGame = gameService.createGame(gameInput);

        //link game to Lobby
        Lobby lobby = gameService.startGame(lobbyId, createdGame.getGameId());
        createdGame = gameService.addPlayers(lobby.getPlayers(), createdGame.getGameId());
        createdGame = gameService.randomizedBeginning(createdGame);
        gameService.startGame(lobbyId, createdGame.getGameId());

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(createdGame);
    }

    @PutMapping("/lobbies/{lobbyId}/game/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    public GameGetDTO updateGame(@PathVariable("lobbyId") Long lobbyId, @PathVariable("gameId") Long gameId,
        @RequestBody GamePostDTO gamePostDTO, @RequestHeader(name = "Authorization", required = true, defaultValue = "") String token) {
        // check if request is authorized
        gameService.checkAuthorization(lobbyId, token);
        // convert API user to internal representation
        Game thingsToUpdate = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);
        // update game data
        thingsToUpdate = gameService.updateGame(thingsToUpdate, gameId);
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(thingsToUpdate);
    }

    @DeleteMapping("/lobbies/{lobbyId}/game/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteGame(@PathVariable("lobbyId") Long lobbyId, @PathVariable("gameId") Long gameId,
        @RequestHeader(name = "Authorization", required = true, defaultValue = "") String token) {

        // check if request is authorized
        gameService.checkAuthorization(lobbyId, token);

        // check if lobby exists
        gameService.checkIfLobbyExists(lobbyId);
        // set GameId in Lobby to null
        gameService.endGame(lobbyId);
        // delete game data
        gameService.deleteGame(gameId);
        
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/lobbies/{lobbyId}/game/{gameId}/attacks")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO executeAttack(@PathVariable("lobbyId") Long lobbyId, @PathVariable("gameId") Long gameId,
        @RequestBody AttackPostDTO attackPostDTO, @RequestHeader(name = "Authorization", required = true, defaultValue = "") String token) {
        // convert API game to internal representation
        Attack attack = DTOMapper.INSTANCE.convertAttackPostDTOtoEntity(attackPostDTO);
        // check if request is authorized
        gameService.checkAuthorization(lobbyId, token);
        // execute attack
        Game updatedGame = gameService.executeRepeatedAttacks(attack, gameId);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(updatedGame);
    } 

    @PutMapping("lobbies/{lobbyId}/game/{gameId}/transfer")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO transferTroops(@PathVariable("lobbyId") Long lobbyId, @PathVariable("gameId") Long gameId,
    @RequestBody AttackPostDTO attackPostDTO, @RequestHeader(name = "Authorization", required = true, defaultValue = "") String token) {
        // convert API game to internal representation
        Attack attack = DTOMapper.INSTANCE.convertAttackPostDTOtoEntity(attackPostDTO);
        // check if request is authorized
        gameService.checkAuthorization(lobbyId, token);
        // execute attack
        Game updatedGame = gameService.transferTroops(attack, gameId);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(updatedGame);
    }

    @PutMapping("lobbies/{lobbyId}/game/{gameId}/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void leaveGame(@PathVariable("lobbyId") Long lobbyId, @PathVariable("gameId") Long gameId, @PathVariable("userId") Long userId,
    @RequestHeader(name = "Authorization", required = true, defaultValue = "") String token) {
        
        gameService.checkAuthorization(lobbyId, token);

        gameService.leaveGame(gameId, lobbyId, userId);
    }
}