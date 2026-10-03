package co.dontlookaway.cite.models;

public class Group {
    private String name;
    private String id;
    private User owner;
    private User[] admins;
    private User[] members;

    public Group(String name, String id, User owner, User[] admins, User[] members) {
        this.name = name;
        this.id = id;
        this.owner = owner;
        this.admins = admins;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User[] getAdmins() {
        return admins;
    }

    public void setAdmins(User[] admins) {
        this.admins = admins;
    }

    public User[] getMembers() {
        return members;
    }

    public void setMembers(User[] members) {
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}