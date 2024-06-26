package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;




@RestController
public class LobbyController{

    private final LobbyService lobbyService;

    LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @GetMapping("/lobbies/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getLobby(@PathVariable("id") Long id,
    @RequestHeader(name = "Authorization", required = true, defaultValue = "") String token){
        //Check Authorization
        lobbyService.checkAuthorization(id, token);

        //find Lobby
        Lobby lobby = lobbyService.getLobbyById(id);

        if (lobby == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Getting Lobby failed. There exists no lobby with ID %s.", id));
        }

        //return Lobby
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody  LobbyPostDTO lobbyPostDTO, HttpServletResponse response){

        //transform input
        Lobby playerInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

        //create Lobby
        Lobby createdLobby = lobbyService.createLobby(playerInput);

        if (createdLobby == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Creating Lobby failed. Players is missing."));
        }

        //Also send token back through header
        response.addHeader("Authorization", createdLobby.getToken());
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    @PutMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO updateLobby(@RequestBody LobbyPutDTO lobbyPutDTO, HttpServletResponse response){
        
        Lobby playerInput = DTOMapper.INSTANCE.convertLobbyPutDTOtoEntity(lobbyPutDTO);

        //update Lobby
        Lobby updatedLobby = lobbyService.updateLobby(playerInput);

        if (updatedLobby == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            String.format("Updating Lobby failed. There either exists no lobby with code %s or players is missing.", playerInput.getCode()));
        }

        //Also send token back
        response.addHeader("Authorization", updatedLobby.getToken());

        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(updatedLobby);
    }

    @PutMapping("/lobbies/{id}/remove")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void removePlayer(@PathVariable("id") Long id,
        @RequestBody LobbyPostDTO lobbyPostDTO){

        Lobby playerInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

        Lobby updatedLobby = lobbyService.removePlayer(playerInput, id);

        if (updatedLobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            String.format("The Lobby with the given ID '%s' doesn't exist or player is not found.", id));
        }
    }
}