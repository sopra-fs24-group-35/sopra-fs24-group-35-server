package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.service.UserService;

import ch.uzh.ifi.hase.soprafs24.constant.Phase;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Attack;
import ch.uzh.ifi.hase.soprafs24.entity.Board;
import ch.uzh.ifi.hase.soprafs24.entity.CardStack;
import ch.uzh.ifi.hase.soprafs24.entity.CardTrade;
import ch.uzh.ifi.hase.soprafs24.entity.Continent;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.Territory;
import ch.uzh.ifi.hase.soprafs24.entity.TurnCycle;
import ch.uzh.ifi.hase.soprafs24.entity.RiskCard;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Continent;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

import org.hibernate.action.spi.Executable;
import org.hibernate.mapping.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.security.SecureRandom;

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

        // Add 4 territories to the board
        Territory paradeplatz = new Territory();
        paradeplatz.setName("Paradeplatz");
        paradeplatz.setTroops(7);
        paradeplatz.setOwner(7L);
        territories.add(paradeplatz);
        Territory central = new Territory();
        central.setName("Central");
        central.setTroops(7);
        central.setOwner(8L);
        territories.add(central);
        Territory bellevue = new Territory();
        bellevue.setName("Bellevue");
        bellevue.setTroops(7);
        bellevue.setOwner(7L);
        territories.add(bellevue);
        Territory milchbuck = new Territory();
        milchbuck.setName("Milchbuck");
        milchbuck.setTroops(7);
        milchbuck.setOwner(8L);
        territories.add(milchbuck);
        board.setTerritories(territories);

        Continent zurich = new Continent();
        ArrayList<Territory> cont_zurich = new ArrayList<>();
        cont_zurich.add(paradeplatz);
        cont_zurich.add(bellevue);
        zurich.setTerritories(cont_zurich);

        Continent zurich2 = new Continent();
        ArrayList<Territory> cont_zurich2 = new ArrayList<>();
        cont_zurich2.add(central);
        cont_zurich2.add(milchbuck);
        zurich2.setTerritories(cont_zurich2);

        ArrayList<Continent> continents = new ArrayList<>();
        continents.add(zurich);
        continents.add(zurich2);
        board.setContinents(continents);

        // Create 4 Risk Cards, one for each territory; Bellevue and Milchbuck have the same troop type
        CardStack cardStack = new CardStack();
        RiskCard cardParadeplatz = new RiskCard();
        cardParadeplatz.setHandedOut(false);
        cardParadeplatz.setTerritoryName("Paradeplatz");
        cardParadeplatz.setTroops(1);
        cardStack.getRiskCards().add(cardParadeplatz);

        RiskCard cardCentral = new RiskCard();
        cardCentral.setHandedOut(false);
        cardCentral.setTerritoryName("Central");
        cardCentral.setTroops(2);
        cardStack.getRiskCards().add(cardCentral);

        RiskCard cardBellevue = new RiskCard();
        cardBellevue.setHandedOut(false);
        cardBellevue.setTerritoryName("Bellevue");
        cardBellevue.setTroops(3);
        cardStack.getRiskCards().add(cardBellevue);

        RiskCard cardMilchbuck = new RiskCard();
        cardMilchbuck.setHandedOut(false);
        cardMilchbuck.setTerritoryName("Milchbuck");
        cardMilchbuck.setTroops(3);
        cardStack.getRiskCards().add(cardMilchbuck);

        // set turn cycle with two players and player1 as current player
        Player p1 = new Player();
        p1.setPlayerId(7L);
        p1.setCardBonus(0);
        ArrayList<RiskCard> l = new ArrayList<RiskCard>();
        p1.setRiskCards(l);

        Player p2 = new Player();
        p2.setPlayerId(8L);
        p2.setCardBonus(0);
        ArrayList<RiskCard> l2 = new ArrayList<RiskCard>();
        p2.setRiskCards(l2);

        ArrayList<Player> players = new ArrayList<Player>();
        players.add(p1);
        players.add(p2);

        TurnCycle tc = new TurnCycle();
        tc.setCurrentPlayer(p1);
        tc.setPlayerCycle(players);

        // create Game with the board
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setBoard(board);
        testGame.setPlayers(players);
        testGame.setCardStack(cardStack);
        testGame.setTurnCycle(tc);
        testGame.setDiceResult("Atk 1 2 Def 3 4");

        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        


    }

    // CREATE GAME ------------------------------------------------------------------------------------------------------

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

    // GET GAME ------------------------------------------------------------------------------------------------------

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

    // UPDATE GAME ------------------------------------------------------------------------------------------------------

    @Test
    public void updateGame_idDoesntExist_noSuccess() {
        // Assert that trying to update a game by an id that doesn't exist throws a HTTP ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> {       
            gameService.updateGame(testGame, 1L, 10L);     
        } );
    }

    // DELETE GAME ------------------------------------------------------------------------------------------------------

    @Test
    public void deleteGame_idDoesntExist_noSuccess() {
        // Assert that trying to update a game by an id that doesn't exist throws a HTTP ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> {       
            gameService.deleteGame(1L);     
        } );
    }

    // UPDATE BOARD ------------------------------------------------------------------------------------------------------

    @Test
    public void testUpdateBoard_SuccessfulUpdate() {
        // Arrange
        Game newGameState = new Game();
        newGameState.setGameId(1L);
        newGameState.setBoard(testGame.getBoard()); // Assuming similar setup as testGame for simplicity

        // mock the check if game exists
        Mockito.when(gameRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(gameRepository.getByGameId(Mockito.anyLong())).thenReturn(testGame);
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(newGameState);

        // Act
        Game updatedGame = gameService.updateBoard(newGameState, 1L, 1L);

        // Assert
        assertNotNull(updatedGame);
        assertEquals(1L, updatedGame.getGameId());
        assertEquals(testGame.getBoard(), updatedGame.getBoard());
    }

    @Test
    public void testUpdateBoard_GameDoesNotExist() {
        // Arrange
        Game newGameState = new Game();
        newGameState.setGameId(1L);
        newGameState.setBoard(testGame.getBoard()); 
    
        // mock the check if game exists
        Mockito.when(gameRepository.existsById(Mockito.anyLong())).thenReturn(false);
    
        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameService.updateBoard(newGameState, 1L, 1L);
        });
    
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Game deletion failed, because there is no game with this id.", exception.getReason());
    }

    // NEXT PHASE ------------------------------------------------------------------------------------------------------

    @Test
    public void testNextPhase_ReinforcementToAttack() {
        // Arrange
        testGame.getTurnCycle().setCurrentPhase(Phase.REINFORCEMENT);
    
        // Act
        Game updatedGame = gameService.nextPhase(testGame);
    
        // Assert
        assertEquals(Phase.ATTACK, updatedGame.getTurnCycle().getCurrentPhase());
        assertEquals(0, updatedGame.getTurnCycle().getCurrentPlayer().getCardBonus());
    }
        
    @Test
    public void testNextPhase_AttackToMove() {
        // Arrange
        testGame.getTurnCycle().setCurrentPhase(Phase.ATTACK);
    
        // Act
        Game updatedGame = gameService.nextPhase(testGame);
    
        // Assert
        assertEquals(Phase.MOVE, updatedGame.getTurnCycle().getCurrentPhase());
    }


    // GET TERRITORY ---------------------------------------------------------------------------------------------------------

    @Test
    public void testGetTerritory_Success() {

        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        // Act
        Territory territory = gameService.getTerritory(1L, "Paradeplatz");

        // Assert
        assertNotNull(territory);
        assertEquals("Paradeplatz", territory.getName());
        assertEquals(7, territory.getTroops());
    }

    @Test
    public void testGetTerritory_GameNotFound() {
        // Arrange
        Mockito.when(gameRepository.getByGameId(2L)).thenReturn(null);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameService.getTerritory(2L, "Paradeplatz");
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Game deletion failed, because there is no game with this id.", exception.getReason());
    }

    @Test
    public void testGetTerritory_TerritoryNotFound() {

        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameService.getTerritory(1L, "NonExistentTerritory");
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Territory with name NonExistentTerritory not found.", exception.getReason());
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

    // ADD PLAYERS ---------------------------------------------------------------------------------------------------------------

    @Test
    public void testAddPlayers_GameNotFound() {
        // Arrange
        ArrayList<Long> playerIds = new ArrayList<>();
        playerIds.add(9L);
        playerIds.add(10L);

        Mockito.when(gameRepository.getByGameId(2L)).thenReturn(null);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameService.addPlayers(playerIds, 2L);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Game deletion failed, because there is no game with this id.", exception.getReason());
    }
    
    // EXECUTE REPEATED ATTACKS ------------------------------------------------------------------------------------------------------

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
    public void testExecuteRepeatedAttacks_SuccessfulAttack() {
        // Arrange
        Attack attack = new Attack();
        attack.setAttackingTerritory("Paradeplatz");
        attack.setDefendingTerritory("Central");
        attack.setRepeats(3);
        attack.setTroopsAmount(3);

        // Set initial ownership and troops
        Territory paradeplatz = testGame.getBoard().getTerritories().stream()
            .filter(t -> t.getName().equals("Paradeplatz"))
            .findFirst()
            .get();
        paradeplatz.setOwner(7L);
        paradeplatz.setTroops(10);

        Territory central = testGame.getBoard().getTerritories().stream()
            .filter(t -> t.getName().equals("Central"))
            .findFirst()
            .get();
        central.setOwner(8L);
        central.setTroops(5);

        // mock the get game from repository
        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        // Act
        Game resultGame = gameService.executeRepeatedAttacks(attack, 1L);

        // Assert
        assertNotNull(resultGame);
        assertTrue(central.getTroops() > 0);  // Troops should be transferred
        assertTrue(paradeplatz.getTroops() >= 1); // Attacking territory should have at least 1 troop left
    }

    @Test
    public void testExecuteRepeatedAttacks_GameDoesNotExist() {
        // Arrange
        Attack attack = new Attack();
        attack.setAttackingTerritory("Paradeplatz");
        attack.setDefendingTerritory("Central");
        attack.setRepeats(3);
        attack.setTroopsAmount(3);
    
        // mock game does not exist
        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(null);
    
        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> gameService.executeRepeatedAttacks(attack, 1L));
    }

    @Test
    public void testExecuteRepeatedAttacks_AttackingTerritoryNotFound() {
        // Arrange
        Attack attack = new Attack();
        attack.setAttackingTerritory("UnknownTerritory");
        attack.setDefendingTerritory("Central");
        attack.setRepeats(3);
        attack.setTroopsAmount(3);
    
        // mock the get game from repository
        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);
    
        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> gameService.executeRepeatedAttacks(attack, 1L));
    }

    @Test
    public void testExecuteRepeatedAttacks_DefendingTerritoryNotFound() {
        // Arrange
        Attack attack = new Attack();
        attack.setAttackingTerritory("Paradeplatz");
        attack.setDefendingTerritory("UnknownTerritory");
        attack.setRepeats(3);
        attack.setTroopsAmount(3);
    
        // mock the get game from repository
        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);
    
        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> gameService.executeRepeatedAttacks(attack, 1L));
    }

    @Test
    public void testExecuteRepeatedAttacks_AttackWithNotEnoughTroops() {
        // Arrange
        Attack attack = new Attack();
        attack.setAttackingTerritory("Paradeplatz");
        attack.setDefendingTerritory("Central");
        attack.setRepeats(3);
        attack.setTroopsAmount(3);
    
        // Set initial ownership and troops
        Territory paradeplatz = testGame.getBoard().getTerritories().stream()
            .filter(t -> t.getName().equals("Paradeplatz"))
            .findFirst()
            .get();
        paradeplatz.setOwner(7L);
        paradeplatz.setTroops(2);
    
        Territory central = testGame.getBoard().getTerritories().stream()
            .filter(t -> t.getName().equals("Central"))
            .findFirst()
            .get();
        central.setOwner(8L);
        central.setTroops(5);
    
        // mock the get game from repository
        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);
    
        // Act
        Game resultGame = gameService.executeRepeatedAttacks(attack, 1L);
    
        // Assert
        assertNotNull(resultGame);
        assertEquals(8L, central.getOwner()); // Ownership should not be transferred
        assertTrue(central.getTroops() > 0);  // Defending territory should still have troops
        assertTrue(paradeplatz.getTroops() >= 1); // Attacking territory should have at least 1 troop left
    }

    // TRANSFER TROOPS ------------------------------------------------------------------------------------------------------

        @Test
    public void testTransferTroopsSuccessful() {
        Attack attack = new Attack();
        attack.setAttackingTerritory("Paradeplatz");
        attack.setDefendingTerritory("Central");
        attack.setTroopsAmount(3);
        attack.setRepeats(1);
        Long gameId = 1L;

        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        Game result = gameService.transferTroops(attack, gameId);

        assertNotNull(result);
        Territory attackingTerritory = result.getBoard().getTerritories().stream()
                .filter(t -> t.getName().equals("Paradeplatz"))
                .findFirst()
                .orElse(null);
        Territory defendingTerritory = result.getBoard().getTerritories().stream()
                .filter(t -> t.getName().equals("Central"))
                .findFirst()
                .orElse(null);

        assertNotNull(attackingTerritory);
        assertNotNull(defendingTerritory);
        assertEquals(4, attackingTerritory.getTroops());
        assertEquals(10, defendingTerritory.getTroops());
    }

    @Test
    public void testGameNotFound() {
        Attack attack = new Attack();
        attack.setAttackingTerritory("Paradeplatz");
        attack.setDefendingTerritory("Central");
        attack.setTroopsAmount(3);
        attack.setRepeats(1);
        Long gameId = 2L;

        when(gameRepository.getByGameId(gameId)).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameService.transferTroops(attack, gameId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("No game with this id could be found.", exception.getReason());
    }

    @Test
    public void testAttackingTerritoryNotFound() {
        Attack attack = new Attack();
        attack.setAttackingTerritory("NonExistent1");
        attack.setDefendingTerritory("Central");
        attack.setTroopsAmount(3);
        attack.setRepeats(1);
        Long gameId = 1L;

        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        Game result = gameService.transferTroops(attack, gameId);

        assertNotNull(result);
        Territory attackingTerritory = result.getBoard().getTerritories().stream()
                .filter(t -> t.getName().equals("Paradeplatz"))
                .findFirst()
                .orElse(null);
        Territory defendingTerritory = result.getBoard().getTerritories().stream()
                .filter(t -> t.getName().equals("Central"))
                .findFirst()
                .orElse(null);

        assertNotNull(attackingTerritory);
        assertNotNull(defendingTerritory);
        assertEquals(7, attackingTerritory.getTroops());
        assertEquals(7, defendingTerritory.getTroops());
    }

    @Test
    public void testDefendingTerritoryNotFound() {
        Attack attack = new Attack();
        attack.setAttackingTerritory("Paradeplatz");
        attack.setDefendingTerritory("NonExistent2");
        attack.setTroopsAmount(3);
        attack.setRepeats(1);
        Long gameId = 1L;

        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        Game result = gameService.transferTroops(attack, gameId);

        assertNotNull(result);
        Territory attackingTerritory = result.getBoard().getTerritories().stream()
                .filter(t -> t.getName().equals("Paradeplatz"))
                .findFirst()
                .orElse(null);
        Territory defendingTerritory = result.getBoard().getTerritories().stream()
                .filter(t -> t.getName().equals("Central"))
                .findFirst()
                .orElse(null);

        assertNotNull(attackingTerritory);
        assertNotNull(defendingTerritory);
        assertEquals(7, attackingTerritory.getTroops());
        assertEquals(7, defendingTerritory.getTroops());
    }

    @Test
    public void testBothTerritoriesNotFound() {
        Attack attack = new Attack();
        attack.setAttackingTerritory("NonExistent1");
        attack.setDefendingTerritory("NonExistent2");
        attack.setTroopsAmount(3);
        attack.setRepeats(1);
        Long gameId = 1L;

        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        Game result = gameService.transferTroops(attack, gameId);

        assertNotNull(result);
        Territory attackingTerritory = result.getBoard().getTerritories().stream()
                .filter(t -> t.getName().equals("Paradeplatz"))
                .findFirst()
                .orElse(null);
        Territory defendingTerritory = result.getBoard().getTerritories().stream()
                .filter(t -> t.getName().equals("Central"))
                .findFirst()
                .orElse(null);

        assertNotNull(attackingTerritory);
        assertNotNull(defendingTerritory);
        assertEquals(7, attackingTerritory.getTroops());
        assertEquals(7, defendingTerritory.getTroops());
    }

    // EXECUTE ATTACK ------------------------------------------------------------------------------------------------------

    @Test
    public void executeAttack_validInputTwoAttacks_updatedGame() {

        // given territories
        Territory attacking = testGame.getBoard().getTerritories().get(0); //Territory called 'Paradeplatz'
        Territory defending = testGame.getBoard().getTerritories().get(1); //Territory called 'Central'

        // given attack entity
        Attack attack = new Attack();
        attack.setTroopsAmount(3);

        
        SecureRandom random = new SecureRandom();

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

        
        SecureRandom random = new SecureRandom();

        // perform the attack method
        Game afterAttack = gameService.executeAttack(testGame, attack, attacking, defending, random);

        // Check if defender (Central) or attacker (Central) has lost 1 troop
        // Since both had 7 troops initially, they should now have 13 troops when added together
        assertEquals(13, afterAttack.getBoard().getTerritories().get(0).getTroops()
        + afterAttack.getBoard().getTerritories().get(1).getTroops());


        
    }

    // LEAVE GAME -----------------------------------------------------------------------------------------------------

        @Test
    public void testLeaveGameSuccessful() {
        Long gameId = 1L;
        Long lobbyId = 1L;
        Long userId = 7L;

        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        gameService.leaveGame(gameId, lobbyId, userId);

        Game game = gameRepository.getByGameId(gameId);
        assertNotNull(game);

        boolean playerExists = game.getTurnCycle().getPlayerCycle().stream()
            .anyMatch(player -> player.getPlayerId().equals(userId));

        assertFalse(playerExists);
        verify(gameRepository, times(1)).save(game);
    }

    @Test
    public void testLeaveGame_GameNotFound() {
        Long gameId = 2L;
        Long lobbyId = 1L;
        Long userId = 7L;

        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameService.leaveGame(gameId, lobbyId, userId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("No game with this id could be found.", exception.getReason());
    }

    @Test
    public void testPlayerNotFound() {
        Long gameId = 1L;
        Long lobbyId = 1L;
        Long userId = 10L; // Non-existing player ID

        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        Game game = gameRepository.getByGameId(gameId);
        assertNotNull(game);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameService.leaveGame(gameId, lobbyId, userId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("No player with this id could be found.", exception.getReason());
    }

    @Test
    public void testCurrentPlayerLeavesGame() {
        Long gameId = 1L;
        Long lobbyId = 1L;
        Long userId = 7L;

        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        gameService.leaveGame(gameId, lobbyId, userId);

        Game game = gameRepository.getByGameId(gameId);
        assertNotNull(game);

        Player currentPlayer = game.getTurnCycle().getCurrentPlayer();
        assertNotEquals(userId, currentPlayer.getPlayerId());
        assertEquals(Phase.REINFORCEMENT, game.getTurnCycle().getCurrentPhase());
        verify(gameRepository, times(1)).save(game);
    }

    // PULL CARD ------------------------------------------------------------------------------------------------------

    @Test
    public void pullCard_validInput_updatedGame() {

        // mock the get game from repository
        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        // check that current player has no card
        assertTrue(testGame.getTurnCycle().getCurrentPlayer().getRiskCards().size() == 0);

        // perform the pull card method
        Game afterCardPull = gameService.pullCard(1L);

        // check that player has a card now
        assertTrue(afterCardPull.getTurnCycle().getCurrentPlayer().getRiskCards().size() > 0);
        assertTrue(afterCardPull.getTurnCycle().getCurrentPlayer().getRiskCards().get(0).getTroops() > 0);
        
    }

    // TRADE CARDS ------------------------------------------------------------------------------------------------------

    @Test
    public void testTradeCards_SuccessfulTrade() {
        // Arrange
        CardTrade cardTrade = new CardTrade();
        cardTrade.setCard1Name("Paradeplatz");
        cardTrade.setCard2Name("Central");
        cardTrade.setCard3Name("Bellevue");

        // mock the get game from repository
        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        // Act
        Game tradedGame = gameService.tradeCards(1L, cardTrade, 3);

        // Assert
        // Verify that the currentPlayer's card bonus is increased by 2
        assert tradedGame.getTurnCycle().getCurrentPlayer().getCardBonus() >= 4;
        assert tradedGame.getTurnCycle().getCurrentPlayer().getCardBonus() <= 16;

        // Verify that the cards are removed from the currentPlayer's risk cards
        assert tradedGame.getTurnCycle().getCurrentPlayer().getRiskCards().isEmpty();
    }

    @Test
    public void testTradeCards_invalidCardName_InvalidTrade() {
        // Arrange
        CardTrade cardTrade = new CardTrade();
        cardTrade.setCard1Name("Paradeplatz");
        cardTrade.setCard2Name("Bellevue");
        cardTrade.setCard3Name("FairyTaleWorld"); // this card name is invalid

        // mock the get game from repository
        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> gameService.tradeCards(1L, cardTrade, 3));
    }

    @Test
    public void testTradeCards_invalidCardCombination_InvalidTrade() {
        // Arrange
        CardTrade cardTrade = new CardTrade();
        cardTrade.setCard1Name("Paradeplatz");
        cardTrade.setCard2Name("Bellevue");
        cardTrade.setCard3Name("Milchbuck"); //has the same troop type as Bellevue and thus is an invalid trade

        // mock the get game from repository
        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> gameService.tradeCards(1L, cardTrade, 3));
    }

    @Test
    public void testTradeCards_GameDoesNotExist() {
        // Arrange
        CardTrade cardTrade = new CardTrade();
        cardTrade.setCard1Name("Paradeplatz");
        cardTrade.setCard2Name("Central");
        cardTrade.setCard3Name("Bellevue");

        // mock game does not exist
        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(null);

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> gameService.tradeCards(1L, cardTrade, 3));
    }

    @Test
    public void testTradeCards_EmptyCardStack() {
        // Arrange
        CardTrade cardTrade = new CardTrade();
        cardTrade.setCard1Name("Paradeplatz");
        cardTrade.setCard2Name("Central");
        cardTrade.setCard3Name("Bellevue");

        // Empty the card stack
        testGame.getCardStack().getRiskCards().clear();

        // mock the get game from repository
        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> gameService.tradeCards(1L, cardTrade, 0));
    }

    @Test
    public void testTradeCards_AllDifferentCards() {
        // Arrange
        CardTrade cardTrade = new CardTrade();
        cardTrade.setCard1Name("Paradeplatz");
        cardTrade.setCard2Name("Central");
        cardTrade.setCard3Name("Bellevue");

        // mock the get game from repository
        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        // Act
        Game tradedGame = gameService.tradeCards(1L, cardTrade, 3);

        // Assert
        assertEquals(12, tradedGame.getTurnCycle().getCurrentPlayer().getCardBonus());
        assertTrue(tradedGame.getTurnCycle().getCurrentPlayer().getRiskCards().isEmpty());
    }

    @Test
    public void transferTroops_validInput() {
        //given
        Territory attacking = testGame.getBoard().getTerritories().get(0); //Territory called 'Paradeplatz'
        Territory defending = testGame.getBoard().getTerritories().get(1); //Territory called 'Central'

        //attack
        Attack attack = new Attack();
        attack.setAttackingTerritory("Paradeplatz");
        attack.setDefendingTerritory("Central");
        attack.setTroopsAmount(2);

        // mock repository
        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        //perform transfer
        Game afterTransfer = gameService.transferTroops(attack, testGame.getGameId());

        assertEquals(5, afterTransfer.getBoard().getTerritories().get(0).getTroops());
        assertEquals(9, afterTransfer.getBoard().getTerritories().get(1).getTroops());
    }

    @Test
    public void transferTroops_validInput_tooManyTroops() {
        //given
        //affected territories
        Territory attacking = testGame.getBoard().getTerritories().get(0); //Territory called 'Paradeplatz'
        Territory defending = testGame.getBoard().getTerritories().get(1); //Territory called 'Central'

        //attack
        Attack attack = new Attack();
        attack.setAttackingTerritory("Paradeplatz");
        attack.setDefendingTerritory("Central");
        attack.setTroopsAmount(10);

        // mock repository
        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        //perform transfer
        Game afterTransfer = gameService.transferTroops(attack, testGame.getGameId());

        assertEquals(1, afterTransfer.getBoard().getTerritories().get(0).getTroops());
        assertEquals(13, afterTransfer.getBoard().getTerritories().get(1).getTroops());
    }

    @Test
    public void leaveGame_validInput_notCurrentPlayer() {
        //given
        //playerToLeave
        Player player2 = testGame.getPlayers().get(1);

        //lobby Id
        Long lobbyId = 20L;

        //mock repository
        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        //perform leave
        gameService.leaveGame(testGame.getGameId(), lobbyId, player2.getPlayerId());

        assertEquals(1, testGame.getTurnCycle().getPlayerCycle().size());
    }

    @Test
    public void leaveGame_validInput_CurrentPlayer() {
        //given
        //playerToLeave
        Player player1 = testGame.getPlayers().get(0);

        //new current player after
        Player player2 = testGame.getPlayers().get(1);

        //lobby Id
        Long lobbyId = 20L;

        //distribute territories so no error happens
        testGame.getBoard().getTerritories().get(0).setOwner(player1.getPlayerId());
        testGame.getBoard().getTerritories().get(1).setOwner(player1.getPlayerId());
        testGame.getBoard().getTerritories().get(2).setOwner(player2.getPlayerId());
        testGame.getBoard().getTerritories().get(3).setOwner(player2.getPlayerId());

        //create a continent with all territories
        Continent continent = new Continent();
        continent.getTerritories().add(testGame.getBoard().getTerritories().get(0));
        continent.getTerritories().add(testGame.getBoard().getTerritories().get(1));
        continent.getTerritories().add(testGame.getBoard().getTerritories().get(2));
        continent.getTerritories().add(testGame.getBoard().getTerritories().get(3));
        testGame.getBoard().getContinents().add(continent);

        //mock repository
        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        //perform leave
        gameService.leaveGame(testGame.getGameId(), lobbyId, player1.getPlayerId());

        assertEquals(1, testGame.getTurnCycle().getPlayerCycle().size());
        assertEquals(player2, testGame.getTurnCycle().getCurrentPlayer());
    }

}