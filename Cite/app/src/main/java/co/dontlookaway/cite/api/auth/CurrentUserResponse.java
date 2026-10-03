package co.dontlookaway.cite.api.auth;

import co.dontlookaway.cite.api.CustomResponse;
import co.dontlookaway.cite.models.User;

public class CurrentUserResponse extends CustomResponse {
    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }
}
