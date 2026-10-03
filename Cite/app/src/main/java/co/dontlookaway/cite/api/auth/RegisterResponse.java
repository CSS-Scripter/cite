package co.dontlookaway.cite.api.auth;

import co.dontlookaway.cite.api.CustomResponse;
import co.dontlookaway.cite.models.User;

public class RegisterResponse extends CustomResponse {
    String jwt;
    User user;

    public RegisterResponse(String jwt, User user) {
        this.jwt = jwt;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
