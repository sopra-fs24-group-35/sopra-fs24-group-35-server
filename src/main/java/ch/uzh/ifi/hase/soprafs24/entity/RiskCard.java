package ch.uzh.ifi.hase.soprafs24.entity;



import javax.persistence.*;
import java.io.Serializable;


@Embeddable
public class RiskCard implements Serializable {

    @Column(nullable = false, unique = false)
    private String type;
    
}
