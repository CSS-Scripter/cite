package co.dontlookaway.cite.api.group;

import com.android.volley.Request;

import co.dontlookaway.cite.api.CustomRequest;
import co.dontlookaway.cite.models.Group;

public class JoinGroupRequest extends CustomRequest<JoinGroupResponse> {

    private String groupCode;

    public JoinGroupRequest(String groupCode) {
        this.groupCode = groupCode;
    }

    @Override
    public String getPath() {
        return String.format("/groups/%s/join", this.groupCode);
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Class<JoinGroupResponse> getClazz() {
        return JoinGroupResponse.class;
    }
}
