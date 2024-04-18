package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Board;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Territory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

@DataJpaTest
public class GameRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameRepository gameRepository;

    @Test
    public void findById_success() {
        
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
        game.setBoard(null);
        game.setPlayers(null);
        game.setTurnCycle(null);
        game.setDiceResult(null);

        // save game
        entityManager.persist(game);
        entityManager.flush();

        // when
        Game found = gameRepository.getByGameId(game.getGameId());

        // then
        assertNotNull(found.getGameId());
        assertEquals(found.getBoard(), game.getBoard());
        assertEquals(found.getPlayers(), game.getPlayers());
        assertEquals(found.getTurnCycle(), game.getTurnCycle());
        assertEquals(found.getDiceResult(), game.getDiceResult());
    }

    @Test
    public void deleteById_success() {
        
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
        game.setBoard(null);
        game.setPlayers(null);
        game.setTurnCycle(null);
        game.setDiceResult(null);

        // save game
        entityManager.persist(game);
        entityManager.flush();

        // delete game
        gameRepository.deleteByGameId(game.getGameId());

        // try to find game
        Game notFound = gameRepository.getByGameId(game.getGameId());

        // then
        assertEquals(null, notFound);
    }
}
