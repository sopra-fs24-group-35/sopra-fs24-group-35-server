package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.ArrayList;


public class UserListPostDTO{

    ArrayList<Long> userIdList = new ArrayList<Long>();

    public ArrayList<Long> getUserIdList(){
        return userIdList;
    }

    public void setUserIdList(ArrayList<Long> userIdList){
        this.userIdList = userIdList;
    }
}