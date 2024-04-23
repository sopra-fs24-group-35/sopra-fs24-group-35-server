package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.Phase;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Attack;
import ch.uzh.ifi.hase.soprafs24.entity.Board;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.Territory;
import ch.uzh.ifi.hase.soprafs24.entity.TurnCycle;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

import org.hibernate.mapping.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
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
public class GameServiceIntegrationTest {

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameService gameService;

    @BeforeEach
    public void setup() {
        gameRepository.deleteAll();
    }

    @Test
    @DirtiesContext
    public void createGame_validInputs_success() {
        // check that repository is empty
        assertFalse(gameRepository.findById(1L).isPresent());

        // given
        // create a board
        Board board = new Board();
        ArrayList<Territory> territories = new ArrayList<>();
        Territory paradeplatz = new Territory();
        paradeplatz.setName("Paradeplatz");
        paradeplatz.setTroops(7);
        territories.add(paradeplatz);
        Territory central = new Territory();
        central.setName("Central");
        central.setTroops(7);
        territories.add(central);
        board.setTerritories(territories);

        // create Game with this board
        Game testGame = new Game();
        testGame.setBoard(board);
        testGame.setPlayers(null);
        testGame.setTurnCycle(null);
        testGame.setDiceResult("Atk 1 2 Def 3 4");

        // when
        Game createdGame = gameService.createGame(testGame);

        // then check that the game is saved in the repository
        assertNotNull(gameRepository.findById(1L));
        assertTrue(gameRepository.findById(1L).isPresent());
    }

    @Test
    @DirtiesContext
    public void getGameById_validInputs_success() {
        // check that repository is empty
        // given
        // create a board
        Board board = new Board();
        ArrayList<Territory> territories = new ArrayList<>();
        Territory paradeplatz = new Territory();
        paradeplatz.setName("Paradeplatz");
        paradeplatz.setTroops(7);
        territories.add(paradeplatz);
        Territory central = new Territory();
        central.setName("Central");
        central.setTroops(7);
        territories.add(central);
        board.setTerritories(territories);

        // create Game with this board
        Game testGame = new Game();
        testGame.setBoard(board);
        testGame.setPlayers(null);
        testGame.setTurnCycle(null);
        testGame.setDiceResult("Atk 1 2 Def 3 4");

        // save the game in the repository
        gameRepository.save(testGame);
        assertTrue(gameRepository.findById(1L).isPresent());

        // when
        Game fetchedGame = gameService.getGameById(1L);

        // then check that the returned game is not null
        assertNotNull(fetchedGame);
        assertEquals(fetchedGame.getGameId(), testGame.getGameId());
    }

    //@Test
    @DirtiesContext
    public void updateGame_validInputs_success() {
        // given
        // create a board
        Board board = new Board();
        ArrayList<Territory> territories = new ArrayList<>();
        Territory paradeplatz = new Territory();
        paradeplatz.setName("Paradeplatz");
        paradeplatz.setTroops(7);
        territories.add(paradeplatz);
        Territory central = new Territory();
        central.setName("Central");
        central.setTroops(7);
        territories.add(central);
        board.setTerritories(territories);

        Player player = new Player();
        player.setPlayerId(7L);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player);

        // create a TurnCycle entity
        TurnCycle turnCycle = new TurnCycle();
        turnCycle.setCurrentPhase(Phase.REINFORCEMENT);
        turnCycle.setCurrentPlayer(player);

        // create Game with this board
        Game testGame = new Game();
        testGame.setBoard(board);
        testGame.setPlayers(players);
        testGame.setTurnCycle(turnCycle);
        testGame.setDiceResult("Atk 1 2 Def 3 4");

        // save the game in the repository
        gameRepository.save(testGame);
        assertTrue(gameRepository.findById(1L).isPresent());

        // updated information
        Board board2 = new Board();
        ArrayList<Territory> territories2 = new ArrayList<>();
        Territory paradeplatz2 = new Territory();
        paradeplatz2.setName("Paradeplatz");
        paradeplatz2.setTroops(10); // <- changed to 10
        territories2.add(paradeplatz2);
        Territory central2 = new Territory();
        central2.setName("Central");
        central2.setTroops(7);
        territories2.add(central2);
        board2.setTerritories(territories2);

        Player player2 = new Player();
        player.setPlayerId(7L);
        ArrayList<Player> players2 = new ArrayList<>();
        players.add(player2);

        TurnCycle turnCycle2 = new TurnCycle();
        turnCycle2.setCurrentPhase(Phase.REINFORCEMENT);
        turnCycle.setCurrentPlayer(player2);

        // create Game with this board
        Game testGame2 = new Game();
        testGame2.setBoard(board2);
        testGame2.setPlayers(players2);
        testGame2.setTurnCycle(turnCycle);
        testGame2.setDiceResult("Atk 1 2 Def 3 4");

        // when
        gameService.updateGame(testGame2, 1L);
        Game fetchedGame = gameService.getGameById(1L);

        // then check that the amount of troops is changed to 10
        assertNotNull(fetchedGame);
        assertEquals(fetchedGame.getBoard().getTerritories().get(0).getTroops(), 10);
    }

    @Test
    @DirtiesContext
    public void deleteGame_validInputs_success() {
        // check that repository is empty
        // given
        // create a board
        Board board = new Board();
        ArrayList<Territory> territories = new ArrayList<>();
        Territory paradeplatz = new Territory();
        paradeplatz.setName("Paradeplatz");
        paradeplatz.setTroops(7);
        territories.add(paradeplatz);
        Territory central = new Territory();
        central.setName("Central");
        central.setTroops(7);
        territories.add(central);
        board.setTerritories(territories);

        // create Game with this board
        Game testGame = new Game();
        testGame.setBoard(board);
        testGame.setPlayers(null);
        testGame.setTurnCycle(null);
        testGame.setDiceResult("Atk 1 2 Def 3 4");

        // save the game in the repository
        gameRepository.save(testGame);
        assertTrue(gameRepository.findById(1L).isPresent());

        // when
        gameService.deleteGame(1L);

        // then check that the game is deleted
        assertFalse(gameRepository.findById(1L).isPresent());
    }

    @Test
    @DirtiesContext
    public void executeRepeatedAttacks_validInputs_success() {
        
        // given
        // create an attack
        Attack attack = new Attack();
        attack.setAttackingTerritory("Paradeplatz");
        attack.setDefendingTerritory("Central");
        attack.setRepeats(3);
        attack.setTroopsAmount(2);

        // create a board
        Board board = new Board();
        ArrayList<Territory> territories = new ArrayList<>();
        Territory paradeplatz = new Territory();
        paradeplatz.setName("Paradeplatz");
        paradeplatz.setTroops(7);
        territories.add(paradeplatz);
        Territory central = new Territory();
        central.setName("Central");
        central.setTroops(7);
        territories.add(central);
        board.setTerritories(territories);

        // create Game with this board
        Game testGame = new Game();
        testGame.setBoard(board);
        testGame.setPlayers(null);
        testGame.setTurnCycle(null);
        testGame.setDiceResult("Atk 1 2 Def 3 4");

        // save the game in the repository
        gameRepository.save(testGame);
        assertTrue(gameRepository.findById(1L).isPresent());

        // when
        Game updatedGame = gameService.executeRepeatedAttacks(attack, 1L);

        // then check that the game is not deleted
        assertTrue(gameRepository.findById(1L).isPresent());

        // check that game is updated
        assertNotEquals(testGame, updatedGame);
        assertNotEquals(testGame.getBoard(), updatedGame.getBoard());
    }


}