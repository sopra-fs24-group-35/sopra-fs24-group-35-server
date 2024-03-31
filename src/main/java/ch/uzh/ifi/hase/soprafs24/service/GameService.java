package ch.uzh.ifi.hase.soprafs24.service;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;

@Service
@Transactional
public class GameService {
    
    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;

    public GameService(@Qualifier("gameRepository") GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game getGameById(Long gameId) {
        boolean exists = checkIfGameExists(gameId, true);
        if (!exists) {
            return null;
        }
        return this.gameRepository.getById(gameId);
    }

    public Game createGame(Game newGame) {
        newGame = initializeGame(newGame);
        newGame = gameRepository.save(newGame);
        gameRepository.flush();

        log.debug("Created a Game");
        return newGame;
    }

    public Game updateGame(Game updatedGame) {
        updatedGame = doConsequences(updatedGame, gameRepository.getById(updatedGame.getGameId()));
        updatedGame = gameRepository.save(updatedGame);
        gameRepository.flush();

        log.debug("Updated a Game");
        return updatedGame;
    }

    public Game deleteGame(Game toDelete) {
        boolean exists = checkIfGameExists(toDelete.getGameId(), true);
        if (!exists) {
            return null;
        }
        gameRepository.deleteById(toDelete.getGameId());
        gameRepository.flush();

        log.debug("Deleted a Game");
        return toDelete;
    }


    // Helper function to check if a game with a certain Id exists in the repository
    private boolean checkIfGameExists(Long gameId, boolean shouldExist) {
        Game gameById = this.gameRepository.getById(gameId);
        // shouldExist: if true, an error is thrown if the user doesn't exist (and vice versa)
        if (shouldExist && gameById == null) {
            return false;
        } else if (!shouldExist && gameById != null) {
            return false;
        }
        return true;
    }

    // Helper function to initialize game
    private Game initializeGame(Game game) {
        // TODO: Insert code to initialize the game here (e.g. distribute troops etc.)
        return game;
    }

    // Helper function to execute consequences on a game when it has been updated by a client (by comparing old state from repository vs. new state from client)
    private Game doConsequences(Game newState, Game oldState) {
        // TODO: Insert code for consequences after a game update (e.g. remove troops, change owner of territory etc.)
        return newState;
    }

}
