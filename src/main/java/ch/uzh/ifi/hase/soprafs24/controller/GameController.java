package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;



@RestController
public class GameController {
    
    // get game information
    @GetMapping("/lobbies/{lobbyId}/game/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    public GameGetDTO getGameById(@PathVariable("lobbyId") Long lobbyId, @PathVariable("gameId") Long gameId,
        @RequestHeader(name = "Authorization", required = true, defaultValue = "") String token) {
        // check if request is authorized
        lobbyService.checkAuthorization(Long.parseLong(lobbyId), token);
        // fetch user in the internal representation
        Game game = gameService.getGameById(gameId);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The game with the given ID doesn't exist.");
        }
        // convert user to the API representation
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    @PostMapping("/lobbies/{lobbyId}/game")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameGetDTO createGame(@RequestBody GamePostDTO gamePostDTO, HttpServletResponse response) {
        // convert API game to internal representation
        Game gameInput = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);
        // create game
        Game createdGame = gameService.createGame(gameInput);
        if (createdGame == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Game creation failed."));
        }
        response.addHeader("Authorization", createdGame.getToken());
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(createdGame);
    }

    @PutMapping("/lobbies/{lobbyId}/game/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity updateGame(@PathVariable("lobbyId") Long lobbyId, @PathVariable("gameId") Long gameId,
        @RequestBody GamePostDTO gamePostDTO, @RequestHeader(name = "Authorization", required = true, defaultValue = "") String token) {
        // check if request is authorized
        lobbyService.checkAuthorization(Long.parseLong(lobbyId), token);
        // convert API user to internal representation
        Game thingsToUpdate = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);
        // update game data
        Game toUpdate = gameService.updateGame(thingsToUpdate, Long.parseLong(gameId));
        if (toUpdate == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Game update failed");
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/lobbies/{lobbyId}/game/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteGame(@PathVariable("lobbyId") Long lobbyId, @PathVariable("gameId") Long gameId,
        @RequestBody GamePostDTO gamePostDTO, @RequestHeader(name = "Authorization", required = true, defaultValue = "") String token) {
        // check if request is authorized
        lobbyService.checkAuthorization(Long.parseLong(lobbyId), token);
        // convert API user to internal representation
        Game gameToDelete = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);
        // delete game data
        Game toDelete = gameService.deleteGame(gameToDelete, Long.parseLong(gameId));
        if (toDelete == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Game deletion failed");
        }
        return ResponseEntity.noContent().build();
    }
}