package co.dontlookaway.cite.api.group;

import com.android.volley.Request;

import co.dontlookaway.cite.api.CustomRequest;

public class LeaveGroupRequest extends CustomRequest<LeaveGroupResponse> {
    private String groupId;

    public LeaveGroupRequest(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String getPath() {
        return "/me/groups/leave";
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Class<LeaveGroupResponse> getClazz() {
        return LeaveGroupResponse.class;
    }
}
