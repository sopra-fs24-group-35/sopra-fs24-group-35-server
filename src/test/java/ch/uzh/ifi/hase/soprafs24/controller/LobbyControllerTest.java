package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyPutDTO;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
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
import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(LobbyController.class)
public class LobbyControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private LobbyService lobbyService;

  @Test
  public void getExistingLobbyWithID() throws Exception {
    // given
    Lobby lobby = new Lobby();
    lobby.setId(1L);
    lobby.setCode("1234");
    lobby.setToken(("abc"));
    lobby.setOwnerId(2L);


    // this mocks the UserService -> we define above what the userService should
    // return when getUser() is called
    given(lobbyService.getLobbyById(1L)).willReturn(lobby);

    // when
    MockHttpServletRequestBuilder getRequest = get("/lobbies/1")
      .contentType(MediaType.APPLICATION_JSON)
      .header("Lobby_ID", "1")
      .header("Authorization", "abc");

    // then
    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$.code", is(lobby.getCode())))
        .andExpect(jsonPath("$.ownerId", is(lobby.getOwnerId().intValue())));
  }

  @Test
  public void getNonexistentLobbyWithId_ErrorMessage() throws Exception {

    //given
    given(lobbyService.getLobbyById(1L)).willReturn(null);

    //when
    MockHttpServletRequestBuilder getRequest = get("/lobbies/1")
    .contentType(MediaType.APPLICATION_JSON)
    .header("Lobby_ID", "1")
    .header("Authorization", "abc");

    //then
    mockMvc.perform(getRequest).andExpect(status().isNotFound());
  }

  @Test
  public void createLobby_validInput() throws Exception {

    //given
    ArrayList<Long> players = new ArrayList<Long>();
    players.add(2L);

    Lobby lobby = new Lobby();
    lobby.setId(1L);
    lobby.setCode("1234");
    lobby.setToken(("abc"));
    lobby.setOwnerId(2L);
    lobby.setPlayers(players);

    LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
    lobbyPostDTO.setPlayers(players);

    given(lobbyService.createLobby(Mockito.any())).willReturn(lobby);

    //when
    MockHttpServletRequestBuilder postRequest = post("/lobbies")
    .contentType(MediaType.APPLICATION_JSON)
    .content(asJsonString(lobbyPostDTO));

    //then
    mockMvc.perform(postRequest).andExpect(status().isCreated())
    .andExpect(jsonPath("$.id", is(lobby.getId().intValue())))
    .andExpect(jsonPath("$.code", is(lobby.getCode())))
    .andExpect(jsonPath("$.ownerId", is(lobby.getOwnerId().intValue())))
    .andExpect(jsonPath(("$.players"), hasItems(2)));
  }

  @Test
  public void createLobby_invalidInput_noPlayers() throws Exception {

    //given
    LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();

    given(lobbyService.createLobby(Mockito.any())).willReturn(null);

    //when
    MockHttpServletRequestBuilder postRequest = post("/lobbies")
    .contentType(MediaType.APPLICATION_JSON)
    .content(asJsonString(lobbyPostDTO));

    //then
    mockMvc.perform(postRequest).andExpect(status().isNotFound());
  }

  @Test
  public void updateLobbies_validInput() throws Exception {

    //given
    ArrayList<Long> bothPlayers = new ArrayList<Long>();
    bothPlayers.add(2L);
    bothPlayers.add(3L);

    ArrayList<Long> players = new ArrayList<Long>();
    players.add(3L);

    Lobby lobby = new Lobby();
    lobby.setId(1L);
    lobby.setCode("1234");
    lobby.setToken(("abc"));
    lobby.setOwnerId(2L);
    lobby.setPlayers(bothPlayers);

    LobbyPutDTO lobbyPutDTO = new LobbyPutDTO();
    lobbyPutDTO.setCode("1234");
    lobbyPutDTO.setPlayers(players);

    given(lobbyService.updateLobby(Mockito.any())).willReturn(lobby);

    //when
    MockHttpServletRequestBuilder putRequest = put("/lobbies")
    .contentType(MediaType.APPLICATION_JSON)
    .content(asJsonString(lobbyPutDTO));

    //then
    mockMvc.perform(putRequest).andExpect(status().isOk())
    .andExpect(jsonPath("$.id", is(lobby.getId().intValue())))
    .andExpect(jsonPath("$.code", is(lobby.getCode())))
    .andExpect(jsonPath("$.ownerId", is(lobby.getOwnerId().intValue())))
    .andExpect(jsonPath("$.players", hasItems(2, 3)));
  }

  @Test
  public void updateLobbies_invalidInput_wrongCode() throws Exception {

    //given
    ArrayList<Long> players = new ArrayList<Long>();
    players.add(3L);

    LobbyPutDTO lobbyPutDTO = new LobbyPutDTO();
    lobbyPutDTO.setCode("0");
    lobbyPutDTO.setPlayers(players);

    given(lobbyService.updateLobby(Mockito.any())).willReturn(null);

    //when
    MockHttpServletRequestBuilder putRequest = put("/lobbies")
    .contentType(MediaType.APPLICATION_JSON)
    .content(asJsonString(lobbyPutDTO));

    //then
    mockMvc.perform(putRequest).andExpect(status().isNotFound());
  }

  @Test
  public void updateLobbies_invalidInput_noPlayers() throws Exception {

    //given
    ArrayList<Long> players = new ArrayList<Long>();
    players.add(2L);

    Lobby lobby = new Lobby();
    lobby.setId(1L);
    lobby.setCode("1234");
    lobby.setToken(("abc"));
    lobby.setOwnerId(2L);
    lobby.setPlayers(players);

    LobbyPutDTO lobbyPutDTO = new LobbyPutDTO();
    lobbyPutDTO.setCode("1234");
    lobbyPutDTO.setPlayers(null);

    given(lobbyService.updateLobby(Mockito.any())).willReturn(null);

    //when
    MockHttpServletRequestBuilder putRequest = put("/lobbies")
    .contentType(MediaType.APPLICATION_JSON)
    .content(asJsonString(lobbyPutDTO));

    //then
    mockMvc.perform(putRequest).andExpect(status().isNotFound());
  }

  @Test
  public void removePlayer_validInput() throws Exception {

    //given
    ArrayList<Long> players = new ArrayList<Long>();
    players.add(3L);

    Lobby lobby = new Lobby();
    lobby.setId(1L);
    lobby.setCode("1234");
    lobby.setToken(("abc"));
    lobby.setOwnerId(2L);
    lobby.addPlayers(2L);

    LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
    lobbyPostDTO.setPlayers(players);

    given(lobbyService.removePlayer(Mockito.any(), Mockito.any())).willReturn(lobby);

    //when
    MockHttpServletRequestBuilder putRequest = put("/lobbies/1")
    .contentType(MediaType.APPLICATION_JSON)
    .content(asJsonString(lobbyPostDTO));

    //then
    mockMvc.perform(putRequest).andExpect(status().isOk());
  }

  @Test
  public void removePlayer_invalidInput_wrongId() throws Exception {

    //given
    ArrayList<Long> players = new ArrayList<Long>();
    players.add(3L);

    LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
    lobbyPostDTO.setPlayers(players);

    given(lobbyService.removePlayer(Mockito.any(), Mockito.any())).willReturn(null);

    //when
    MockHttpServletRequestBuilder putRequest = put("/lobbies/1")
    .contentType(MediaType.APPLICATION_JSON)
    .content(asJsonString(lobbyPostDTO));

    //then
    mockMvc.perform(putRequest).andExpect(status().isNotFound());
  }

  @Test
  public void removePlayer_invalidInput_wrongPlayer() throws Exception {

    //given
    ArrayList<Long> bothPlayers = new ArrayList<Long>();
    bothPlayers.add(2L);
    bothPlayers.add(3L);

    ArrayList<Long> players = new ArrayList<Long>();
    players.add(4L);

    Lobby lobby = new Lobby();
    lobby.setId(1L);
    lobby.setCode("1234");
    lobby.setToken(("abc"));
    lobby.setOwnerId(2L);
    lobby.setPlayers(bothPlayers);

    LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
    lobbyPostDTO.setPlayers(players);

    given(lobbyService.removePlayer(Mockito.any(), Mockito.any())).willReturn(null);

    //when
    MockHttpServletRequestBuilder putRequest = put("/lobbies/1")
    .contentType(MediaType.APPLICATION_JSON)
    .content(asJsonString(lobbyPostDTO));

    //then
    mockMvc.perform(putRequest).andExpect(status().isNotFound());
  }


  /**
   * Helper Method to convert lobbyPostDTO into a JSON string such that the input
   * can be processed
   * Input will look like this: {"code": "1234"}
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