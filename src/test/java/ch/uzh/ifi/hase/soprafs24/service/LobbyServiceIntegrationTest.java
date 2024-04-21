package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Optional;


/**
 * Test class for the GameResource REST resource.
 *
 * @see GameService
 */
@WebAppConfiguration
@SpringBootTest
public class LobbyServiceIntegrationTest {

    @Qualifier("lobbyRepository")
    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private LobbyService lobbyService;

    @BeforeEach
    public void setup() {
        lobbyRepository.deleteAll();
    }

    @Test
    public void createLobby_validInput() {
        // check that repository is empty
        assertFalse(lobbyRepository.findById(1L).isPresent());

        // given
        // create Lobby
        Lobby testLobby = new Lobby();
        testLobby.addPlayers(2L);

        // when
        Lobby createdLobby = lobbyService.createLobby(testLobby);

        // then check that the game is saved in the repository
        assertNotNull(lobbyRepository.findById(1L));
        assertTrue(lobbyRepository.findById(1L).isPresent());
    }

    @Test
    public void getLobbyById_validInput() {
        // check that repository is empty
        assertFalse(lobbyRepository.findById(1L).isPresent());
        // given
        // create Lobby
        Lobby testLobby = new Lobby();
        testLobby.addPlayers(2L);
        testLobby.setOwnerId(2L);
        testLobby.setCode("1234");
        testLobby.setToken("abc");

        // save the game in the repository
        lobbyRepository.save(testLobby);
        assertTrue(lobbyRepository.findById(1L).isPresent());

        // when
        Lobby fetchedLobby = lobbyService.getLobbyById(1L);

        // then check that the returned game is not null
        assertNotNull(fetchedLobby);
        assertEquals(fetchedLobby.getId(), testLobby.getId());
    }

    @Test
    public void updateLobby_validInput() {
        // check that repository is empty
        assertFalse(lobbyRepository.findById(1L).isPresent());

        // given
        // create Lobby
        Lobby testLobby = new Lobby();
        testLobby.addPlayers(2L);
        testLobby.setOwnerId(2L);
        testLobby.setCode("1234");
        testLobby.setToken("abc");

        // save the game in the repository
        lobbyRepository.save(testLobby);
        assertTrue(lobbyRepository.findById(1L).isPresent());

        // create Lobby with player and code to update testLobby
        Lobby testLobby2 = new Lobby();
        testLobby2.addPlayers(3L);
        testLobby2.setCode("1234");

        // when
        lobbyService.updateLobby(testLobby2);
        Lobby fetchedLobby = lobbyService.getLobbyById(1L);

        // then check that the amount of players is changed to 2
        assertNotNull(fetchedLobby);
        assertEquals(fetchedLobby.getPlayers().size(), 2);
    }

    @Test
    public void deleteLobby_validInput() {
        // check that repository is empty
        assertFalse(lobbyRepository.findById(1L).isPresent());

        // given
        // create Lobby
        Lobby testLobby = new Lobby();
        testLobby.addPlayers(2L);
        testLobby.setOwnerId(2L);
        testLobby.setCode("1234");
        testLobby.setToken("abc");

        // save the game in the repository
        lobbyRepository.save(testLobby);
        assertTrue(lobbyRepository.findById(1L).isPresent());

        //create Lobby with player to remove
        Lobby lobby = new Lobby();
        lobby.addPlayers(2L);

        // when
        lobbyService.removePlayer(lobby, 1L);

        // then check that the game is deleted
        assertFalse(lobbyRepository.findById(1L).isPresent());
    }
}
