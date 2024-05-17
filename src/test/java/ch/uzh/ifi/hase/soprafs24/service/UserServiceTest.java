package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private User testUser;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // given
    testUser = new User();
    testUser.setId(1L);
    testUser.setUsername("testUsername");
    testUser.setPassword("12345678");
    testUser.setStatus(UserStatus.ONLINE);

    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    Mockito.when(userRepository.getById(Mockito.any())).thenReturn(testUser);
  
  }

  @Test
  public void createUser_validInputs_success() {
    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    User createdUser = userService.createUser(testUser);

    // then
    Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

    assertEquals(testUser.getId(), createdUser.getId());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertNotNull(createdUser.getToken());
    //When a user is created, he is automatically logged in after, so the user status should be ONLINE.
    assertEquals(UserStatus.ONLINE, createdUser.getStatus());
  }

  

  @Test
  public void createUser_duplicateUsername_returnsNull() {
    // given -> a first user has already been created
    userService.createUser(testUser);

    // when -> setup additional mocks for UserRepository
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

    // then -> attempt to create second user with same user -> check that an error
    // is thrown
    // check that null is returned
    assertNull(userService.createUser(testUser));
  }

@Test
  public void testGetUserByUsername() {

      Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

      User foundUser = userService.getUserByUsername(testUser.getUsername());
      assertNotNull(foundUser);
      assertEquals(testUser.getUsername(), foundUser.getUsername());
      verify(userRepository, times(1)).findByUsername(testUser.getUsername());
  }

  @Test
  public void testGetUserById() {

      Mockito.when(userRepository.getById(Mockito.any())).thenReturn(testUser);

    
      User foundUser = userService.getUserById(testUser.getId());
      assertNotNull(foundUser);
      assertEquals(testUser.getId(), foundUser.getId());
  }

  @Test
  public void testGetUserById_UserDoesNotExist() {
      Mockito.when(userRepository.getById(Mockito.any())).thenReturn(null);
      User foundUser = userService.getUserById(testUser.getId());
      assertNull(foundUser);
  }

  @Test
  public void testLoginUser_Success() {
      Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
      User loginUser = new User();
      loginUser.setUsername(testUser.getUsername());
      loginUser.setPassword(testUser.getPassword());

      User loggedInUser = userService.loginUser(loginUser);
      assertNotNull(loggedInUser);
      assertEquals(UserStatus.ONLINE, loggedInUser.getStatus());
      assertNotNull(loggedInUser.getToken());
  }

  @Test
  public void testLoginUser_WrongPassword() {
      Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
      User loginUser = new User();
      loginUser.setUsername(testUser.getUsername());
      loginUser.setPassword("wrongPassword");

      ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
          userService.loginUser(loginUser);
      });

      assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
      assertEquals("Login failed. The password is wrong.", exception.getReason());
  }

  @Test
  public void testLogoutUser() {
      Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
      User loggedOutUser = userService.logoutUser(testUser.getId());
      assertNotNull(loggedOutUser);
      assertEquals(UserStatus.OFFLINE, loggedOutUser.getStatus());
  }

  @Test
  public void testLogoutUser_UserDoesNotExist() {
      Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
      User loggedOutUser = userService.logoutUser(testUser.getId());
      assertNotNull(loggedOutUser);
  }

  @Test
  public void testCheckAuthorization_Success() {
      testUser.setToken("abc");
      Mockito.when(userRepository.getById(Mockito.any())).thenReturn(testUser);
      userService.checkAuthorization(testUser.getId(), testUser.getToken());
      verify(userRepository, times(1)).getById(testUser.getId());
  }

  @Test
  public void testCheckAuthorization_Failure() {
      testUser.setToken("abc");
      User unauthorizedUser = new User();
      unauthorizedUser.setId(2L);
      unauthorizedUser.setToken("wrongToken");

      Mockito.when(userRepository.getById(Mockito.any())).thenReturn(testUser);

      ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
          userService.checkAuthorization(testUser.getId(), "wrongToken");
      });

      assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
      assertEquals("Authorization failed. The user is not allowed to access data on the server.", exception.getReason());
      verify(userRepository, times(1)).getById(testUser.getId());
  }

  @Test
  public void testUpdateUser_SuccessfulUpdate() {
      // Mocking checkIfUserExistsUsername to return true
      Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

      User updatedUserData = new User();
      updatedUserData.setUsername("updatedUsername");

      User updatedUser = userService.updateUser(updatedUserData, testUser.getId());

      assertNotNull(updatedUser);
      assertEquals(updatedUserData.getUsername(), updatedUser.getUsername());
      verify(userRepository, times(1)).save(testUser);
  }

  @Test
  public void testUpdateUser_NoUserFound() {
      // Mocking checkIfUserExistsUsername to return false
      Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

      User updatedUserData = new User();
      updatedUserData.setUsername("updatedUsername");

      User updatedUser = userService.updateUser(updatedUserData, testUser.getId());

      assertNull(updatedUser);
      verify(userRepository, never()).save(testUser);
  }

  @Test
  public void testUpdateUser_NullUsername() {
      // Mocking checkIfUserExistsUsername to return true
      Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

      User updatedUserData = new User(); // No username provided

      User updatedUser = userService.updateUser(updatedUserData, testUser.getId());

      assertNotNull(updatedUser);
      assertEquals(testUser.getUsername(), updatedUser.getUsername()); // Username should remain unchanged
      verify(userRepository, times(1)).save(testUser);
  }

}
