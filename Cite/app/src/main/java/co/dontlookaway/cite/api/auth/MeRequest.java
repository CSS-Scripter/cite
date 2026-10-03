package co.dontlookaway.cite.api.auth;

import com.android.volley.Request;

import co.dontlookaway.cite.api.CustomRequest;

public class MeRequest  extends CustomRequest<MeResponse> {
    public MeRequest() {
    }

    @Override
    public String getPath() {
        return "/me";
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public Class<MeResponse> getClazz() {
        return MeResponse.class;
    }
}
