package ch.uzh.ifi.hase.soprafs24.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Territory;

@Repository("gameRepository")
public interface GameRepository extends JpaRepository<Game, Long> {

    Game getByGameId(Long gameId);

    void deleteByGameId(Long gameId);
    
}
