package ch.uzh.ifi.hase.soprafs24.rest.dto;


public class UserPostDTO {

  private String username;
  private String password;
  private int avatarId;


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getAvatarId() {
    return this.avatarId;
  }

  public void setAvatarId(int avatarId) {
    this.avatarId = avatarId;
  }
}
