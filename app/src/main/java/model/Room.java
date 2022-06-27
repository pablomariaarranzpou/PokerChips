package model;

import java.util.UUID;

public class Room {

    private int numPlayers;
    private int inPlayers;
    private int numChips;
    private String roomID;

    public Room(int numPlayers, int numChips) {
        this.numPlayers = numPlayers;
        this.inPlayers = 0;
        this.numChips = numChips;
    }
}
