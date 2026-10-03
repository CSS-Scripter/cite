package co.dontlookaway.cite.api.auth;

import com.android.volley.Request;

import co.dontlookaway.cite.api.CustomRequest;

public class RegisterRequest extends CustomRequest<RegisterResponse> {
    String email;
    String username;
    String password;

    public RegisterRequest(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    @Override
    public String getPath() {
        return "/auth/signup";
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Class<RegisterResponse> getClazz() {
        return RegisterResponse.class;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
