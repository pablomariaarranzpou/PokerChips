package model;

public class Player {

    private String username, player_id;
    private int userChips;

    public Player(String username, int userChips, String player_id) {
        this.username = username;
        this.userChips = userChips;
        this.player_id = player_id;
    }

    public String getPlayer_id() {
        return player_id;
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
