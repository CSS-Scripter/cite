package co.dontlookaway.cite.api.group;

import com.android.volley.Request;

import co.dontlookaway.cite.api.CustomRequest;

public class CreateGroupRequest extends CustomRequest<CreateGroupResponse> {
    private String name;

    public CreateGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPath() {
        return "/groups";
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Class<CreateGroupResponse> getClazz() {
        return CreateGroupResponse.class;
    }
}
