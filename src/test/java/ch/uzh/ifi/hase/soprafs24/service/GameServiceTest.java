package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Attack;
import ch.uzh.ifi.hase.soprafs24.entity.Board;
<<<<<<< HEAD
import ch.uzh.ifi.hase.soprafs24.entity.CardStack;
import ch.uzh.ifi.hase.soprafs24.entity.CardTrade;
=======
>>>>>>> 418582e77a04997cb9ebc62088814b54745433e5
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.Territory;
import ch.uzh.ifi.hase.soprafs24.entity.TurnCycle;
import ch.uzh.ifi.hase.soprafs24.entity.RiskCard;
import ch.uzh.ifi.hase.soprafs24.entity.User;
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
<<<<<<< HEAD

        // Add 4 territories to the board
=======
>>>>>>> 418582e77a04997cb9ebc62088814b54745433e5
        Territory paradeplatz = new Territory();
        paradeplatz.setName("Paradeplatz");
        paradeplatz.setTroops(7);
        territories.add(paradeplatz);
        Territory central = new Territory();
        central.setName("Central");
        central.setTroops(7);
        territories.add(central);
<<<<<<< HEAD
        Territory bellevue = new Territory();
        bellevue.setName("Bellevue");
        bellevue.setTroops(7);
        territories.add(bellevue);
        Territory milchbuck = new Territory();
        milchbuck.setName("Milchbuck");
        milchbuck.setTroops(7);
        territories.add(milchbuck);
        board.setTerritories(territories);

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
        ArrayList<RiskCard> l = new ArrayList<RiskCard>();
        p1.setRiskCards(l);

        Player p2 = new Player();
        p2.setPlayerId(8L);
        ArrayList<RiskCard> l2 = new ArrayList<RiskCard>();
        p2.setRiskCards(l2);

        ArrayList<Player> players = new ArrayList<Player>();
        players.add(p1);
        players.add(p2);

        TurnCycle tc = new TurnCycle();
        tc.setCurrentPlayer(p1);
        tc.setPlayerCycle(players);

        // create Game with the board
=======
        board.setTerritories(territories);

        // create Game with this board
>>>>>>> 418582e77a04997cb9ebc62088814b54745433e5
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setBoard(board);
        testGame.setPlayers(null);
<<<<<<< HEAD
        testGame.setCardStack(cardStack);
        testGame.setTurnCycle(tc);
=======
        testGame.setTurnCycle(null);
>>>>>>> 418582e77a04997cb9ebc62088814b54745433e5
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

<<<<<<< HEAD
    @Test
    public void pullCard_validInput_updatedGame() {

        // mock the get game from repository
        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);

        // check that current player has no card
=======
    //@Test
    public void pullCard_validInput_updatedGame() {

        // set turn cycle with two players
        Player p1 = new Player();
        p1.setPlayerId(7L);
        ArrayList<RiskCard> l = new ArrayList<RiskCard>();
        p1.setRiskCards(l);

        Player p2 = new Player();
        p2.setPlayerId(8L);
        ArrayList<RiskCard> l2 = new ArrayList<RiskCard>();
        p2.setRiskCards(l2);

        ArrayList<Player> players = new ArrayList<Player>();
        players.add(p1);
        players.add(p2);

        TurnCycle tc = new TurnCycle();
        tc.setCurrentPlayer(p1);
        tc.setPlayerCycle(players);
        testGame.setTurnCycle(tc);

        // mock the get game from repository
        Mockito.when(gameRepository.getByGameId(Mockito.any())).thenReturn(testGame);
        //Mockito.when(territories.get(Mockito.any()))

        // check that player has no card
>>>>>>> 418582e77a04997cb9ebc62088814b54745433e5
        assertTrue(testGame.getTurnCycle().getCurrentPlayer().getRiskCards().size() == 0);

        // perform the pull card method
        Game afterCardPull = gameService.pullCard(1L);

        // check that player has a card now
        assertTrue(afterCardPull.getTurnCycle().getCurrentPlayer().getRiskCards().size() > 0);
        assertTrue(afterCardPull.getTurnCycle().getCurrentPlayer().getRiskCards().get(0).getTroops() > 0);
<<<<<<< HEAD
=======


>>>>>>> 418582e77a04997cb9ebc62088814b54745433e5
        
    }

    @Test
<<<<<<< HEAD
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
        assert tradedGame.getTurnCycle().getCurrentPlayer().getCardBonus() == 4;

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
=======
>>>>>>> 418582e77a04997cb9ebc62088814b54745433e5
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


