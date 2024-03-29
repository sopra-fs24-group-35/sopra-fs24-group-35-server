package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository("userRepository")
public interface LobbyRepository extends JpaRepository<Lobby, Long> {

  Lobby findByCode(String code);

  Lobby getById(Long id);
}
