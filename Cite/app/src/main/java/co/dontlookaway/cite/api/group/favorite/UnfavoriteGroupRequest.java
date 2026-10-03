package co.dontlookaway.cite.api.group.favorite;

import com.android.volley.Request;

import co.dontlookaway.cite.api.CustomRequest;

public class UnfavoriteGroupRequest extends CustomRequest<UnfavoriteGroupResponse> {
    private String groupId;

    public UnfavoriteGroupRequest(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String getPath() {
        return "/me/groups/unfavorite";
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Class<UnfavoriteGroupResponse> getClazz() {
        return UnfavoriteGroupResponse.class;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
