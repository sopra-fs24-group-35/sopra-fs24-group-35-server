package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyPostDTO;
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
    public LobbyGetDTO getLobby(@PathVariable("id") Long id){

        //find Lobby
        Lobby lobby = lobbyService.getLobbyById(id);

        //return Lobby
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody  LobbyPostDTO lobbyPostDTO){

        //transform input
        Lobby playerInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

        //create Lobby
        Lobby createdLobby = lobbyService.createLobby(playerInput);
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    @PostMapping("/lobbies/update")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO updateLobby(@RequestBody LobbyPostDTO lobbyPostDTO){
        
        Lobby playerInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

        //update Lobby
        Lobby updatedLobby = lobbyService.updateLobby(playerInput);

        if (updatedLobby == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("The Lobby with the given Code '%s' doesn't exist.", playerInput.getCode()));
        }

        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(updatedLobby);
    }

    @PostMapping("/lobbies/{id}/remove")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO removePlayer(@PathVariable("id") Long id,
        @RequestBody LobbyPostDTO lobbyPostDTO){

        Lobby playerInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

        Lobby updatedLobby = lobbyService.removePlayer(playerInput, id);
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(updatedLobby);
    }
}