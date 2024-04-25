package ch.uzh.ifi.hase.soprafs24.constant;

import java.util.ArrayList;

public enum Phase {
    REINFORCEMENT, ATTACK, MOVE;

    private static final Phase[] vals = values();
    
    public Phase next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }
}

