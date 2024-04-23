package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class TerritoryGetDTO {
    private String name;
    private Long owner;
    private int troops;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for owner
    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    // Getter and setter for troops
    public int getTroops() {
        return troops;
    }

    public void setTroops(int troops) {
        this.troops = troops;
    }
}