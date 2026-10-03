package co.dontlookaway.cite.api.joinrequest;

import co.dontlookaway.cite.api.CustomResponse;
import co.dontlookaway.cite.models.JoinRequest;

public class GetAllJoinRequestsResponse extends CustomResponse {
    private JoinRequest[] requests;

    public GetAllJoinRequestsResponse(JoinRequest[] requests) {
        this.requests = requests;
    }

    public JoinRequest[] getRequests() {
        return requests;
    }

    public void setRequests(JoinRequest[] requests) {
        this.requests = requests;
    }
}
