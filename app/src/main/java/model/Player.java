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


    public int getUserChips() {
        return userChips;
    }

    public String getUserChipstoString() {
        return String.valueOf(userChips);
    }

}
