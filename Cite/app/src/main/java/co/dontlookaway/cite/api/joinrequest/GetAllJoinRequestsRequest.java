package co.dontlookaway.cite.api.joinrequest;

import com.android.volley.Request;

import co.dontlookaway.cite.api.CustomRequest;
import co.dontlookaway.cite.models.Group;

public class GetAllJoinRequestsRequest extends CustomRequest<GetAllJoinRequestsResponse> {
    private Group group;

    public GetAllJoinRequestsRequest(Group group) {
        this.group = group;
    }

    @Override
    public String getPath() {
        return String.format("/groups/%s/requests", this.group.getId());
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public Class<GetAllJoinRequestsResponse> getClazz() {
        return GetAllJoinRequestsResponse.class;
    }
}
