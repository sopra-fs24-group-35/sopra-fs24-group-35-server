package ch.uzh.ifi.hase.soprafs24.service;

import java.util.ArrayList;
import java.util.Collections;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

import ch.uzh.ifi.hase.soprafs24.entity.Board;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.Phase;
import ch.uzh.ifi.hase.soprafs24.entity.Continent;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Territory;
import ch.uzh.ifi.hase.soprafs24.entity.TurnCycle;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

import java.util.List;
import java.util.Arrays;
import java.util.Random;

@Service
@Transactional
public class GameService {
    
    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;

    private UserService userService;

    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, UserService userService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    public Game getGameById(Long gameId) {
        boolean exists = checkIfGameExists(gameId, true);
        if (!exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
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

    public void updateGame(Game updatedGame, Long gameId) {

        // throww error if game with the given id doesn't exist
        boolean exists = checkIfGameExists(gameId, true);
        if (!exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Game update failed, because there is no game with this id.");
        }

        // update game
        updatedGame = doConsequences(updatedGame, gameRepository.getByGameId(updatedGame.getGameId()));

        // save updated game to repository
        updatedGame = gameRepository.save(updatedGame);
        gameRepository.flush();

        log.debug("Updated a Game");
        return;
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

        return null;

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
            //System.out.println(player.getPlayerId());
            //System.out.println(player.getUsername());
            updatedGame.addPlayers(player);
        }



        //Save Game in repository
        updatedGame = gameRepository.save(updatedGame);
        gameRepository.flush();

        return updatedGame;
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
                troopPerTerritory = (maxTroops);
                territory.setTroops(troopPerTerritory+1);
                maxTroopsVar = 1;
            } else if (maxTroopsVar == 1) {
                territory.setTroops(maxTroopsVar);
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
        Phase phase = new Phase();
        phase.setCurrentPhase("setup");
        turnCycle.setCurrentPhase(phase);

        //save turn cycle to game
        game.setTurnCycle(turnCycle);

        game = gameRepository.save(game);
        gameRepository.flush();

        return game;
    }

    // Helper function to initialize game
    private Game initializeGame(Game game) {
        // TODO: Insert code to initialize the game here (e.g. distribute troops etc.)

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
        africa.setAdditionalTroopBonus(4);

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
        asia.setAdditionalTroopBonus(4);

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
        australia.setAdditionalTroopBonus(4);

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
        europe.setAdditionalTroopBonus(4);

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
        northAmerica.setAdditionalTroopBonus(4);

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
        southAmerica.setAdditionalTroopBonus(4);

        //BOARD----------------------------------------------------------------------

        Board board = new Board();

        ArrayList<Continent> continents = new ArrayList<>();
        continents.add(europe);
        continents.add(asia);
        continents.add(australia);
        continents.add(northAmerica);
        continents.add(southAmerica);

        ArrayList<Territory> territories = new ArrayList<>();
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

        return game;
    }

    // Helper function to execute consequences on a game when it has been updated by a client (by comparing old state from repository vs. new state from client)
    private Game doConsequences(Game newState, Game oldState) {
        // TODO: Insert code for consequences after a game update (e.g. remove troops, change owner of territory etc.)
        return newState;
    }

}
