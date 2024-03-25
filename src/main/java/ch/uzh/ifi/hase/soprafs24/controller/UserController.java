package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
//@RequestMapping("/users")
@RestController
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/users")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<UserGetDTO> getAllUsers(
    @RequestHeader(name = "Authorization", required = true, defaultValue = "") String token, 
    @RequestHeader(name = "User_ID", required = true, defaultValue = "") String user_id) {
    // check if request is authorized
    System.out.println(token);
    System.out.println(user_id);
    userService.checkAuthorization(Long.parseLong(user_id), token);
    
    List<UserGetDTO> userGetDTOs = new ArrayList<>();
    // fetch all users in the internal representation
    List<User> users = userService.getUsers();
    // convert each user to the API representation
    for (User user : users) {
      userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
    }
    return userGetDTOs;
  }

  // get user information
  @GetMapping("/users/{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserGetDTO getUserById(@PathVariable("id") Long id,
    @RequestHeader(name = "Authorization", required = true, defaultValue = "") String token,
    @RequestHeader(name = "User_ID", required = true, defaultValue = "") String user_id) {
    // check if request is authorized
    userService.checkAuthorization(Long.parseLong(user_id), token);
    // fetch user in the internal representation
    User user = userService.getUserById(id);
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user with the given ID doesn't exist.");
    }
    // convert user to the API representation
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
  }

  // Registration
  @PostMapping("/users/registration")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO, HttpServletResponse response) {
    // convert API user to internal representation
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
    //String.format("Login failed. There exists no user with the username '%s'.", userData.getUsername()));
    // create user
    User createdUser = userService.createUser(userInput);
    if (createdUser == null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Login failed. There already exists an user with the username '%s'.", userInput.getUsername()));
    }
    response.addHeader("Authorization", createdUser.getToken());
    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
  }
  // Login
  @PostMapping("/users/login")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO loginUser(@RequestBody UserPostDTO userPostDTO, HttpServletResponse response) {
    // convert API user to internal representation
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
    System.out.println("userInput created");
    // 
    // log in user
    User loggedInUser = userService.loginUser(userInput);
    if (loggedInUser == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Login failed. There exists no user with the username '%s'.", userInput.getUsername()));
    }
    System.out.println("login complete");
    response.addHeader("Authorization", loggedInUser.getToken());
    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(loggedInUser);
  }
  // Logout
  @PostMapping("/users/logout")
  @ResponseStatus(HttpStatus.OK)
  public UserGetDTO logoutUser(@RequestHeader(name = "Authorization", required = true, defaultValue = "") String token,
    @RequestHeader(name = "User_ID", required = true, defaultValue = "") Long user_id) {
    // check if request is authorized
    userService.checkAuthorization(user_id, token);

    // log out user
    User loggedOutUser = userService.logoutUser(user_id);
    if (loggedOutUser == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Logout failed. There exists no user with ID %s.", user_id));
    }
    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(loggedOutUser);
  }

  // update user information
  @PutMapping("/users/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity updateUser(@PathVariable("id") Long id,
    @RequestBody UserPostDTO userPostDTO, @RequestHeader(name = "Authorization", required = true, defaultValue = "") String token,
    @RequestHeader(name = "User_ID", required = true, defaultValue = "") String user_id) {
    // check if request is authorized
    userService.checkAuthorization(Long.parseLong(user_id), token);
    // convert API user to internal representation
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
    // update user data
    User toUpdate = userService.updateUser(userInput, Long.parseLong(user_id));
    if (toUpdate == null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Name change failed. This username is already taken.");
    }
    return ResponseEntity.noContent().build();
  }
}
