package co.dontlookaway.cite.models;

public class JoinRequest {
    private String id;
    private User user;
    private ShieldedGroup group;

    public JoinRequest(String id, User user, ShieldedGroup group) {
        this.id = id;
        this.user = user;
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ShieldedGroup getGroup() {
        return group;
    }

    public void setGroup(ShieldedGroup group) {
        this.group = group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
