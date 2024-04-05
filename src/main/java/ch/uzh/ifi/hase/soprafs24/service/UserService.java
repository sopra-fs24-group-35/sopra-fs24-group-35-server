package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User getUserByUsername(String username) {
    return this.userRepository.findByUsername(username);
  }


  public User getUserById(Long user_id) {
    boolean exists = checkIfUserExistsId(user_id, true);
    if (!exists) {
      return null;
    }
    return this.userRepository.getById(user_id);
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User loginUser(User userData) {
    try {
      boolean exists = checkIfUserExistsUsername(userData, true);
      if (!exists) {
        return null;
      }
      User user = getUserByUsername(userData.getUsername());
      if (userData.getPassword().equals(user.getPassword())) {
        user.setToken(UUID.randomUUID().toString());
        user.setStatus(UserStatus.ONLINE);
        return user;
      }
      else {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Login failed. The password is wrong.");
      }
    }
    catch(Exception e) {
      throw e;
    }

  }

  public User logoutUser(Long user_id) {
    
    boolean exists = checkIfUserExistsId(user_id, true);
    if (!exists) {
      return null;
    }
    User user = getUserById(user_id);
    user.setStatus(UserStatus.OFFLINE);
    return user;

  }

  public void checkAuthorization(Long user_id, String token) {
    User userById = userRepository.getById(user_id);
    if (userById == null || !userById.getToken().equals(token)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
      "Authorization failed. The user is not allowed to access data on the server.");
    }
    return;
  }

  public User createUser(User newUser) {
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setStatus(UserStatus.ONLINE);
    boolean exists = checkIfUserExistsUsername(newUser, false);
    if (!exists) {
      return null;
    }
    // saves the given entity but data is only persisted in the database once
    // flush() is called
    newUser = userRepository.save(newUser);
    userRepository.flush();

    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  public User updateUser(User userData, Long id) {
    boolean exists = checkIfUserExistsUsername(userData, false);
    if (!exists) {
      return null;
    }
    // get user from repository
    User toUpdate = getUserById(id);
    // Update Username and Birthday
    if (userData.getUsername() != null) {
      toUpdate.setUsername(userData.getUsername());
    }
    
    toUpdate = userRepository.save(toUpdate);
    userRepository.flush();

    log.debug("Updated Information for User: {}", toUpdate);
    return toUpdate;
  }

  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */

   // Check if user exists by username
  private boolean checkIfUserExistsUsername(User userToBeCreated, boolean shouldExist) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
    // shouldExist: if true, an error is thrown if the user doesn't exist (and vice versa)
    if (shouldExist && userByUsername == null) {
      return false;
    } else if (!shouldExist && userByUsername != null) {
      return false;
    }
    return true;
  }
  // Check if user exists by ID
  private boolean checkIfUserExistsId(Long user_id, boolean shouldExist) {
    User userById = this.userRepository.getById(user_id);
    // shouldExist: if true, an error is thrown if the user doesn't exist (and vice versa)
    if (shouldExist && userById == null) {
      return false;
    } else if (!shouldExist && userById != null) {
      return false;
    }
    return true;
  }
}
