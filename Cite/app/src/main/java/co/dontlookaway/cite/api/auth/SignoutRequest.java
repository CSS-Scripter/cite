package co.dontlookaway.cite.api.auth;

import com.android.volley.Request;

import co.dontlookaway.cite.api.CustomRequest;

public class SignoutRequest extends CustomRequest<SignoutResponse> {
    public SignoutRequest() {
    }

    @Override
    public String getPath() {
        return "/auth/signout";
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Class<SignoutResponse> getClazz() {
        return SignoutResponse.class;
    }
}
