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

    @GetMapping("/lobby/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    @ResponseBody
    public LobbyGetDTO getLobby(@PathVariable("id") Long id){

        //find Lobby
        Lobby lobby = lobbyService.getLobbyById(id);

        //return Lobby
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
    }

    @PostMapping("/lobby")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody  LobbyPostDTO lobbyPostDTO){

        //transform input
        Lobby playerInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

        //create Lobby
        Lobby createdLobby = lobbyService.createLobby(playerInput);
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    @PostMapping("/lobby/update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public LobbyGetDTO updateLobby(@RequestBody Long player_id, String code){

        //update Lobby
        Lobby updatedLobby = lobbyService.updateLobby(player_id, code);
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(updatedLobby);
    }

    @PostMapping("/lobby/{id}/remove")
    @ResponseStatus(HttpStatus.GONE)
    @ResponseBody
    public LobbyGetDTO removePlayer(@PathVariable("id") Long id,
    @RequestBody Long player_id){
        Lobby updatedLobby = lobbyService.removePlayer(player_id, id);
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(updatedLobby);
    }
    
    
}