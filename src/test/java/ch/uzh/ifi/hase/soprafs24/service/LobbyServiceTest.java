package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

import org.hibernate.action.spi.Executable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class LobbyServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @InjectMocks
    private LobbyService lobbyService;

    private Lobby testLobby;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        //given
        //create testLobby
        testLobby = new Lobby();
        testLobby.setId(1L);
        testLobby.setCode("1234");
        testLobby.setOwnerId(2L);
        testLobby.addPlayers(2L);
        testLobby.setGameId(null);

        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        Mockito.when(lobbyRepository.save(Mockito.any())).thenReturn(testLobby);
    }

    @Test
    public void createLobby_validInputs() {
        // when -> any object is being save in the lobbyRepository -> return the dummy

        //create a lobby with one player in it
        Lobby lobby = new Lobby();
        lobby.addPlayers(2L);

        // testLobby
        Lobby createdLobby = lobbyService.createLobby(lobby);

    
        // then
        Mockito.verify(lobbyRepository, Mockito.times(1)).save(Mockito.any());
    
        assertEquals(testLobby.getId(), createdLobby.getId());
        assertNotEquals(testLobby.getOwnerId(), null);
    }

    @Test
    public void createLobby_invalidInput_noPlayer() {
        //create an empty lobby as input
        Lobby lobby = new Lobby();

        assertThrows(ResponseStatusException.class, () -> {
            lobbyService.createLobby(lobby);
        });
    }

    @Test
    public void getLobby_validInputs() {
        // when -> any object is being save in the gameRepository -> return the dummy
        // testLobby

        Mockito.when(lobbyRepository.getById(Mockito.any())).thenReturn(testLobby);

        Lobby fetchedLobby = lobbyService.getLobbyById(1L);

        assertEquals(testLobby.getId(), fetchedLobby.getId());
        assertEquals(testLobby.getCode(), fetchedLobby.getCode());
        assertEquals(testLobby.getPlayers(), fetchedLobby.getPlayers());
        assertEquals(testLobby.getOwnerId(), fetchedLobby.getOwnerId());
        assertEquals(testLobby.getGameId(), fetchedLobby.getGameId());
    }

    @Test
    public void getLobbyByCode_validInput() {
        //given
        //create lobbyCode
        String code = testLobby.getCode();

        //mock Repository
        Mockito.when(lobbyRepository.findByCode(testLobby.getCode())).thenReturn(testLobby);

        Lobby lobby = lobbyService.getLobbyByCode(code);

        assertEquals(testLobby.getId(), lobby.getId());
        assertEquals(testLobby.getToken(), lobby.getToken());
        assertEquals(testLobby.getPlayers(), lobby.getPlayers());
        assertEquals(testLobby.getOwnerId(), lobby.getOwnerId());
        assertEquals(testLobby.getGameId(), lobby.getGameId());
    }

    @Test
    public void getLobbyByCode_invalidInput_wrongCode() {
        //given
        //create code
        String code = "1";

        assertThrows(ResponseStatusException.class, () -> {
            Lobby lobby = lobbyService.getLobbyByCode(code);
        });
    }

    @Test
    public void getLobby_idDoesntExist_noSuccess() {
        // Assert that trying to get a game by an id that doesn't exist throws a HTTP ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> {       
            Lobby fetchedLobby = lobbyService.getLobbyById(10L);         
        } );
    }

    @Test
    public void updateLobby_wrongCode() {
        // Assert that trying to update a game by an id that doesn't exist throws a HTTP ResponseStatusException
        Lobby lobby = new Lobby();
        lobby.setCode("lol");
        lobby.addPlayers(3L);
        assertThrows(ResponseStatusException.class, () -> {       
            lobbyService.updateLobby(lobby);     
        } );
    }

    @Test
    public void deleteLobby_wrongId() {
        // Assert that trying to update a game by an id that doesn't exist throws a HTTP ResponseStatusException
        Lobby lobby = new Lobby();
        lobby.setId(2L);
        lobby.addPlayers(3L);
        assertThrows(ResponseStatusException.class, () -> {       
            lobbyService.removePlayer(lobby, 3L);     
        } );
    }

    @Test
    public void removePlayer_validInput() {
        //given
        //add player to lobby
        testLobby.addPlayers(3L);

        //create removePlayer request
        Lobby lobby = new Lobby();
        lobby.addPlayers(3L);

        Mockito.when(lobbyRepository.getById(Mockito.any())).thenReturn(testLobby);

        lobbyService.removePlayer(lobby, testLobby.getId());

        assertEquals(1, testLobby.getPlayers().size());
    }

    @Test
    public void removePlayer_doesntExist() {
        Lobby lobby = new Lobby();
        lobby.addPlayers(10L);

        assertThrows(ResponseStatusException.class, () -> {
            lobbyService.removePlayer(lobby, testLobby.getId());
        });
    }

    @Test
    public void startGame_validInput() {

        Mockito.when(lobbyRepository.getById(Mockito.any())).thenReturn(testLobby);

        Lobby lobby = lobbyService.startGame(testLobby.getId(), 10L);

        assertEquals(10L, lobby.getGameId());
    }

    @Test
    public void startGame_invalidInput_lobbyDoesntExist() {

        assertThrows(ResponseStatusException.class, () -> {
            lobbyService.startGame(10L, 11L);
        });
    }

    @Test
    public void endGame_invalidInput_lobbyDoesntExist() {
        assertThrows(ResponseStatusException.class, () -> {
            lobbyService.endGame(testLobby.getId());
        });
    }
}
