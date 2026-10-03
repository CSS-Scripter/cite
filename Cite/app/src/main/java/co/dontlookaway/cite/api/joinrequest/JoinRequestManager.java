package co.dontlookaway.cite.api.joinrequest;

import co.dontlookaway.cite.api.CustomResponse;
import co.dontlookaway.cite.api.GsonRequest;
import co.dontlookaway.cite.api.RequestCallback;
import co.dontlookaway.cite.models.Group;
import co.dontlookaway.cite.models.JoinRequest;
import co.dontlookaway.cite.util.ErrorHelper;

public class JoinRequestManager {

    public JoinRequestManager() {}

    public GsonRequest<GetAllJoinRequestsResponse> getAllGroupJoinRequestsRequest(Group group, RequestCallback<GetAllJoinRequestsResponse> callback) {
        return new GsonRequest<>(
                new GetAllJoinRequestsRequest(group),
                callback::onResponse,
                error -> new ErrorHelper<GetAllJoinRequestsResponse>().handleVolleyError(error, callback));
    }

    public GsonRequest<CustomResponse> processJoinRequest(JoinRequest joinRequest, boolean accepted, RequestCallback<CustomResponse> callback) {
        return new GsonRequest<>(
                new ProcessJoinRequestRequest(joinRequest, accepted),
                callback::onResponse,
                error -> new ErrorHelper<>().handleVolleyError(error, callback));
    }
}
