package co.dontlookaway.cite.models;

public class User {
    private String id;
    private String email;
    private String username;
    private String[] favoriteGroups;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String[] getFavoriteGroups() {
        return favoriteGroups;
    }

    public void setFavoriteGroups(String[] favoriteGroups) {
        this.favoriteGroups = favoriteGroups;
    }
}
