package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
public class UserList {

    @Id
    @GeneratedValue
    private Long id;
    
    @Column(nullable = false)
    private ArrayList<Long> userIdList = new ArrayList<Long>();

    public ArrayList<Long> getUserIdList(){
        return userIdList;
    }

    public void setUserIdList(ArrayList<Long> userIdList){
        this.userIdList = userIdList;
    }
}
