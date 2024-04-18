package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Attack;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GamePostDTO;
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
        gameService.startGame(lobbyId, createdGame.getGameId());

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(createdGame);
    }

    @PutMapping("/lobbies/{lobbyId}/game/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity updateGame(@PathVariable("lobbyId") Long lobbyId, @PathVariable("gameId") Long gameId,
        @RequestBody GamePostDTO gamePostDTO, @RequestHeader(name = "Authorization", required = true, defaultValue = "") String token) {
        // check if request is authorized
        gameService.checkAuthorization(lobbyId, token);
        // convert API user to internal representation
        Game thingsToUpdate = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);
        // update game data
        gameService.updateGame(thingsToUpdate, gameId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/lobbies/{lobbyId}/game/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteGame(@PathVariable("lobbyId") Long lobbyId, @PathVariable("gameId") Long gameId,
        @RequestBody GamePostDTO gamePostDTO, @RequestHeader(name = "Authorization", required = true, defaultValue = "") String token) {
        // check if request is authorized
        gameService.checkAuthorization(lobbyId, token);

        
        // convert API user to internal representation
        Game gameToDelete = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);
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
}