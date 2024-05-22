package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Test
  public void givenUser_whenGetUser_thenReturnJsonUser() throws Exception {
    // given
    User user = new User();
    user.setId(1L);
    user.setUsername("firstname@lastname");
    user.setPassword("12345678");
    user.setToken(("abc"));
    user.setStatus(UserStatus.OFFLINE);


    // this mocks the UserService -> we define above what the userService should
    // return when getUser() is called
    given(userService.getUserById(1L)).willReturn(user);

    // when
    MockHttpServletRequestBuilder getRequest = get("/users/1")
      .contentType(MediaType.APPLICATION_JSON)
      .header("User_ID", "1")
      .header("Authorization", "abc");

    // then
    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$.username", is(user.getUsername())))
        .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
  }

  @Test
  public void givenNoUser_whenGetUser_thenThrowException() throws Exception {

    // this mocks the UserService -> we define above what the userService should
    // return when getUser() is called
    given(userService.getUserById(1L)).willReturn(null);
    
    // when
    MockHttpServletRequestBuilder getRequest = get("/users/10")
      .contentType(MediaType.APPLICATION_JSON)
      .header("User_ID", "10")
      .header("Authorization", "abc");

    // then
    mockMvc.perform(getRequest).andExpect(status().isNotFound());
        
  }

  @Test
  public void createUser_validInput_userCreated() throws Exception {
    // given
    User user = new User();
    user.setId(1L);
    user.setUsername("testUsername");
    user.setToken("abc");
    user.setPassword("12345678");
    user.setStatus(UserStatus.ONLINE);

    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setUsername("testUsername");

    given(userService.createUser(Mockito.any())).willReturn(user);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users/registration")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO));
    
    mockMvc.perform(postRequest).andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(user.getId().intValue())))
        .andExpect(jsonPath("$.username", is(user.getUsername())))
        .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
  }


  @Test
  public void createUser_usernameAlreadyExists_thenThrowsException() throws Exception {
    // given
    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setUsername("testUsername");

    given(userService.createUser(Mockito.any())).willReturn(null);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users/registration")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO));


    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isConflict());
  }

  @Test
  public void loginUser_validInput_userLoggedIn() throws Exception {
    // given
    User user = new User();
    user.setId(1L);
    user.setUsername("testUsername");
    user.setToken("abc");
    user.setPassword("12345678");
    user.setStatus(UserStatus.ONLINE);

    UserPostDTO userPostDTO = new UserPostDTO();;
    userPostDTO.setUsername("testUsername");
    userPostDTO.setPassword("12345678");

    given(userService.loginUser(Mockito.any())).willReturn(user);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO));

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(user.getId().intValue())))
        .andExpect(jsonPath("$.username", is(user.getUsername())))
        .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
  }

  @Test
  public void loginUser_userDoesntExist_thenThrowsException() throws Exception {
    // given
    UserPostDTO userPostDTO = new UserPostDTO();;
    userPostDTO.setUsername("testUsername");

    given(userService.loginUser(Mockito.any())).willReturn(null);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO));

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isNotFound());
  }

  @Test
  public void logoutUser_validInput_userLoggedOut() throws Exception {
    // given
    User user = new User();
    user.setId(1L);
    user.setUsername("testUsername");
    user.setToken("abc");
    user.setPassword("12345678");
    user.setStatus(UserStatus.OFFLINE);

    UserPostDTO userPostDTO = new UserPostDTO();;
    userPostDTO.setUsername("testUsername");

    given(userService.logoutUser(Mockito.any())).willReturn(user);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users/logout")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO))
        .header("User_ID", "1")
        .header("Authorization", "abc");

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(user.getId().intValue())))
        .andExpect(jsonPath("$.username", is(user.getUsername())))
        .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
  }

  @Test
  public void logoutUser_userDoesntExist_thenThrowsError() throws Exception {
    // given
    UserPostDTO userPostDTO = new UserPostDTO();;
    userPostDTO.setUsername("testUsername");

    given(userService.logoutUser(Mockito.any())).willReturn(null);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users/logout")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO))
        .header("User_ID", "1")
        .header("Authorization", "abc");

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isNotFound());
  }


  @Test
  public void updateUser_validInput_userUpdated() throws Exception {
    // given
    User user = new User();
    user.setId(1L);
    user.setUsername("testUsername");
    user.setToken("abc");
    user.setPassword("12345678");
    user.setStatus(UserStatus.OFFLINE);

    UserPostDTO userPostDTO = new UserPostDTO();;
    userPostDTO.setUsername("testUsername");

    given(userService.updateUser(Mockito.any(), Mockito.any())).willReturn(user);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = put("/users/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO))
        .header("User_ID", "1")
        .header("Authorization", "abc");
    

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isNoContent());
  }

  public void updateUser_userDoesntExist_thenThrowsException() throws Exception {
    // given
    UserPostDTO userPostDTO = new UserPostDTO();;
    userPostDTO.setUsername("testUsername");

    given(userService.updateUser(Mockito.any(), Mockito.any())).willReturn(null);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = put("/users/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO))
        .header("User_ID", "1")
        .header("Authorization", "abc");
    

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isNotFound());
  }

  @Test
  public void updateUser_invalidInput_usernameAlreadyTaken() throws Exception {
    // given
    UserPostDTO userPostDTO = new UserPostDTO();;
    userPostDTO.setUsername("testUsername");

    given(userService.updateUser(Mockito.any(), Mockito.any())).willReturn(null);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = put("/users/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO))
        .header("User_ID", "1")
        .header("Authorization", "abc");
    

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isConflict());
  }
  /**
   * Helper Method to convert userPostDTO into a JSON string such that the input
   * can be processed
   * Input will look like this: {"username": "testUsername", "password": "12345678"}
   * 
   * @param object
   * @return string
   */
  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e.toString()));
    }
  }
}