package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Board;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.constant.Phase;
import ch.uzh.ifi.hase.soprafs24.entity.Territory;
import ch.uzh.ifi.hase.soprafs24.entity.TurnCycle;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.AttackPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(GameController.class)
public class GameControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private LobbyService lobbyService;

    // GET tests ----------------------------------------------------------------------------------------------------

    @Test
    public void givenGame_whenGetGame_thenReturnGame() throws Exception {
        // given
        Game game = new Game();
        game.setGameId(1L);
        game.setBoard(null);
        game.setPlayers(null);
        game.setTurnCycle(null);
        game.setDiceResult(null);

        // Mocking
        given(gameService.getGameById(1L)).willReturn(game);

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/game/1")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "abc");

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk());
    }

    @Test
    public void givenNoGame_whenGetGame_thenReturnNotFound() throws Exception {
        // given
        Game game = new Game();
        game.setGameId(1L);
        game.setBoard(null);
        game.setPlayers(null);
        game.setTurnCycle(null);
        game.setDiceResult(null);

        // Mocking
        given(gameService.getGameById(1L)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "No game with this id could be found."));

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/game/1")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "abc");

        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }

    @Test
    public void givenGame_whenUnauthorizedGetGame_thenReturnUnauthorized() throws Exception {
        // given
        Game game = new Game();
        game.setGameId(1L);
        game.setBoard(null);
        game.setPlayers(null);
        game.setTurnCycle(null);
        game.setDiceResult(null);

        // Mocking
        given(gameService.getGameById(1L)).willReturn(game);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization failed. The user is not allowed to access this Lobby.")).when(gameService).checkAuthorization(Mockito.any(), Mockito.any());

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/game/1")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "abc");

        // then
        mockMvc.perform(getRequest).andExpect(status().isUnauthorized());
    }

    @Test
    public void givenGame_whenPullCard_thenReturnGame() throws Exception {
        // given
        Game game = new Game();
        game.setGameId(1L);
        game.setBoard(null);
        game.setPlayers(null);
        game.setTurnCycle(null);
        game.setDiceResult(null);

        // Mocking
        given(gameService.pullCard(1L)).willReturn(game);

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/game/1/cards")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "abc");

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
            .andExpect(jsonPath("$.gameId", is(game.getGameId().intValue())))
            .andExpect(jsonPath("$.board", is(game.getBoard())))
            .andExpect(jsonPath("$.players", is(game.getPlayers())))
            .andExpect(jsonPath("$.turnCycle", is(game.getTurnCycle())));
    }


    // POST tests ----------------------------------------------------------------------------------------------------

    @Test
    public void createGame_validInput_gameCreated() throws Exception {
        // given
        Game game = new Game();
        game.setGameId(1L);
        game.setBoard(null);
        game.setPlayers(null);
        game.setTurnCycle(null);
        game.setDiceResult("Atk 1 2 Def 3 4");

        Lobby lobby = new Lobby();
        lobby.setId(1L);

        GamePostDTO gamePostDTO = new GamePostDTO();

        given(gameService.createGame(Mockito.any())).willReturn(game);
        given(gameService.startGame(Mockito.any(), Mockito.any())).willReturn(lobby);
        given(gameService.addPlayers(Mockito.any(), Mockito.any())).willReturn(game);
        given(gameService.randomizedBeginning(Mockito.any())).willReturn(game);
        doNothing().when(gameService).checkIfLobbyExists(1L);
        
        

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/game")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(gamePostDTO))
            .header("Authorization", "abc");
            
        // perform validation -> check if the method returns the correct HTTP response (Code: 201, correct Game DTO in the JSON of the body)
        mockMvc.perform(postRequest)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.gameId", is(game.getGameId().intValue())))
            .andExpect(jsonPath("$.board", is(game.getBoard())))
            .andExpect(jsonPath("$.players", is(game.getPlayers())))
            .andExpect(jsonPath("$.turnCycle", is(game.getTurnCycle())))
            .andExpect(jsonPath("$.diceResult", is(game.getDiceResult().toString())));
    }

    @Test
    public void createGame_noLobby_LobbyNotFound() throws Exception {
        // given
        Game game = new Game();
        game.setGameId(1L);
        game.setBoard(null);
        game.setPlayers(null);
        game.setTurnCycle(null);
        game.setDiceResult("Atk 1 2 Def 3 4");

        Lobby lobby = new Lobby();
        lobby.setId(1L);

        GamePostDTO gamePostDTO = new GamePostDTO();

        given(gameService.createGame(Mockito.any())).willReturn(game);
        given(gameService.startGame(Mockito.any(), Mockito.any())).willReturn(lobby);
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,"No lobby with this ID exists to add a game to.")).when(gameService).checkIfLobbyExists(1L);
        
        

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/game")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(gamePostDTO))
            .header("Authorization", "abc");
            
            
        
        mockMvc.perform(postRequest)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isNotFound());
            
    }

    @Test
    public void createGame_notAuthorized_Unauthorized() throws Exception {
        // given
        Game game = new Game();
        game.setGameId(1L);
        game.setBoard(null);
        game.setPlayers(null);
        game.setTurnCycle(null);
        game.setDiceResult("Atk 1 2 Def 3 4");

        Lobby lobby = new Lobby();
        lobby.setId(1L);

        GamePostDTO gamePostDTO = new GamePostDTO();

        given(gameService.createGame(Mockito.any())).willReturn(game);
        given(gameService.startGame(Mockito.any(), Mockito.any())).willReturn(lobby);
        doNothing().when(gameService).checkIfLobbyExists(1L);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization failed. The user is not allowed to access this Lobby.")).when(gameService).checkAuthorization(Mockito.any(), Mockito.any());
        
        

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/game")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(gamePostDTO))
            .header("Authorization", "abc");
            
            
        
        mockMvc.perform(postRequest)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isUnauthorized());
            
    }

    // PUT tests ----------------------------------------------------------------------------------------------------

    @Test
    public void updateGame_validInput_gameUpdated() throws Exception {
        // given
        // create board with a territory called 'Paradeplatz'
        Board board = new Board();
        ArrayList<Territory> territories = new ArrayList<>();
        Territory paradeplatz = new Territory();
        paradeplatz.setName("Paradeplatz");
        paradeplatz.setTroops(7);
        territories.add(paradeplatz);
        board.setTerritories(territories);

        //create 2 players
        Player player1 = new Player();
        player1.setPlayerId(2L);

        Player player2 = new Player();
        player2.setPlayerId(3L);

        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);

        //create TurnCycleBefore and TurnCycleAfter
        TurnCycle turnCycleBefore = new TurnCycle();
        turnCycleBefore.setCurrentPlayer(player1);
        turnCycleBefore.setPlayerCycle(players);
        turnCycleBefore.setCurrentPhase(Phase.REINFORCEMENT);

        TurnCycle turnCycleAfter = new TurnCycle();
        turnCycleAfter.setCurrentPlayer(player1);
        turnCycleAfter.setPlayerCycle(players);
        turnCycleAfter.setCurrentPhase(Phase.ATTACK);

        // create Game with this board
        Game game = new Game();
        game.setGameId(1L);
        game.setBoard(board);
        game.setPlayers(players);
        game.setTurnCycle(turnCycleAfter);


        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setBoard(board);
        gamePostDTO.setTurnCycle(turnCycleBefore);

        given(gameService.updateGame(Mockito.any(), Mockito.any())).willReturn(game);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbies/1/game/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(gamePostDTO))
            .header("Authorization", "abc");
        

        // then
        mockMvc.perform(putRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.turnCycle.currentPhase", is(game.getTurnCycle().getCurrentPhase().toString())));
    }

    

    @Test
    public void updateGame_validInput_gameNotFound() throws Exception {
        // given
        // create board with a territory called 'Paradeplatz'
        Board board = new Board();
        ArrayList<Territory> territories = new ArrayList<>();
        Territory paradeplatz = new Territory();
        paradeplatz.setName("Paradeplatz");
        paradeplatz.setTroops(7);
        territories.add(paradeplatz);
        board.setTerritories(territories);

        // create Game with this board
        Game game = new Game();
        game.setGameId(1L);
        game.setBoard(board);
        game.setPlayers(null);
        game.setTurnCycle(null);


        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setBoard(board);

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Game update failed, because there is no game with this id.")).when(gameService).updateGame(Mockito.any(), Mockito.any());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbies/1/game/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(gamePostDTO))
            .header("Authorization", "abc");
        

        // then
        mockMvc.perform(putRequest)
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateGame_invalidInput_notAuthorized() throws Exception {
        // given
        // create board with a territory called 'Paradeplatz'
        Board board = new Board();
        ArrayList<Territory> territories = new ArrayList<>();
        Territory paradeplatz = new Territory();
        paradeplatz.setName("Paradeplatz");
        paradeplatz.setTroops(7);
        territories.add(paradeplatz);
        board.setTerritories(territories);

        // create Game with this board
        Game game = new Game();
        game.setGameId(1L);
        game.setBoard(board);
        game.setPlayers(null);
        game.setTurnCycle(null);


        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setBoard(board);

        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization failed. The user is not allowed to access this Lobby.")).when(gameService).checkAuthorization(Mockito.any(), Mockito.any());
        given(gameService.updateGame(Mockito.any(), Mockito.any())).willReturn(game);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbies/1/game/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(gamePostDTO))
            .header("Authorization", "abc");
        

        // then
        mockMvc.perform(putRequest)
            .andExpect(status().isUnauthorized());
    }

    // DELETE tests ----------------------------------------------------------------------------------------------------

    @Test
    public void deleteGame_validInput_gameDeleted() throws Exception {
        // given
        // create board with a territory called 'Paradeplatz'
        Board board = new Board();
        ArrayList<Territory> territories = new ArrayList<>();
        Territory paradeplatz = new Territory();
        paradeplatz.setName("Paradeplatz");
        paradeplatz.setTroops(7);
        territories.add(paradeplatz);
        board.setTerritories(territories);

        // create Game with this board
        Game game = new Game();
        game.setGameId(1L);
        game.setBoard(board);
        game.setPlayers(null);
        game.setTurnCycle(null);


        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setBoard(board);

        doNothing().when(gameService).deleteGame(Mockito.any());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder deleteRequest = delete("/lobbies/1/game/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(gamePostDTO))
            .header("Authorization", "abc");
        

        // then
        mockMvc.perform(deleteRequest)
            .andExpect(status().isNoContent());
    }

    @Test
    public void deleteGame_validInput_gameNotFound() throws Exception {
        // given
        // create board with a territory called 'Paradeplatz'
        Board board = new Board();
        ArrayList<Territory> territories = new ArrayList<>();
        Territory paradeplatz = new Territory();
        paradeplatz.setName("Paradeplatz");
        paradeplatz.setTroops(7);
        territories.add(paradeplatz);
        board.setTerritories(territories);

        // create Game with this board
        Game game = new Game();
        game.setGameId(1L);
        game.setBoard(board);
        game.setPlayers(null);
        game.setTurnCycle(null);


        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setBoard(board);

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Game deletion failed, because there is no game with this id.")).when(gameService).deleteGame(Mockito.any());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder deleteRequest = delete("/lobbies/1/game/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(gamePostDTO))
            .header("Authorization", "abc");
        

        // then
        mockMvc.perform(deleteRequest)
            .andExpect(status().isNotFound());
    }

    @Test
    public void deleteGame_invalidInput_notAuthorized() throws Exception {
        // given
        // create board with a territory called 'Paradeplatz'
        Board board = new Board();
        ArrayList<Territory> territories = new ArrayList<>();
        Territory paradeplatz = new Territory();
        paradeplatz.setName("Paradeplatz");
        paradeplatz.setTroops(7);
        territories.add(paradeplatz);
        board.setTerritories(territories);

        // create Game with this board
        Game game = new Game();
        game.setGameId(1L);
        game.setBoard(board);
        game.setPlayers(null);
        game.setTurnCycle(null);


        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setBoard(board);

        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization failed. The user is not allowed to access this Lobby.")).when(gameService).checkAuthorization(Mockito.any(), Mockito.any());
        given(gameService.updateGame(Mockito.any(), Mockito.any())).willReturn(game);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder deleteRequest = delete("/lobbies/1/game/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(gamePostDTO))
            .header("Authorization", "abc");
        

        // then
        mockMvc.perform(deleteRequest)
            .andExpect(status().isUnauthorized());
    }

    // POST/attack tests ----------------------------------------------------------------------------------------------------

    @Test
    public void attack_validInput_executeAttack() throws Exception {
        // given
        Game game = new Game();
        game.setGameId(1L);
        game.setBoard(null);
        game.setPlayers(null);
        game.setTurnCycle(null);
        game.setDiceResult("Atk 1 2 Def 3 4");

        AttackPostDTO attackPostDTO = new AttackPostDTO();

        given(gameService.executeRepeatedAttacks(Mockito.any(), Mockito.any())).willReturn(game);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/game/1/attacks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(attackPostDTO))
            .header("Authorization", "abc");
        
        mockMvc.perform(postRequest)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.gameId", is(game.getGameId().intValue())))
            .andExpect(jsonPath("$.board", is(game.getBoard())))
            .andExpect(jsonPath("$.players", is(game.getPlayers())))
            .andExpect(jsonPath("$.turnCycle", is(game.getTurnCycle())))
            .andExpect(jsonPath("$.diceResult", is(game.getDiceResult().toString())));
    }

    @Test
    public void attack_invalidInput_notAuthorized() throws Exception {
        // given
        Game game = new Game();
        game.setGameId(1L);
        game.setBoard(null);
        game.setPlayers(null);
        game.setTurnCycle(null);
        game.setDiceResult("Atk 1 2 Def 3 4");

        AttackPostDTO attackPostDTO = new AttackPostDTO();

        given(gameService.executeRepeatedAttacks(Mockito.any(), Mockito.any())).willReturn(game);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization failed. The user is not allowed to access this Lobby.")).when(gameService).checkAuthorization(Mockito.any(), Mockito.any());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/game/1/attacks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(attackPostDTO))
            .header("Authorization", "abc");
        
        mockMvc.perform(postRequest)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isUnauthorized());
    }

    // helpers

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            String.format("The request body could not be created.%s", e.toString()));
        }
    }

}
