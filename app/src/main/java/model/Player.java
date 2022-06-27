package model;

public class Player {

    private String username;
    private int userChips;

    public Player(String username, int userChips) {
        this.username = username;
        this.userChips = userChips;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserChips() {
        return userChips;
    }

    public void setUserChips(int userChips) {
        this.userChips = userChips;
    }
}
