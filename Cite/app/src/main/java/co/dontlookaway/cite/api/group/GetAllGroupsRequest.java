package co.dontlookaway.cite.api.group;

import com.android.volley.Request;

import co.dontlookaway.cite.api.CustomRequest;

public class GetAllGroupsRequest extends CustomRequest<GetAllGroupsResponse> {
    @Override
    public String getPath() {
        return "/groups";
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public Class<GetAllGroupsResponse> getClazz() {
        return GetAllGroupsResponse.class;
    }
}
