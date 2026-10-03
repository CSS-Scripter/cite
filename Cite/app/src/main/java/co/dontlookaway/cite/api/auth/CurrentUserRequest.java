package co.dontlookaway.cite.api.auth;

import com.android.volley.Request;

import co.dontlookaway.cite.api.CustomRequest;

public class CurrentUserRequest extends CustomRequest<CurrentUserResponse> {
    @Override
    public String getPath() {
        return "/me";
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public Class<CurrentUserResponse> getClazz() {
        return CurrentUserResponse.class;
    }
}
