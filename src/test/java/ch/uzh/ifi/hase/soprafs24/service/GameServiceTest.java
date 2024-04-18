package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Attack;
import ch.uzh.ifi.hase.soprafs24.entity.Board;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Territory;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
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

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    private Game testGame;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

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
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setBoard(board);
        testGame.setPlayers(null);
        testGame.setTurnCycle(null);
        testGame.setDiceResult("Atk 1 2 Def 3 4");

        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        


    }

    @Test
    public void createGame_validInputs_success() {
        // when -> any object is being save in the gameRepository -> return the dummy
        // testGame
        Game createdGame = gameService.createGame(new Game());

    
        // then
        Mockito.verify(gameRepository, Mockito.times(1)).save(Mockito.any());
    
        assertEquals(testGame.getGameId(), createdGame.getGameId());
        assertNotEquals(testGame.getBoard(), null);
    }

    @Test
    public void getGame_validInputs_success() {
        // when -> any object is being save in the gameRepository -> return the dummy
        // testGame

        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        Game fetchedGame = gameService.getGameById(1L);

        assertEquals(testGame.getGameId(), fetchedGame.getGameId());
        assertEquals(testGame.getBoard(), fetchedGame.getBoard());
        assertEquals(testGame.getPlayers(), fetchedGame.getPlayers());
        assertEquals(testGame.getTurnCycle(), fetchedGame.getTurnCycle());
        assertEquals(testGame.getDiceResult(), fetchedGame.getDiceResult());
    }

    @Test
    public void getGame_idDoesntExist_noSuccess() {
        // Assert that trying to get a game by an id that doesn't exist throws a HTTP ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> {       
            Game fetchedGame = gameService.getGameById(2L);         
        } );
    }

    @Test
    public void updateGame_idDoesntExist_noSuccess() {
        // Assert that trying to update a game by an id that doesn't exist throws a HTTP ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> {       
            gameService.updateGame(testGame, 1L);     
        } );
    }

    @Test
    public void deleteGame_idDoesntExist_noSuccess() {
        // Assert that trying to update a game by an id that doesn't exist throws a HTTP ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> {       
            gameService.deleteGame(1L);     
        } );
    }

    @Test
    public void executeRepeatedAttacks_validInput_success() {

        Attack attack = new Attack();
        attack.setAttackingTerritory("Central");
        attack.setDefendingTerritory("Paradeplatz");
        attack.setRepeats(3);
        attack.setTroopsAmount(2);

        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        Game updatedGame = gameService.executeRepeatedAttacks(attack, 1L);

        assertEquals(testGame.getGameId(), updatedGame.getGameId());
        assertEquals(testGame.getBoard(), updatedGame.getBoard());
        assertEquals(testGame.getPlayers(), updatedGame.getPlayers());
        assertEquals(testGame.getTurnCycle(), updatedGame.getTurnCycle());
        assertEquals(testGame.getDiceResult(), updatedGame.getDiceResult());
    }

    @Test
    public void executeRepeatedAttacks_territoryDoesntExist_noSuccess() {
        // Assert that trying to select territories that don't exist throws a HTTP ResponseStatusException
        Attack attack = new Attack();
        attack.setAttackingTerritory("Matterhorn");
        attack.setDefendingTerritory("Säntis");
        attack.setRepeats(3);
        attack.setTroopsAmount(2);
        assertThrows(ResponseStatusException.class, () -> {       
            Game updatedGame = gameService.executeRepeatedAttacks(attack, 1L);    
        } );
    }

    @Test
    public void executeRepeatedAttacks_idDoesntExist_noSuccess() {
        // Assert that trying to update a game by an id that doesn't exist throws a HTTP ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> {       
            gameService.executeRepeatedAttacks(new Attack(), 1L);     
        } );
    }




}
