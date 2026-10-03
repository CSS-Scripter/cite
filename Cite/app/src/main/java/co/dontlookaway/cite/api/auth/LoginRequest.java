package co.dontlookaway.cite.api.auth;

import com.android.volley.Request;

import co.dontlookaway.cite.api.CustomRequest;

public class LoginRequest extends CustomRequest<LoginResponse> {
    String email;
    String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String getPath() {
        return "/auth/signin";
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Class<LoginResponse> getClazz() {
        return LoginResponse.class;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
