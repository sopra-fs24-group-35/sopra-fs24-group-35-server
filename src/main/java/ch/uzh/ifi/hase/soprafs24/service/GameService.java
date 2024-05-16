package ch.uzh.ifi.hase.soprafs24.service;


import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import java.util.HashMap;
import javax.persistence.Id;
import javax.transaction.Transactional;

import org.hibernate.mapping.Collection;
import org.hibernate.mapping.IdGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

import ch.uzh.ifi.hase.soprafs24.entity.Board;
import ch.uzh.ifi.hase.soprafs24.entity.CardStack;
import ch.uzh.ifi.hase.soprafs24.entity.CardTrade;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.RiskCard;
import ch.uzh.ifi.hase.soprafs24.entity.Continent;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.Territory;
import ch.uzh.ifi.hase.soprafs24.entity.TurnCycle;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.Random;

import ch.uzh.ifi.hase.soprafs24.constant.Phase;
import ch.uzh.ifi.hase.soprafs24.entity.Attack;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;

@Service
@Transactional
public class GameService {
    
    private final Logger log = LoggerFactory.getLogger(GameService.class);

    @Autowired
    private Validator validator;

    private final GameRepository gameRepository;
    private final LobbyService lobbyService;

    private UserService userService;

    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, @Qualifier("lobbyRepository") LobbyRepository lobbyRepository, UserService userService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
        this.lobbyService = new LobbyService(lobbyRepository);
    }

    public void checkAuthorization(Long lobby_id, String token) {
        lobbyService.checkAuthorization(lobby_id, token);
    }

    public Lobby startGame(Long lobby_id, Long game_id){
        return lobbyService.startGame(lobby_id, game_id);
    }

    public void endGame(Long lobby_id){
        lobbyService.endGame(lobby_id);
    }
    
    public void checkIfLobbyExists(long lobbyId) {
        lobbyService.checkIfExists(lobbyId);
    }

    public Game getGameById(Long gameId) {
        boolean exists = checkIfGameExists(gameId, true);
        if (!exists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            "No game with this id could be found.");
        }
        log.debug("Sent out game information");
        return this.gameRepository.getByGameId(gameId);
    }

    public Game createGame(Game newGame) {
        
        // do game initialization
        Game initializedGame = initializeGame(newGame);

        // save to repository
        initializedGame = gameRepository.save(initializedGame);
        gameRepository.flush();

        log.debug("Created a Game");
        return initializedGame;
    }

    public Game updateGame(Game updatedGame, Long gameId, Long lobbyId) {

        // throww error if game with the given id doesn't exist
        boolean exists = checkIfGameExists(gameId, true);
        if (!exists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game update failed, because there is no game with this id.");
        }

        // update game
        updatedGame = doConsequences(updatedGame, gameRepository.getByGameId(gameId));

        // update Turn Cycle:
        updatedGame = nextPhase(updatedGame);

        // If reinforcement phase, then distribute troops to current player
        if (updatedGame.getTurnCycle().getCurrentPhase() == Phase.REINFORCEMENT) {
            updatedGame = distributeTroops(updatedGame, updatedGame.getTurnCycle().getCurrentPlayer().getPlayerId());
        } //if player haas no territories remove him
        else if (updatedGame.getTurnCycle().getCurrentPhase() == Phase.MOVE) {
            int territoriesOwned = 0;
            List<Player> toRemove = new ArrayList<Player>();
            for (Player player : updatedGame.getTurnCycle().getPlayerCycle()) {
                toRemove.add(player);
            }
            for (Player player : toRemove) {
                territoriesOwned = 0;
                for (Territory territory : updatedGame.getBoard().getTerritories()) {
                    if (territory.getOwner() == player.getPlayerId()){
                        territoriesOwned++;
                    }
                }

                if (territoriesOwned == 0) {
                    leaveGame(gameId, lobbyId, player.getPlayerId());
                }
            }
        }

        // save updated game to repository
        updatedGame = gameRepository.save(updatedGame);
        gameRepository.flush();

        log.debug("Updated a Game");
        return updatedGame;
    }

    public Game updateBoard(Game game, Long gameId, Long lobbyId){

        // throw error if game with given id doesn't exist
        boolean exists = checkIfGameExists(gameId, true);
        if (!exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Game deletion failed, because there is no game with this id.");
        }

        Game oldGame = getGameById(gameId);

        //overwrite old game with new state
        game = doConsequences(game, oldGame);

        return game;
    }

    public void deleteGame(Long gameId) {

        // throw error if game with given id doesn't exist
        boolean exists = checkIfGameExists(gameId, true);
        if (!exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Game deletion failed, because there is no game with this id.");
        }

        // delete game
        gameRepository.deleteById(gameId);
        gameRepository.flush();

        log.debug("Deleted a Game");
        return;
    }

    private Game nextPhase(Game game) {
        Phase phase = game.getTurnCycle().getCurrentPhase();

        // update phase
        game.getTurnCycle().setCurrentPhase(phase.next());

        // if it's a new player's turn, update TurnCycle
        if (game.getTurnCycle().getCurrentPhase() == Phase.REINFORCEMENT) {
            Player currentPlayer = game.getTurnCycle().getCurrentPlayer();

            // resrt card bonus of current player
            currentPlayer.setCardBonus(0);

            // add a Risk card to the old player if he awaits one
            if (currentPlayer.getAwaitsCard() == true) {
                pullCard(game.getGameId());
                currentPlayer.setAwaitsCard(false);
            }

            // change currentPlayer to the new player
            Player nextPlayer = nextPlayer(currentPlayer, game.getTurnCycle());
            game.getTurnCycle().setCurrentPlayer(nextPlayer);
        }
        if (game.getTurnCycle().getCurrentPhase() == Phase.ATTACK) {

            // reset card bonus of player after reinforcement phase has finished
            Player currentPlayer = game.getTurnCycle().getCurrentPlayer();
            currentPlayer.setCardBonus(0);
            
        }
        return game;
    }

    private Player nextPlayer(Player currentPlayer, TurnCycle turnCycle) {
        int position = 0;

        // find current player in player cycle array
        for (Player player : turnCycle.getPlayerCycle()) {
            if (player.getPlayerId() == currentPlayer.getPlayerId()){
                break;
            } else {
                position += 1;
            }
        }

        Player nextPlayer;
        // get tha next player
        if ((position + 1) > turnCycle.getPlayerCycle().size()-1){
            nextPlayer = turnCycle.getPlayerCycle().get(0);
        } else {
            nextPlayer = turnCycle.getPlayerCycle().get(position+1);
        }

        // set gotACard to false
        turnCycle.setGotACard(false);
        return nextPlayer;
    }

    public Territory getTerritory(Long gameId, String territoryName){
        boolean exists = checkIfGameExists(gameId, true);
        if (!exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Game deletion failed, because there is no game with this id.");
        }

        Game game = getGameById(gameId);
        for (Territory territory : game.getBoard().getTerritories()) {
            if (territory.getName().equals(territoryName)){
                return territory;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Territory with name %s not found.", territoryName));

    }

    public Game addPlayers(ArrayList<Long> players, Long gameId){
        boolean exists = checkIfGameExists(gameId, true);
        if (!exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Game deletion failed, because there is no game with this id.");
        }
        //Get game
        Game updatedGame = getGameById(gameId);
        
        //Create players from their id and add them to player list
        for(Long id: players){
            Player player = new Player();
            player.setPlayerId(id);
            player.setUsername(userService.getUserById(id).getUsername());
            player.setAvatarId(userService.getUserById(id).getAvatarId());
            player.setCardBonus(0);
            player.setAwaitsCard(false);
            updatedGame.addPlayers(player);
        }



        //Save Game in repository
        updatedGame = gameRepository.save(updatedGame);
        gameRepository.flush();

        return updatedGame;
    }

    public Game executeRepeatedAttacks(Attack attack, Long gameId) {
        boolean exists = checkIfGameExists(gameId, true);
        if (!exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "No game with this id could be found.");
        }

        // get game from repository
        Game game = this.gameRepository.getByGameId(gameId);

        // find territories of interest in the game entity
        Territory attackingTerritory = null;
        Territory defendingTerritory = null;
        for (int i = 0; i < game.getBoard().getTerritories().size(); i++) {
            if (game.getBoard().getTerritories().get(i).getName().equals(attack.getAttackingTerritory())) {
                attackingTerritory = game.getBoard().getTerritories().get(i);
            } else if (game.getBoard().getTerritories().get(i).getName().equals(attack.getDefendingTerritory())) {
                defendingTerritory = game.getBoard().getTerritories().get(i);
            }
        }

        // check if both territories are found
        if (attackingTerritory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            "No attacking territory with the given name exists.");
        }
        if (defendingTerritory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            "No defending territory with the given name exists.");
        }

        //save attacking and defending player in variable
        Player defendingPlayer = new Player();
        Player attackingPlayer = new Player();
        for (Player player : game.getPlayers()) {
            if (defendingTerritory.getOwner() == player.getPlayerId()){
                defendingPlayer = player;
            }

            if (attackingTerritory.getOwner() == player.getPlayerId()){
                attackingPlayer = player;
            }
        }

        // repeat attacks as many times as 'repeat' specifies, but only do it as long as there are enough troops on both territories.
        String diceResult = "";
        game.setDiceResult(diceResult);
        for (int i = 0; i < attack.getRepeats(); i++) {
            if (defendingTerritory.getTroops() > 0 && attackingTerritory.getTroops() > 1) {
                game = executeAttack(game, attack, attackingTerritory, defendingTerritory, new Random());
            }
        }

        // if attacker has beaten all troops of the defending territory, he gets ownership of it and attacking troops are transferred to it
        int troopsFromAtk = Math.min(attackingTerritory.getTroops() - 1, Math.min(attack.getTroopsAmount(), 3));
        if (defendingTerritory.getTroops() <= 0) {
            defendingTerritory.setOwner(attackingTerritory.getOwner());
            defendingTerritory.setTroops(troopsFromAtk);
            attackingTerritory.setTroops(attackingTerritory.getTroops() - troopsFromAtk);

            // now set the player that he awaits a risk card at the end of the turn
            game.getTurnCycle().getCurrentPlayer().setAwaitsCard(true);

            //check how many territories defender still owns
            int ownedTerritories = 0;
            for (Territory territory : game.getBoard().getTerritories()) {
                if (territory.getOwner() == defendingPlayer.getPlayerId()){
                    ownedTerritories++;
                }
            }

            //removes all RiskCards from the defending player and adds them to the attacking if the defender has zero territories
            if (ownedTerritories == 0) {
                if (defendingPlayer.getRiskCards().size() > 0) {

                    for (RiskCard card : defendingPlayer.getRiskCards()) {
                        //defendingPlayer.getRiskCards().remove(card);
                        attackingPlayer.getRiskCards().add(card);
                    }

                    defendingPlayer.setRiskCards(null);
                }
            }
            
        }

        // Now save the adjusted territories to the repository
        gameRepository.save(game);
        gameRepository.flush();

        return game;
    }

    public Game transferTroops(Attack attack, Long gameId){
        boolean exists = checkIfGameExists(gameId, true);
        if (!exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "No game with this id could be found.");
        }

        // get game from repository
        Game game = this.gameRepository.getByGameId(gameId);

        // find territories of interest in the game entity
        Territory attackingTerritory = null;
        Territory defendingTerritory = null;
        for (int i = 0; i < game.getBoard().getTerritories().size(); i++) {
            if (game.getBoard().getTerritories().get(i).getName().equals(attack.getAttackingTerritory())) {
                attackingTerritory = game.getBoard().getTerritories().get(i);
            } else if (game.getBoard().getTerritories().get(i).getName().equals(attack.getDefendingTerritory())) {
                defendingTerritory = game.getBoard().getTerritories().get(i);
            }
        }

        int transferingTroops = Math.min(attackingTerritory.getTroops()-1, attack.getTroopsAmount());

        attackingTerritory.setTroops(attackingTerritory.getTroops() - transferingTroops);
        defendingTerritory.setTroops(defendingTerritory.getTroops() + transferingTroops);

        gameRepository.save(game);
        gameRepository.flush();

        return game;
    }

    private Game distributeTroops(Game game, Long playerId) {
        Board board = game.getBoard();
        int count = 0;
        for (Territory territory : board.getTerritories()) {
            if (territory.getOwner() == playerId) {
                count++;
            }
        }
        for (Player player : game.getPlayers()) {
            if (player.getPlayerId() ==  playerId) {
                player.setTroopBonus(Math.max(count/3, 3)); // set bonus to number of owned territories/3 and minimum 3
                
                int territoriesOwned;
                int size;
                //Add the continent bonus if player has all territories
                for (Continent continent : board.getContinents()) {
                    size = continent.getTerritories().size();
                    territoriesOwned = 0;
                    
                    //Check ownership of each territory
                    for (Territory territory : continent.getTerritories()) {
                        if (territory.getOwner() == playerId){
                            territoriesOwned ++;
                        }
                    }
                    if (territoriesOwned == size){
                        player.setTroopBonus(player.getTroopBonus() + continent.getAdditionalTroopBonus());
                        
                    }
                }
            }
        }
        return game;
    }
    
    public Game tradeCards(Long gameId, CardTrade cardTrade, int cardStackSize) {

        boolean exists = checkIfGameExists(gameId, true);
        if (!exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "No game with this id could be found.");
        }

        // get game from repository
        Game game = this.gameRepository.getByGameId(gameId);

        // get the names of the three cards
        String card1Name = cardTrade.getCard1Name();
        String card2Name = cardTrade.getCard2Name();
        String card3Name = cardTrade.getCard3Name();

        // get first card in the card stack
        RiskCard card1 = null;
        for (int i = 0; i < cardStackSize; i++) {
            if (game.getCardStack().getRiskCards().get(i).getTerritoryName().equals(card1Name)) {
                card1 = game.getCardStack().getRiskCards().get(i);
                break;
            }
        }
        if (card1 == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "Card trade failed. The name of the first of the three traded-in cards cannot be found in the stack.");
        }

        // get second card in the card stack
        RiskCard card2 = null;
        for (int i = 0; i < cardStackSize; i++) {
            if (game.getCardStack().getRiskCards().get(i).getTerritoryName().equals(card2Name)) {
                card2 = game.getCardStack().getRiskCards().get(i);
                break;
            }
        }
        if (card2 == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "Card trade failed. The name of the second of the three traded-in cards cannot be found in the stack.");
        }

        // get third card in the card stack
        RiskCard card3 = null;
        for (int i = 0; i < cardStackSize; i++) {
            if (game.getCardStack().getRiskCards().get(i).getTerritoryName().equals(card3Name)) {
                card3 = game.getCardStack().getRiskCards().get(i);
                break;
            }
        }
        if (card3 == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "Card trade failed. The name of the third of the three traded-in cards cannot be found in the stack.");
        }

        // check if the trade is valid
        if (((card1.getTroops() == card2.getTroops() || card1.getTroops() == 0 || card2.getTroops() == 0) 
            && (card2.getTroops() == card3.getTroops() || card2.getTroops() == 0 || card3.getTroops() == 0))
            || ((card1.getTroops() != card2.getTroops() || card1.getTroops() == 0 || card2.getTroops() == 0)
            && (card2.getTroops() != card3.getTroops() || card1.getTroops() == 0 || card2.getTroops() == 0))) {

            Player currentPlayer = game.getTurnCycle().getCurrentPlayer();

            // calculte card bonus
            // set values for calculation (we use a mathematical trick with modulo to determine bonusses)
            int cSum = 0;
            if(card1.getTroops() == 1) {cSum += 1;}
            else if(card1.getTroops() == 2) {cSum += 4;}
            else if(card1.getTroops() == 3) {cSum += 13;}
            else {cSum += 0;}
            if(card2.getTroops() == 1) {cSum += 1;}
            else if(card2.getTroops() == 2) {cSum += 4;}
            else if(card2.getTroops() == 3) {cSum += 13;}
            else {cSum += 0;}
            if(card3.getTroops() == 1) {cSum += 1;}
            else if(card3.getTroops() == 2) {cSum += 4;}
            else if(card3.getTroops() == 3) {cSum += 13;}
            else {cSum += 0;}

            int cardBonus = 0;

            // if 3 different cards
            if (cSum == 18 || cSum == 17 || cSum == 14 || cSum == 13 || cSum == 5 || cSum == 4 || cSum < 3) {
                cardBonus = 10;
            }
            else if (cSum % 13 == 0) {cardBonus = 8;} // 3x Artillery
            else if (cSum % 4 == 0) {cardBonus = 6;} // 3x Cavallery
            else if (cSum % 1 == 0) {cardBonus = 4;} // 3x Infantery

            // calculate card name bonus
            /*
             * 1 territory matching: +2 points
             * 2 territories matching: +4 points
             * 3 territories matching: +8 points
             */
            int cardNameBonus = 0;
            for (Territory t : game.getBoard().getTerritories()) {
                if (t.getName().equals(card1Name) && t.getOwner() == game.getTurnCycle().getCurrentPlayer().getPlayerId()) {
                    cardNameBonus += 1;
                }
                else if (t.getName().equals(card2Name) && t.getOwner() == game.getTurnCycle().getCurrentPlayer().getPlayerId()) {
                    cardNameBonus += 1;
                }
                else if (t.getName().equals(card3Name) && t.getOwner() == game.getTurnCycle().getCurrentPlayer().getPlayerId()) {
                    cardNameBonus += 1;
                }
                if (cardNameBonus == 3) {cardNameBonus = 6;}
            }

            // add card name bonus to card bonus
            cardBonus += cardNameBonus;
            
            // perform trade
            // increase troop bonus of current player by 2
            currentPlayer.setTroopBonus(currentPlayer.getTroopBonus() + cardBonus);
            currentPlayer.setCardBonus(currentPlayer.getCardBonus() + cardBonus);

            // change labels of the cards to be not handed out anymore
            card1.setHandedOut(false);
            card2.setHandedOut(false);
            card3.setHandedOut(false);

            // remove cards from player
            int i = 0;
            while (i < currentPlayer.getRiskCards().size()) {
                RiskCard card = currentPlayer.getRiskCards().get(i);
                if (card.getTerritoryName().equals(card1.getTerritoryName())) {
                    currentPlayer.getRiskCards().remove(card);
                }else if (card.getTerritoryName().equals(card2.getTerritoryName())) {
                    currentPlayer.getRiskCards().remove(card);
                }else if (card.getTerritoryName().equals(card3.getTerritoryName())) {
                    currentPlayer.getRiskCards().remove(card);
                }else {i++;}
            }

        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "Card trade failed. Two cards have the same troop type but not all three cards. This is not allowed.");
        }

        gameRepository.save(game);
        gameRepository.flush();

        return game;

    }


    public Game executeAttack(Game game, Attack attack, Territory attackingTerritory, Territory defendingTerritory, Random rand) {
        

        // specify how many troops the attacker and the defender use each
        int troopsFromAtk = Math.min(attackingTerritory.getTroops() - 1, Math.min(attack.getTroopsAmount(), 3));
        int troopsFromDef = Math.min(defendingTerritory.getTroops(), 2);

        // do dice rolling 
        ArrayList<Integer> atkRolls = new ArrayList<>();
        ArrayList<Integer> defRolls = new ArrayList<>();

        //Reset string because of length limit upon executing attack multiple times
        game.setDiceResult("");

        // Answer string
        String diceResult = game.getDiceResult();
        diceResult += "Atk: ";

        // attacker rolls dice between 1 and 3 times depending on situation. Results are added to an array.
        for (int i = 0; i < troopsFromAtk; i++) {
            atkRolls.add(rand.nextInt(6)+1);
            diceResult += atkRolls.get(i).toString() + " ";
        }
        
        // defender rolls dice between 1 and 2 times depending on situation. Results are added to an array.
        diceResult += "Def: ";
        for (int i = 0; i < troopsFromDef; i++) {
            defRolls.add(rand.nextInt(6)+1);
            diceResult += defRolls.get(i).toString() + " ";
        }
        // sort dice results of both arrays in descending order to figure out the dice pairs
        Collections.sort(atkRolls);
        Collections.reverse(atkRolls);
        Collections.sort(defRolls);
        Collections.reverse(defRolls);

        // compare first pair of dices
        if (atkRolls.get(0) > defRolls.get(0)) { // if attacker wins with the first pair
            defendingTerritory.setTroops(defendingTerritory.getTroops() - 1); // then remove a troop from the defending territory
        } else {
            attackingTerritory.setTroops(attackingTerritory.getTroops() - 1); // else remove a troop from the attacking territory
        }

        // compare second pair of dices if it exists (if both arrays have at least two dice rolls)
        if (atkRolls.size() > 1 && defRolls.size() > 1) {
            if (atkRolls.get(1) > defRolls.get(1)) { // if attacker wins with the second pair
                defendingTerritory.setTroops(defendingTerritory.getTroops() - 1); // then remove a troop from the defending territory
            } else {
                attackingTerritory.setTroops(attackingTerritory.getTroops() - 1); // else remove a troop from the attacking territory
            }
        }


        // put dice result into game entity
        game.setDiceResult(diceResult);

        

        return game;
    }

    //function for players to leave game
    public void leaveGame(Long gameId, Long lobbyId, Long userId){
        boolean exists = checkIfGameExists(gameId, true);
        if (!exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "No game with this id could be found.");
        }

        // get game from repository
        Game game = this.gameRepository.getByGameId(gameId);

        boolean removed = false;
        for (Player player : game.getTurnCycle().getPlayerCycle()) {
            if (player.getPlayerId() == userId){
                //check if it's users turn, if yes go to next user
                if (game.getTurnCycle().getCurrentPlayer() == player){
                    int nextPosition = game.getTurnCycle().getPlayerCycle().indexOf(player)+1;
                    if (nextPosition > game.getTurnCycle().getPlayerCycle().size()-1){
                        nextPosition=0;
                    }
                    game.getTurnCycle().setCurrentPlayer(game.getTurnCycle().getPlayerCycle().get(nextPosition));
                    game.getTurnCycle().setCurrentPhase(Phase.REINFORCEMENT);
                    game = distributeTroops(game, game.getTurnCycle().getCurrentPlayer().getPlayerId());
                }
                //remove player from turnCycle
                System.out.println("here1");
                game.getTurnCycle().getPlayerCycle().remove(player);

                System.out.println("here2");
                removed = true;

                //create a lobby so player can also get removed from lobby
                Lobby lobby = new Lobby();
                lobby.addPlayers(userId);

                //lobbyService.removePlayer(lobby, lobbyId);

                System.out.println(game.getTurnCycle().getPlayerCycle().size());
                //if last player has left game, delete game
                if (game.getTurnCycle().getPlayerCycle().size() == 0) {
                    deleteGame(gameId);
                    lobbyService.endGame(lobbyId);
                    return;
                }
                
                break;
            }
        }

        //if no player was found, raise error
        if (removed == false) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "No player with this id could be found.");
        }

        System.out.println("hello");

        gameRepository.save(game);
        gameRepository.flush();

        System.out.println("wieso");
    }
      
    public Game pullCard(Long gameId) {

        // get game from repository
        Game game = this.gameRepository.getByGameId(gameId);

        // shuffle the list of cards (card stack)
        Collections.shuffle(game.getCardStack().getRiskCards());

        // choose the first card that's free
        RiskCard pulledCard = null;

        for (int i = 0; i < 44; i++) {
            if (game.getCardStack().getRiskCards().get(i).isHandedOut() == false) {
                pulledCard = game.getCardStack().getRiskCards().get(i);
                break;
            }
        }

        // get current player
        Player currentPlayer = game.getTurnCycle().getCurrentPlayer();

        // add the new card to the player and label it to not be in the stack anymore
        currentPlayer.getRiskCards().add(pulledCard);
        pulledCard.setHandedOut(true);

        this.gameRepository.save(game);
        gameRepository.flush();
        
        return game;
    }


    // Helper function to check if a game with a certain Id exists in the repository
    private boolean checkIfGameExists(Long gameId, boolean shouldExist) {
        Game gameById = this.gameRepository.getByGameId(gameId);
        // shouldExist: if true, an error is thrown if the user doesn't exist (and vice versa)
        if (shouldExist && gameById == null) {
            return false;
        } else if (!shouldExist && gameById != null) {
            return false;
        }
        return true;
    }

    public Game randomizedBeginning(Game game){
        int AmountOfPlayers = game.getPlayers().size();
        int TerritoryPerPlayer = (game.getBoard().getTerritories().size() / AmountOfPlayers);

        List<Player> playerList = game.getPlayers();

        Random rand = new Random();

        //Shuffle territory list so destribution is random
        Collections.shuffle(game.getBoard().getTerritories());
        int i=0;
        int y=0;

        //amount of troops every player gets, is dependent on amount of players
        int maxTroops = 0;
        if (AmountOfPlayers == 2){
            maxTroops = 50-TerritoryPerPlayer;
        } else if (AmountOfPlayers == 3) {
            maxTroops = 35-TerritoryPerPlayer;
        } else if (AmountOfPlayers == 4) {
            maxTroops = 30-TerritoryPerPlayer;
        } else if (AmountOfPlayers == 5) {
            maxTroops = 25-TerritoryPerPlayer;
        } else if (AmountOfPlayers >= 6) {
            maxTroops = 20-TerritoryPerPlayer;
        }
        int troopPerTerritory = 0;
        int maxTroopsVar = maxTroops;

        //distribute Territory
        for (Territory territory : (game.getBoard().getTerritories())) {
            territory.setOwner(playerList.get(y).getPlayerId());
            i=i+1;
            //Assign random amount of Troops from (0 to 4) + 1 to country if maxTroops is bigger than 1
            if (maxTroopsVar > 4) {
                troopPerTerritory = rand.nextInt(5);
                territory.setTroops(troopPerTerritory+1);
                maxTroopsVar -= troopPerTerritory;
            } else if (maxTroopsVar > 1) {
                troopPerTerritory = (maxTroopsVar-1);
                territory.setTroops(troopPerTerritory+1);
                maxTroopsVar = 1;
            } else if (maxTroopsVar == 1) {
                territory.setTroops(maxTroopsVar+1);
                maxTroopsVar -= 1;
            } else {
                territory.setTroops(1);
            }

            //as soon as territory per player has been reached, distribute left over troops and move to next player
            if (i>=TerritoryPerPlayer){
                if (maxTroopsVar > 1) {
                    territory.setTroops(maxTroopsVar + territory.getTroops());
                }
                maxTroopsVar = maxTroops;
                y=y+1;
                i=0;
            }
            //If there is a rest amount, distribute it one by one to players
            if (y>=AmountOfPlayers){
                i=0;
                y=0;
                TerritoryPerPlayer=1;
                maxTroopsVar = 1;
                maxTroops = 1;
            }
        }

        //randomize order of players
        Collections.shuffle(game.getPlayers());

        //create TurnCycle for game
        TurnCycle turnCycle = new TurnCycle();
        turnCycle.setCurrentPlayer(game.getPlayers().get(0));
        turnCycle.setPlayerCycle(game.getPlayers());

        //Create Phase phase
        turnCycle.setCurrentPhase(Phase.REINFORCEMENT);

        //save turn cycle to game
        game.setTurnCycle(turnCycle);

        //Distribute troops for reinforcement phase for first player
        game = distributeTroops(game, game.getTurnCycle().getCurrentPlayer().getPlayerId());

        game = gameRepository.save(game);
        gameRepository.flush();

        return game;
    }
    

    // Helper function to initialize game
    private Game initializeGame(Game game) {

        //Set arrays of territories
        String[] africaTerritories = {"North Africa", "Egypt", "East Africa", "Central Africa", "South Africa", "Madagascar"};
        String[] asiaTerritories = {"Middle East", "Afghanistan", "Ural", "Siberia", "Yakutsk", "Kamchatka", "Irkutsk", "Mongolia", "China", "India", "Siam", "Japan"};
        String[] europeTerritories = {"Western Europe", "Southern Europe", "Northern Europe", "Great Britain", "Iceland", "Scandinavia", "Ukraine"};
        String[] northAmericaTerritories = {"Alaska", "Northwest Territory", "Greenland", "Alberta", "Ontario", "Quebec", "Western United States", "Eastern United States", "Central America"};
        String[] southAmericaTerritories = {"Venezuela", "Peru", "Brazil", "Argentina"};
        String[] australiaTerritories = {"Indonesia", "New Guinea", "Eastern Australia", "Western Australia"};

        int[] africaTerritoriesTroops = {2, 1, 1, 1, 3, 2};
        int[] asiaTerritoriesTroops = {1, 2, 2, 2, 2, 1, 2, 1, 1, 2, 1, 3};
        int[] europeTerritoriesTroops = {3, 3, 3, 3, 1, 2, 2};
        int[] northAmericaTerritoriesTroops = {1, 3, 2, 2, 2, 2, 3, 3, 3};
        int[] southAmericaTerritoriesTroops = {1, 1, 3, 1};
        int[] australiaTerritoriesTroops = {3, 2, 3, 3};

        //AFRICA----------------------------------------------------------------------

        Territory northAfrica = new Territory();
        northAfrica.setName("North Africa");
        northAfrica.setOwner(null);
        northAfrica.setTroops(0);

        Territory egypt = new Territory();
        egypt.setName("Egypt");
        egypt.setOwner(null);
        egypt.setTroops(0);

        Territory congo = new Territory();
        congo.setName("Congo");
        congo.setOwner(null);
        congo.setTroops(0);

        Territory eastAfrica = new Territory();
        eastAfrica.setName("East Africa");
        eastAfrica.setOwner(null);
        eastAfrica.setTroops(0);

        Territory southAfrica = new Territory();
        southAfrica.setName("South Africa");
        southAfrica.setOwner(null);
        southAfrica.setTroops(0);

        Territory madagascar = new Territory();
        madagascar.setName("Madagascar");
        madagascar.setOwner(null);
        madagascar.setTroops(0);

        Continent africa = new Continent();
        africa.setName("Africa");
        ArrayList<Territory> territoriesAfrica = new ArrayList<>();
        territoriesAfrica.add(northAfrica);
        territoriesAfrica.add(egypt);
        territoriesAfrica.add(congo);
        territoriesAfrica.add(eastAfrica);
        territoriesAfrica.add(southAfrica);
        territoriesAfrica.add(madagascar);
        africa.setTerritories(territoriesAfrica);
        africa.setAdditionalTroopBonus(3);

        //ASIA----------------------------------------------------------------------

        Territory yakutsk = new Territory();
        yakutsk.setName("Yakutsk");
        yakutsk.setOwner(null);
        yakutsk.setTroops(0);

        Territory ural = new Territory();
        ural.setName("Ural");
        ural.setOwner(null);
        ural.setTroops(0);

        Territory siberia = new Territory();
        siberia.setName("Siberia");
        siberia.setOwner(null);
        siberia.setTroops(0);

        Territory irkutsk = new Territory();
        irkutsk.setName("Irkutsk");
        irkutsk.setOwner(null);
        irkutsk.setTroops(0);

        Territory kamchatka = new Territory();
        kamchatka.setName("Kamchatka");
        kamchatka.setOwner(null);
        kamchatka.setTroops(0);

        Territory afghanistan = new Territory();
        afghanistan.setName("Afghanistan");
        afghanistan.setOwner(null);
        afghanistan.setTroops(0);

        Territory china = new Territory();
        china.setName("China");
        china.setOwner(null);
        china.setTroops(0);

        Territory mongolia = new Territory();
        mongolia.setName("Mongolia");
        mongolia.setOwner(null);
        mongolia.setTroops(0);

        Territory japan = new Territory();
        japan.setName("Japan");
        japan.setOwner(null);
        japan.setTroops(0);

        Territory middleEast = new Territory();
        middleEast.setName("Middle East");
        middleEast.setOwner(null);
        middleEast.setTroops(0);

        Territory india = new Territory();
        india.setName("India");
        india.setOwner(null);
        india.setTroops(0);

        Territory siam = new Territory();
        siam.setName("Siam");
        siam.setOwner(null);
        siam.setTroops(0);

        Continent asia = new Continent();
        asia.setName("Asia");

        ArrayList<Territory> territoriesAsia = new ArrayList<>();
        territoriesAsia.add(yakutsk);
        territoriesAsia.add(ural);
        territoriesAsia.add(siberia);
        territoriesAsia.add(irkutsk);
        territoriesAsia.add(kamchatka);
        territoriesAsia.add(afghanistan);
        territoriesAsia.add(china);
        territoriesAsia.add(mongolia);
        territoriesAsia.add(japan);
        territoriesAsia.add(middleEast);
        territoriesAsia.add(india);
        territoriesAsia.add(siam);

        asia.setTerritories(territoriesAsia);
        asia.setAdditionalTroopBonus(7);

        //AUSTRALIA----------------------------------------------------------------------

        Territory indonesia = new Territory();
        indonesia.setName("Indonesia");
        indonesia.setOwner(null);
        indonesia.setTroops(0);

        Territory newGuinea = new Territory();
        newGuinea.setName("New Guinea");
        newGuinea.setOwner(null);
        newGuinea.setTroops(0);

        Territory westernAustralia = new Territory();
        westernAustralia.setName("Western Australia");
        westernAustralia.setOwner(null);
        westernAustralia.setTroops(0);

        Territory easternAustralia = new Territory();
        easternAustralia.setName("Eastern Australia");
        easternAustralia.setOwner(null);
        easternAustralia.setTroops(0);

        Continent australia = new Continent();
        australia.setName("Australia");

        ArrayList<Territory> territoriesAustralia = new ArrayList<>();
        territoriesAustralia.add(indonesia);
        territoriesAustralia.add(newGuinea);
        territoriesAustralia.add(westernAustralia);
        territoriesAustralia.add(easternAustralia);

        australia.setTerritories(territoriesAustralia);
        australia.setAdditionalTroopBonus(2);

        //EUROPE----------------------------------------------------------------------

        Territory iceland = new Territory();
        iceland.setName("Iceland");
        iceland.setOwner(null);
        iceland.setTroops(0);

        Territory scandinavia = new Territory();
        scandinavia.setName("Scandinavia");
        scandinavia.setOwner(null);
        scandinavia.setTroops(0);

        Territory greatBritain = new Territory();
        greatBritain.setName("Great Britain");
        greatBritain.setOwner(null);
        greatBritain.setTroops(0);

        Territory northernEurope = new Territory();
        northernEurope.setName("Northern Europe");
        northernEurope.setOwner(null);
        northernEurope.setTroops(0);

        Territory ukraine = new Territory();
        ukraine.setName("Ukraine");
        ukraine.setOwner(null);
        ukraine.setTroops(0);

        Territory westernEurope = new Territory();
        westernEurope.setName("Western Europe");
        westernEurope.setOwner(null);
        westernEurope.setTroops(0);

        Territory southernEurope = new Territory();
        southernEurope.setName("Southern Europe");
        southernEurope.setOwner(null);
        southernEurope.setTroops(0);

        Continent europe = new Continent();
        europe.setName("Europe");

        ArrayList<Territory> territoriesEurope = new ArrayList<>();
        territoriesEurope.add(iceland);
        territoriesEurope.add(scandinavia);
        territoriesEurope.add(greatBritain);
        territoriesEurope.add(northernEurope);
        territoriesEurope.add(ukraine);
        territoriesEurope.add(westernEurope);
        territoriesEurope.add(southernEurope);

        europe.setTerritories(territoriesEurope);
        europe.setAdditionalTroopBonus(5);

        //NORTH AMERICA----------------------------------------------------------------------

        Territory alaska = new Territory();
        alaska.setName("Alaska");
        alaska.setOwner(null);
        alaska.setTroops(0);

        Territory northwestTerritory = new Territory();
        northwestTerritory.setName("Northwest Territory");
        northwestTerritory.setOwner(null);
        northwestTerritory.setTroops(0);

        Territory greenland = new Territory();
        greenland.setName("Greenland");
        greenland.setOwner(null);
        greenland.setTroops(0);

        Territory alberta = new Territory();
        alberta.setName("Alberta");
        alberta.setOwner(null);
        alberta.setTroops(0);

        Territory ontario = new Territory();
        ontario.setName("Ontario");
        ontario.setOwner(null);
        ontario.setTroops(0);

        Territory quebec = new Territory();
        quebec.setName("Quebec");
        quebec.setOwner(null);
        quebec.setTroops(0);

        Territory westernUS = new Territory();
        westernUS.setName("Western US");
        westernUS.setOwner(null);
        westernUS.setTroops(0);

        Territory easternUS = new Territory();
        easternUS.setName("Eastern US");
        easternUS.setOwner(null);
        easternUS.setTroops(0);

        Territory centralAmerica = new Territory();
        centralAmerica.setName("Central America");
        centralAmerica.setOwner(null);
        centralAmerica.setTroops(0);

        Continent northAmerica = new Continent();
        northAmerica.setName("North America");

        ArrayList<Territory> territoriesNorthAmerica = new ArrayList<>();
        territoriesNorthAmerica.add(alaska);
        territoriesNorthAmerica.add(northwestTerritory);
        territoriesNorthAmerica.add(greenland);
        territoriesNorthAmerica.add(alberta);
        territoriesNorthAmerica.add(ontario);
        territoriesNorthAmerica.add(quebec);
        territoriesNorthAmerica.add(westernUS);
        territoriesNorthAmerica.add(easternUS);
        territoriesNorthAmerica.add(centralAmerica);

        northAmerica.setTerritories(territoriesNorthAmerica);
        northAmerica.setAdditionalTroopBonus(5);

        //SOUTH AMERICA----------------------------------------------------------------------
        
        Territory venezuela = new Territory();
        venezuela.setName("Venezuela");
        venezuela.setOwner(null);
        venezuela.setTroops(0);
        
        Territory brazil = new Territory();
        brazil.setName("Brazil");
        brazil.setOwner(null);
        brazil.setTroops(0);
        
        Territory peru = new Territory();
        peru.setName("Peru");
        peru.setOwner(null);
        peru.setTroops(0);
        
        Territory argentina = new Territory();
        argentina.setName("Argentina");
        argentina.setOwner(null);
        argentina.setTroops(0);

        Continent southAmerica = new Continent();
        southAmerica.setName("South America");

        ArrayList<Territory> territoriesSouthAmerica = new ArrayList<>();
        territoriesSouthAmerica.add(venezuela);
        territoriesSouthAmerica.add(brazil);
        territoriesSouthAmerica.add(peru);
        territoriesSouthAmerica.add(argentina);

        southAmerica.setTerritories(territoriesSouthAmerica);
        southAmerica.setAdditionalTroopBonus(2);

        //BOARD----------------------------------------------------------------------

        Board board = new Board();

        ArrayList<Continent> continents = new ArrayList<>();
        continents.add(africa);
        continents.add(europe);
        continents.add(asia);
        continents.add(australia);
        continents.add(northAmerica);
        continents.add(southAmerica);

        ArrayList<Territory> territories = new ArrayList<>();
        territories.add(northAfrica);
        territories.add(egypt);
        territories.add(congo);
        territories.add(eastAfrica);
        territories.add(southAfrica);
        territories.add(madagascar);
        territories.add(iceland);
        territories.add(scandinavia);
        territories.add(greatBritain);
        territories.add(northernEurope);
        territories.add(ukraine);
        territories.add(westernEurope);
        territories.add(southernEurope);
        territories.add(yakutsk);
        territories.add(ural);
        territories.add(siberia);
        territories.add(irkutsk);
        territories.add(kamchatka);
        territories.add(afghanistan);
        territories.add(china);
        territories.add(mongolia);
        territories.add(japan);
        territories.add(middleEast);
        territories.add(india);
        territories.add(siam);
        territories.add(indonesia);
        territories.add(newGuinea);
        territories.add(westernAustralia);
        territories.add(easternAustralia);
        territories.add(alaska);
        territories.add(northwestTerritory);
        territories.add(greenland);
        territories.add(alberta);
        territories.add(ontario);
        territories.add(quebec);
        territories.add(westernUS);
        territories.add(easternUS);
        territories.add(centralAmerica);
        territories.add(venezuela);
        territories.add(brazil);
        territories.add(peru);
        territories.add(argentina);

        board.setContinents(continents);
        board.setTerritories(territories);

        game.setBoard(board);

        // set card stack

        CardStack cardStack = new CardStack();

        // Iterate over Africa territories
        int i = 0;
        for (String territory : africaTerritories) {
            RiskCard card = new RiskCard();
            card.setHandedOut(false);
            card.setTerritoryName(territory);
            card.setTroops(africaTerritoriesTroops[i]);
            cardStack.getRiskCards().add(card);
            i++;
        }

        // Iterate over Asia territories
        i = 0;
        for (String territory : asiaTerritories) {
            RiskCard card = new RiskCard();
            card.setHandedOut(false);
            card.setTerritoryName(territory);
            card.setTroops(asiaTerritoriesTroops[i]);
            cardStack.getRiskCards().add(card);
            i++;
        }

        // Iterate over Europe territories
        i = 0;
        for (String territory : europeTerritories) {
            RiskCard card = new RiskCard();
            card.setHandedOut(false);
            card.setTerritoryName(territory);
            card.setTroops(europeTerritoriesTroops[i]);
            cardStack.getRiskCards().add(card);
            i++;
        }

        // Iterate over North America territories
        i = 0;
        for (String territory : northAmericaTerritories) {
            RiskCard card = new RiskCard();
            card.setHandedOut(false);
            card.setTerritoryName(territory);
            card.setTroops(northAmericaTerritoriesTroops[i]);
            cardStack.getRiskCards().add(card);
            i++;
        }

        // Iterate over South America territories
        i = 0;
        for (String territory : southAmericaTerritories) {
            RiskCard card = new RiskCard();
            card.setHandedOut(false);
            card.setTerritoryName(territory);
            card.setTroops(southAmericaTerritoriesTroops[i]);
            cardStack.getRiskCards().add(card);
            i++;
        }

        // Iterate over Australia territories
        i = 0;
        for (String territory : australiaTerritories) {
            RiskCard card = new RiskCard();
            card.setHandedOut(false);
            card.setTerritoryName(territory);
            card.setTroops(australiaTerritoriesTroops[i]);
            cardStack.getRiskCards().add(card);
            i++;
        }

        // Add jokers

        RiskCard joker1 = new RiskCard();
        joker1.setHandedOut(false);
        joker1.setTerritoryName("Joker1");
        joker1.setTroops(0);
        cardStack.getRiskCards().add(joker1);

        RiskCard joker2 = new RiskCard();
        joker2.setHandedOut(false);
        joker2.setTerritoryName("Joker2");
        joker2.setTroops(0);
        cardStack.getRiskCards().add(joker2);

        game.setCardStack(cardStack);
        return game;
    }

    // Helper function to execute consequences on a game when it has been updated by a client (by comparing old state from repository vs. new state from client)
    private Game doConsequences(Game newState, Game oldState) {
        // This function simply transfers territory stats from the incoming request to the repository game
        for (int i = 0; i < oldState.getBoard().getTerritories().size(); i++) {
            oldState.getBoard().getTerritories().get(i).setTroops(newState.getBoard().getTerritories().get(i).getTroops());
            oldState.getBoard().getTerritories().get(i).setOwner(newState.getBoard().getTerritories().get(i).getOwner());
        }
        
        return oldState;
    }

}
