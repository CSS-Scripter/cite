package co.dontlookaway.cite.api.joinrequest;

import android.widget.ProgressBar;

import com.android.volley.Request;

import co.dontlookaway.cite.api.CustomRequest;
import co.dontlookaway.cite.api.CustomResponse;
import co.dontlookaway.cite.models.JoinRequest;

public class ProcessJoinRequestRequest extends CustomRequest<CustomResponse> {

    private JoinRequest joinRequest;
    private boolean accepted;

    public ProcessJoinRequestRequest(JoinRequest joinRequest, boolean accepted) {
        this.joinRequest = joinRequest;
        this.accepted = accepted;
    }

    @Override
    public String getPath() {
        return String.format("/groups/%s/requests/%s", this.joinRequest.getGroup().getId(), this.joinRequest.getId());
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Class<CustomResponse> getClazz() {
        return CustomResponse.class;
    }
}
