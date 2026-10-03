package co.dontlookaway.cite.api.auth;

import co.dontlookaway.cite.api.CustomResponse;
import co.dontlookaway.cite.models.User;

public class MeResponse extends CustomResponse {
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    User currentUser;

    public MeResponse(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getUser() {
        return currentUser;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }
}