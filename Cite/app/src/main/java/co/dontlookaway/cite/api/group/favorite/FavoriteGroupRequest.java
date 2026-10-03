package co.dontlookaway.cite.api.group.favorite;

import com.android.volley.Request;

import co.dontlookaway.cite.api.CustomRequest;

public class FavoriteGroupRequest extends CustomRequest<FavoriteGroupResponse> {
    private String groupId;

    public FavoriteGroupRequest(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String getPath() {
        return "/me/groups/favorite";
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Class<FavoriteGroupResponse> getClazz() {
        return FavoriteGroupResponse.class;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}
