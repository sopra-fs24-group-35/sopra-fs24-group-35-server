package ch.uzh.ifi.hase.soprafs24.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import ch.uzh.ifi.hase.soprafs24.entity.Game;

@Repository("gameRepository")
public interface GameRepository extends JpaRepository<Game, Long> {

    Game getById(Long gameId);

    void deleteById(Long gameId);
    
}
