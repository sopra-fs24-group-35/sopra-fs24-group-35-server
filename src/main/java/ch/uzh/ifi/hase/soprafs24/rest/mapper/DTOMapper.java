package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GamePostDTO;

import ch.uzh.ifi.hase.soprafs24.entity.Territory;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TerritoryGetDTO;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;

import ch.uzh.ifi.hase.soprafs24.rest.dto.AttackPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.CardTradePostDTO;
import ch.uzh.ifi.hase.soprafs24.entity.Attack;
import ch.uzh.ifi.hase.soprafs24.entity.CardTrade;
import ch.uzh.ifi.hase.soprafs24.entity.UserList;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserListPostDTO;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyPostDTO;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {

  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

  //User
  @Mapping(source = "avatarId", target = "avatarId")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "password", target = "password")
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "avatarId", target = "avatarId")
  @Mapping(source = "id", target = "id")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "status", target = "status")
  UserGetDTO convertEntityToUserGetDTO(User user);

  //Lobby
  @Mapping(source = "userIdList", target = "userIdList")
  UserList convertUserListPostDTOtoEntity(UserListPostDTO userListPostDTO);

  @Mapping(source = "players", target = "players")
  Lobby convertLobbyPostDTOtoEntity(LobbyPostDTO lobbyPostDTO);

  @Mapping(source = "code", target = "code")
  @Mapping(source = "players", target = "players")
  Lobby convertLobbyPutDTOtoEntity(LobbyPutDTO lobbyPutDTO);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "code", target = "code")
  @Mapping(source = "ownerId", target = "ownerId")
  @Mapping(source = "players", target = "players")
  @Mapping(source = "gameId", target = "gameId")
  LobbyGetDTO convertEntityToLobbyGetDTO(Lobby lobby);

  //Game
  @Mapping(source = "board", target = "board")
  @Mapping(source = "players", target = "players")
  @Mapping(source = "turnCycle", target = "turnCycle")
  Game convertGamePostDTOtoEntity(GamePostDTO gamePostDTO);

  @Mapping(source = "gameId", target = "gameId")
  @Mapping(source = "board", target = "board")
  @Mapping(source = "players", target = "players")
  @Mapping(source = "turnCycle", target = "turnCycle")
  @Mapping(source = "diceResult", target = "diceResult")
  GameGetDTO convertEntityToGameGetDTO(Game game);

  //Territory
  @Mapping(source = "name", target = "name")
  @Mapping(source = "owner", target = "owner")
  @Mapping(source = "troops", target = "troops")
  TerritoryGetDTO convertEntityToTerritoryGetDTO(Territory territory);
  
  //Attack
  @Mapping(source = "attackingTerritory", target = "attackingTerritory")
  @Mapping(source = "defendingTerritory", target = "defendingTerritory")
  @Mapping(source = "troopsAmount", target = "troopsAmount")
  @Mapping(source = "repeats", target = "repeats")
  Attack convertAttackPostDTOtoEntity(AttackPostDTO attackPostDTO);

  //CardTrade
  @Mapping(source = "card1Name", target = "card1Name")
  @Mapping(source = "card2Name", target = "card2Name")
  @Mapping(source = "card3Name", target = "card3Name")
  CardTrade convertCardTradePostDTOtoEntity(CardTradePostDTO cardTradePostDTO);
}
