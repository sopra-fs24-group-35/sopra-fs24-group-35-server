package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

@DataJpaTest
public class LobbyRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LobbyRepository lobbyRepository;

    @Test
    public void findById_success() {
        
        // given
        // create Lobby
        Lobby lobby = new Lobby();
        lobby.setCode("1234");
        lobby.setOwnerId(2L);
        lobby.addPlayers(2L);
        lobby.setToken("abc");

        // save lobby
        entityManager.persist(lobby);
        entityManager.flush();

        // when
        Lobby found = lobbyRepository.getById(lobby.getId());

        // then
        assertNotNull(found.getId());
        assertEquals(lobby.getCode(), found.getCode());
        assertEquals(lobby.getOwnerId(), found.getOwnerId());
        assertEquals(lobby.getPlayers(), found.getPlayers());
    }

    @Test
    public void deleteById_success() {
        
        // given
        // create Lobby
        Lobby lobby = new Lobby();
        lobby.setCode("1234");
        lobby.setOwnerId(2L);
        lobby.addPlayers(2L);
        lobby.setToken("abc");

        // save lobby
        entityManager.persist(lobby);
        entityManager.flush();

        // delete lobby
        lobbyRepository.deleteById(lobby.getId());

        // try to find lobby
        Lobby notFound = lobbyRepository.getById(lobby.getId());

        // then
        assertEquals(null, notFound);
    }
}
