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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Random;

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
            gameService.updateGame(testGame, 1L, 10L);     
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


    @Test
    public void executeAttack_validInputTwoAttacks_updatedGame() {

        // given territories
        Territory attacking = testGame.getBoard().getTerritories().get(0); //Territory called 'Paradeplatz'
        Territory defending = testGame.getBoard().getTerritories().get(1); //Territory called 'Central'

        // given attack entity
        Attack attack = new Attack();
        attack.setTroopsAmount(3);

        
        Random random = new Random();

        // perform the attack method
        Game afterAttack = gameService.executeAttack(testGame, attack, attacking, defending, random);

        // Check if defender (Central) and attacker (Central) have lost 2 troops in total
        // Since both had 7 troops initially, they should now have 12 troops when added together
        assertEquals(12, afterAttack.getBoard().getTerritories().get(0).getTroops()
        + afterAttack.getBoard().getTerritories().get(1).getTroops());

    }

    @Test
    public void executeAttack_validInputOneAttack_updatedGame() {

        // given territories
        Territory attacking = testGame.getBoard().getTerritories().get(0); //Territory called 'Paradeplatz'
        Territory defending = testGame.getBoard().getTerritories().get(1); //Territory called 'Central'

        // given attack entity whist states to execute only one attack
        Attack attack = new Attack();
        attack.setTroopsAmount(1);

        
        Random random = new Random();

        // perform the attack method
        Game afterAttack = gameService.executeAttack(testGame, attack, attacking, defending, random);

        // Check if defender (Central) or attacker (Central) has lost 1 troop
        // Since both had 7 troops initially, they should now have 13 troops when added together
        assertEquals(13, afterAttack.getBoard().getTerritories().get(0).getTroops()
        + afterAttack.getBoard().getTerritories().get(1).getTroops());


        
    }

    @Test
    public void executeRepeatedAttacks_validInput_updatedGame() {

        // given territories
        Territory attacking = testGame.getBoard().getTerritories().get(0); //Territory called 'Paradeplatz'
        Territory defending = testGame.getBoard().getTerritories().get(1); //Territory called 'Central'

        // given attack entity whist states to execute only one attack
        Attack attack = new Attack();
        attack.setAttackingTerritory("Paradeplatz");
        attack.setDefendingTerritory("Central");
        attack.setRepeats(3);
        attack.setTroopsAmount(2);

        // mock repository
        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        // perform the attack method
        Game afterAttack = gameService.executeRepeatedAttacks(attack, 1L);

        // Check if defender (Central) and attacker (Central) have lost 6 troops in total (3 attacks with 2 troops each)
        // Since both had 7 troops initially, they should now have 8 troops when added together
        assertEquals(8, afterAttack.getBoard().getTerritories().get(0).getTroops()
        + afterAttack.getBoard().getTerritories().get(1).getTroops());

    }

    @Test
    public void getTerritory_validInput_success() {

        //when
        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        Territory territory = gameService.getTerritory(1L,"Paradeplatz");

        //then
        assertEquals(territory.getName(), "Paradeplatz");

    }

    @Test
    public void getTerritory_invalidInput_error() {

        //when
        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        //then
        assertThrows(ResponseStatusException.class, () -> {       
            gameService.getTerritory(1L, "Bellvue");     
        } );

    }

}


